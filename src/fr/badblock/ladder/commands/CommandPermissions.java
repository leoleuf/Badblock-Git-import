package fr.badblock.ladder.commands;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonElement;

import fr.badblock.ladder.Proxy;
import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.chat.ChatColor;
import fr.badblock.ladder.api.commands.Command;
import fr.badblock.ladder.api.entities.CommandSender;
import fr.badblock.ladder.api.entities.OfflinePlayer;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.ladder.api.utils.StringUtils;
import fr.badblock.ladder.api.utils.Time;
import fr.badblock.ladder.http.HttpHandler;
import fr.badblock.ladder.http.SlackMessage;
import fr.badblock.permissions.PermissibleGroup;
import fr.badblock.permissions.PermissiblePlayer;
import fr.badblock.permissions.Permission;
import fr.badblock.permissions.PermissionManager;
import fr.badblock.utils.Callback;

public class CommandPermissions extends Command {

	private String[] help = new String[] {
			ChatColor.GOLD + "/perms groups : " + ChatColor.RED + "Liste des groupes",
			ChatColor.GOLD + "/perms group <group> create/destroy (<inheritance>) : " + ChatColor.RED + "Ajoute/Détruit un groupe",
			ChatColor.GOLD + "/perms group <group> add/remove <perm> : " + ChatColor.RED + "Manipule les permissions d'un groupe",
			ChatColor.GOLD + "/perms group <group> inheritance <group> : " + ChatColor.RED + "Définit le groupe parent d'un groupe",
			ChatColor.GOLD + "/perms user <user> destroy : " + ChatColor.RED + "Suppression de toutes les permissions/groupe de l'utilisateur",
			ChatColor.GOLD + "/perms user <user> add/remove <perm> : " + ChatColor.RED + "Modifie les permissions du joueur",
			ChatColor.GOLD + "/perms user <user> group add/set/remove <group> (<time>) : " + ChatColor.RED + "Manipule les groupes du joueur",
			ChatColor.GOLD + "/perms user <user> send : " + ChatColor.RED + "Envoit les permissions du joueur",
	};

	public CommandPermissions() {
		super("permissions", null, "perms");
	}

	PermissionManager getPermissionsManager() {
		return PermissionManager.getInstance();
	}

	@Override
	public void executeCommand(CommandSender sender, String[] args) {
		if (!sender.hasPermission("perms") && !sender.hasPermission("ladder.commands.permission.upgrade")) {
			sender.sendMessage("§cVous n'avez pas la permission d'exécuter cette commande.");
			return;
		}
		if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
			help(sender);
			return;
		}
		if (args[0].equalsIgnoreCase("groups")) {
			groupsList(sender);
			return;
		} 
		if (args[0].equalsIgnoreCase("group") && args.length > 1) {
			PermissibleGroup group = getPermissionsManager().getGroup(args[1]);
			if (!groupExists(sender, group, args)) return;
			if(args.length == 2) {
				groupInfo(sender, group);
				return;
			}
			if(args[2].equalsIgnoreCase("create")) {
				groupCreate(sender, group, args);
				return;
			}
			if(args[2].equalsIgnoreCase("destroy")) {
				groupDestroy(sender, group, args);
				return;
			}
			if (args[2].equalsIgnoreCase("add") && args.length == 4) {
				groupPermissionAdd(sender, group, args);
				return;
			}
			if(args[2].equalsIgnoreCase("remove") && args.length == 4) {
				groupPermissionRemove(sender, group, args);
				return;
			}
			if(args[2].equalsIgnoreCase("inheritance") && args.length == 4) {
				if (!sender.hasPermission("ladder.commands.permission.groups." + group.getName() + ".changeinheritance." + args[3])) {
					sender.sendMessage("§cVous n'avez pas la permission d'ajouter cette inhéritance à ce groupe.");
					return;
				}
				String inheritance = getPermissionsManager().getGroup(args[3]).getName();
				group.setSuperGroup(inheritance);
				sender.sendMessage(ChatColor.GOLD + "Le groupe " + ChatColor.RED + args[1] + ChatColor.GOLD + " a maintenant comme inhéritance " + ChatColor.RED + inheritance);
				return;
			}else{
				help(sender);
				return;
			}
		}
		if(args[0].equalsIgnoreCase("user") && args.length >= 2){
			OfflinePlayer player = Ladder.getInstance().getOfflinePlayer(args[1]);
			if(!player.hasPlayed()) {
				sender.sendMessage(ChatColor.RED + "Le joueur '" + args[1] + "' n'a jamais joué !");
				return;
			}

			PermissiblePlayer p = (PermissiblePlayer) player.getAsPermissible();
			if (args.length == 2) {
				if (!sender.hasPermission("ladder.commands.permission.groups.upgrade." + p.getSuperGroup())) {
					sender.sendMessage("§cVous n'avez pas la permission de voir les permissions de ce joueur.");
					return;
				}
				for (String alternateGroups : p.getAlternateGroups().keySet()) {
					if (!sender.hasPermission("ladder.commands.permission.groups.upgrade." + alternateGroups)) {
						sender.sendMessage("§cVous n'avez pas la permission de voir les permissions de ce joueur.");
						return;
					}
				}
				List<String> messages = new ArrayList<>();
				messages.add(ChatColor.GOLD + "Le joueur " + ChatColor.RED + player.getName() + ChatColor.GOLD + " a comme groupe principal : " + ChatColor.RED + p.getSuperGroup());
				messages.add(ChatColor.GOLD + "Ses groups secondaires sont : " + ChatColor.RED + StringUtils.join(p.getAlternateGroups().keySet(), ","));
				messages.add(ChatColor.GOLD + "Permission supplémentaires : ");
				p.getPermissions().forEach(permission -> messages.add(ChatColor.RED + "- " + ChatColor.GOLD + permission.toString()));

				sender.sendMessages(messages.toArray(new String[0]));
				return;
			}
			if (args[2].equalsIgnoreCase("destroy")) {
				if (!sender.hasPermission("ladder.commands.permission.groups.upgrade." + p.getSuperGroup())) {
					sender.sendMessage("§cVous n'avez pas la permission de retirer les permissions de ce joueur.");
					return;
				}
				for (String alternateGroups : p.getAlternateGroups().keySet()) {
					if (!sender.hasPermission("ladder.commands.permission.groups.upgrade." + alternateGroups)) {
						sender.sendMessage("§cVous n'avez pas la permission de retirer les permissions de ce joueur.");
						return;
					}
				}
				p.removeAll();
				player.saveData();
				Player po = Proxy.getInstance().getPlayer(player.getName());
				if (po != null) {
					po.sendToBukkit("permissions");
					po.sendToBungee("permissions");
				}
				sender.sendMessage(ChatColor.GOLD + "Les permissions du joueur ont bien été supprimées !");
				return;
			}
			if (args[2].equalsIgnoreCase("add") && args.length == 4) {
				if (!sender.hasPermission("ladder.commands.permission.user.addpermission." + args[3])) {
					sender.sendMessage("§cVous n'avez pas la permission d'ajouter cette permission à ce joueur.");
					return;
				}
				Permission perm = new Permission(args[3]);
				if(p.permissionExist(perm)){
					sender.sendMessage(ChatColor.RED + "Le joueur a déjà cette permission !");
				} else {
					p.addPermission(perm);
					player.saveData();
					Player po = Proxy.getInstance().getPlayer(player.getName());
					if (po != null) {
						po.sendToBukkit("permissions");
						po.sendToBungee("permissions");
					}
					sender.sendMessage(ChatColor.GOLD + "Le joueur " + ChatColor.RED + args[1] + ChatColor.GOLD + " a maintenant la permission " + ChatColor.RED + perm);
				}
				return;
			}
			if(args[2].equalsIgnoreCase("remove") && args.length == 4){
				if (!sender.hasPermission("ladder.commands.permission.user.removepermission." + args[3])) {
					sender.sendMessage("§cVous n'avez pas la permission d'ajouter cette permission à ce joueur.");
					return;
				}
				Permission perm = new Permission(args[3]);
				if(!p.permissionExist(perm)){
					sender.sendMessage(ChatColor.RED + "Le joueur n'a pas cette permission !");
				} else {
					p.removePermission(perm);
					player.saveData();
					Player po = Proxy.getInstance().getPlayer(player.getName());
					if (po != null) {
						po.sendToBukkit("permissions");
						po.sendToBungee("permissions");
					}
					sender.sendMessage(ChatColor.GOLD + "Le joueur " + ChatColor.RED + args[1] + ChatColor.GOLD + " n'a plus la permission " + ChatColor.RED + perm);
				}
				return;
			}
			if(args[2].equalsIgnoreCase("group") && args.length >= 5){
				PermissibleGroup group = getPermissionsManager().getGroup(args[4]);
				long end = -1;
				long duration = -1;
				String durationStr = "à vie";
				if (args.length == 6) {
					if (!sender.hasPermission("ladder.commands.permission.groups.managethemwithtime")) {
						sender.sendMessage("§cVous n'avez pas la permission de gérer l'attribution d'un groupe avec un temps.");
						return;
					}
					try {
						duration    = Time.MILLIS_SECOND.matchTime(args[5]);
						end 	    = System.currentTimeMillis() + duration;
						durationStr = Time.MILLIS_SECOND.toFrench(duration, Time.HOUR, Time.MONTH);
					}catch(Exception e) {
						sender.sendMessage(ChatColor.RED + args[5] + " n'est pas un temps valide !");
						return;
					}
				}
				if (!group.getName().equalsIgnoreCase(args[4])) {
					sender.sendMessage(ChatColor.RED + "Le groupe '" + args[4] + "' n'existe pas !");
					return;
				}
				if(args[3].equalsIgnoreCase("set")) {
					if (!sender.hasPermission("ladder.commands.permission.groups.upgrade." + p.getSuperGroup())) {
						sender.sendMessage("§cVous n'avez pas la permission de définir ce groupe à ce joueur.");
						return;
					}
					if (!sender.hasPermission("ladder.commands.permission.groups.upgrade." + args[4])) {
						sender.sendMessage("§cVous n'avez pas la permission de définir ce groupe à ce joueur.");
						return;
					}
					if (!p.getParent().getName().equals("default") && !sender.hasPermission("ladder.commands.permission.groups.upgrade." + p.getParent().getName())) {
						sender.sendMessage("§cVous n'avez pas la permission de définir ce groupe à ce joueur.");
						return;
					}
					
					logAddGroup(sender, p, group);
					
					p.setParent(end, group);
					player.saveData();
					Player po = Proxy.getInstance().getPlayer(player.getName());
					if (po != null) {
						po.sendToBukkit("permissions");
						po.sendToBungee("permissions");
					}
					sender.sendMessage(ChatColor.GOLD + "Le joueur a maintenant pour groupe " + ChatColor.RED + group.getName() + ChatColor.GOLD + " (" + durationStr + ").");
					return;
				}
				if (args[3].equalsIgnoreCase("add")) {
					if (!sender.hasPermission("ladder.commands.permission.groups.upgrade." + p.getSuperGroup())) {
						sender.sendMessage("§cVous n'avez pas la permission d'ajouter ce groupe à ce joueur.");
						return;
					}
					if (!sender.hasPermission("ladder.commands.permission.groups.upgrade." + args[4])) {
						sender.sendMessage("§cVous n'avez pas la permission d'ajouter ce groupe à ce joueur.");
						return;
					}

					logAddGroup(sender, p, group);
					
					if (p.getSuperGroup().equalsIgnoreCase("default")) { // pas de groupe
						p.setParent(end, group);
					} else {
						p.addParent(end, group);
					}
					player.saveData();
					Player po = Proxy.getInstance().getPlayer(player.getName());
					if (po != null) {
						po.sendToBukkit("permissions");
						po.sendToBungee("permissions");
					}
					sender.sendMessage(ChatColor.GOLD + "Le joueur a maintenant pour groupe " + ChatColor.RED + group.getName() + ChatColor.GOLD + " (" + durationStr + ").");
					return;
				} 
				if (args[3].equalsIgnoreCase("remove")) {
					if (!sender.hasPermission("ladder.commands.permission.groups.upgrade." + p.getSuperGroup())) {
						sender.sendMessage("§cVous n'avez pas la permission de supprimer ce groupe à ce joueur.");
						return;
					}
					if (!sender.hasPermission("ladder.commands.permission.groups.upgrade." + args[4])) {
						sender.sendMessage("§cVous n'avez pas la permission de supprimer ce groupe à ce joueur.");
						return;
					}
					p.removeParent(group);
					player.saveData();
					Player po = Proxy.getInstance().getPlayer(player.getName());
					if (po != null) {
						po.sendToBukkit("permissions");
						po.sendToBungee("permissions");
					}
					sender.sendMessage(ChatColor.GOLD + "Le joueur n'a plus le groupe " + ChatColor.RED + group.getName() + ChatColor.GOLD + ".");
					return;
				}
			}else{
				help(sender);
				return;
			}
			player.saveData();
			Player pl = Ladder.getInstance().getPlayer(player.getName());
			if (pl != null) {
				pl.sendToBungee("permissions");
				pl.sendToBukkit("permissions");
			}
		}else{
			help(sender); return;
		}
	}

	private void logAddGroup(CommandSender sender, PermissiblePlayer p, PermissibleGroup group) {
		if(sender.hasPermission("ladder.commands.permission.group.logaddgroup")){
			JsonElement value = group.getValue("store_group_offer");
			
			if(value != null){
				int offer = value.getAsInt();
				
				new HttpHandler(new Callback<String>(){
					@Override
					public void done(String result, Throwable error) {
						if(error != null){
							error.printStackTrace();
							return;
						}
						
						try {
							Boolean has = Boolean.parseBoolean(result);
							
							if(has == Boolean.FALSE){
								post("@channel Ajout du groupe " + group.getName() + " à " + p.getName() + " par " + sender.getName() + " ! Celui-ci n'a pas acheté le groupe ! :rage:");
								System.out.println("méééé euh !");
							} else {
								post("Ajout du groupe " + group.getName() + " à " + p.getName() + " par " + sender.getName() + " !");
								System.out.println("méééé euh !");
							}
						} catch(Exception e){
							post("Ajout du groupe " + group.getName() + " à " + p.getName() + " par " + sender.getName() + " !");
							System.out.println("méééé euh !");
						}
					}
				}, "https://badblock.fr/store/player_has_buy.php?user=" + p.getName().toLowerCase() + "&offer=" + Integer.toString(offer)).start();
			} else {
				post("Ajout du groupe " + group + " à " + p.getName() + " par " + sender.getName() + " !");
				System.out.println("méééé euh !");
			}
		}
	}
	
	private void post(String message){
		new SlackMessage(message, "https://hooks.slack.com/services/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", "info_given-perms", "BottyPerms", false).run();
	}

	public void groupPermissionRemove(CommandSender sender, PermissibleGroup group, String[] args) {
		if (!sender.hasPermission("ladder.commands.permission.groups." + group.getName() + ".addpermission." + args[3])) {
			sender.sendMessage("§cVous n'avez pas la permission de retirer cette permissions à ce groupe.");
			return;
		}
		Permission perm = new Permission(args[3]);
		if(!group.permissionExist(perm)){
			sender.sendMessage(ChatColor.RED + "Le groupe n'a pas cette permission !");
		} else {
			group.removePermission(perm);
			sender.sendMessage(ChatColor.GOLD + "Le groupe " + ChatColor.RED + args[1] + ChatColor.GOLD + " n'a plus la permission " + ChatColor.RED + perm);
		}
	}

	public void groupPermissionAdd(CommandSender sender, PermissibleGroup group, String[] args) {
		if (!sender.hasPermission("ladder.commands.permission.groups." + group.getName() + ".addpermission." + args[3])) {
			sender.sendMessage("§cVous n'avez pas la permission d'ajouter cette permissions à ce groupe.");
			return;
		}
		Permission perm = new Permission(args[3]);
		if(group.permissionExist(perm)){
			sender.sendMessage(ChatColor.RED + "Le groupe a déjà cette permission !");
		} else {
			group.addPermission(perm);
			sender.sendMessage(ChatColor.GOLD + "Le groupe " + ChatColor.RED + args[1] + ChatColor.GOLD + " a maintenant la permission " + ChatColor.RED + perm);
		}
	}

	public boolean groupExists(CommandSender sender, PermissibleGroup group, String[] args) {
		if (!group.getName().equalsIgnoreCase(args[1]) && !(args.length == 3 && args[2].equalsIgnoreCase("create"))) {
			sender.sendMessage(ChatColor.RED + "Le groupe '" + args[1] + "' n'existe pas !");
			return false;
		}
		return true;
	}

	public void groupCreate(CommandSender sender, PermissibleGroup group, String[] args) {
		if (!sender.hasPermission("ladder.commands.permission.groups.create")) {
			sender.sendMessage("§cVous n'avez pas la permission de créer un groupe.");
			return;
		}
		String inheritance = "default";
		if (args.length == 4) {
			inheritance = getPermissionsManager().getGroup(args[3]).getName();
		}
		getPermissionsManager().createGroup(args[1], inheritance);
		sender.sendMessage(ChatColor.GOLD + "Le groupe " + ChatColor.RED + args[1] + ChatColor.GOLD + " a bien été créé avec comme inhéritance " + ChatColor.RED + inheritance);
	}

	public void groupDestroy(CommandSender sender, PermissibleGroup group, String[] args) {
		if (!sender.hasPermission("ladder.commands.permission.groups." + group.getName() + ".destroy")) {
			sender.sendMessage("§cVous n'avez pas la permission de détruire un groupe.");
			return;
		}
		getPermissionsManager().removeGroup(args[1]);
		sender.sendMessage(ChatColor.GOLD + "Le groupe a bien été supprimé !");
	}

	public void groupInfo(CommandSender sender, PermissibleGroup group) {
		if (!sender.hasPermission("ladder.commands.permission.groups." + group.getName() + ".showpermissions")) {
			sender.sendMessage("§cVous n'avez pas la permission de voir les permissions de ce groupe.");
			return;
		}
		List<String> messages = new ArrayList<>();
		messages.add(ChatColor.GOLD + "Le groupe " + ChatColor.RED + group.getName() + ChatColor.GOLD + " hérite de : " + ChatColor.RED + group.getSuperGroup());
		messages.add(ChatColor.GOLD + "Permission supplémentaires : ");
		for(Permission permission : group.getPermissions()){
			messages.add(ChatColor.RED + "- " + ChatColor.GOLD + permission.toString());
		}
		sender.sendMessages(messages.toArray(new String[0]));
	}

	public void groupsList(CommandSender sender) {
		if (!sender.hasPermission("ladder.commands.permission.groupslist")) {
			sender.sendMessage("§cVous n'avez pas la permission de voir la liste des groupes existants.");
			return;
		}
		List<String> messages = new ArrayList<>();
		messages.add(ChatColor.GOLD + "Liste des groupes d'assignations possibles :");
		for(String group : getPermissionsManager().getGroupsName()){
			if (!sender.hasPermission("ladder.commands.permission.groups.upgrade." + group) && !sender.hasPermission("ladder.commands.permission.groupslist.bypass")) continue;
			messages.add(ChatColor.RED + "- " + ChatColor.GOLD + group);
		}
		sender.sendMessages(messages.toArray(new String[0]));
	}

	public void help(CommandSender sender) {
		sender.sendMessages(help);
	}

}
