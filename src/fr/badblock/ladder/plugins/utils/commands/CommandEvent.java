package fr.badblock.ladder.plugins.utils.commands;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.chat.ChatColor;
import fr.badblock.ladder.api.commands.Command;
import fr.badblock.ladder.api.entities.Bukkit;
import fr.badblock.ladder.api.entities.CommandSender;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.ladder.plugins.utils.Animation;

public class CommandEvent extends Command {
	public CommandEvent() {
		super("event", null, "anim");
	}

	@Override
	public void executeCommand(CommandSender sender, String[] args) {
		if(args.length == 0){
			sendHelp(sender);
		} else if(args[0].equalsIgnoreCase("join") && sender instanceof Player){
			joinEvent((Player) sender);
		} else if(args[0].equalsIgnoreCase("create") && sender.hasPermission("event.create")){
			createEvent(sender, args);
		} else if(args[0].equalsIgnoreCase("stop") && sender.hasPermission("event.stop")){
			stopEvent(sender);
		} else sendHelp(sender);
	}

	public void sendHelp(CommandSender sender){
		if(sender.hasPermission("event.create"))
			sender.sendMessage(ChatColor.RED + "Utilisez /event create <serveur> <fallbackServer> <timeBetweenMessages> <animationLength (-1 = no unlimited)> <eventName (space = _)>");
		if(sender.hasPermission("event.stop"))
			sender.sendMessage(ChatColor.RED + "Utilisez /event stop");
	}

	public void joinEvent(Player player){
		if(Animation.ANIMATION != null){
			if(!player.getBukkitServer().getName().startsWith("lobby")){
				player.sendMessage(ChatColor.RED + "Vous devez être dans un lobby pour vous téléporter à une animation !");
			} else player.connect(Animation.ANIMATION.getServer());
		}
	}

	public void createEvent(CommandSender sender, String[] args){
		if(Animation.ANIMATION != null) {
			sender.sendMessage(ChatColor.RED + "Une animation est déjà en cours !");
		} else {
			try {
				Bukkit server     = Ladder.getInstance().getBukkitServer(args[1]);
				Bukkit fallback   = Ladder.getInstance().getBukkitServer(args[2]);
				int    timeBewMsg = Integer.parseInt(args[3]);
				int    time		  = Integer.parseInt(args[4]);
				String name		  = ChatColor.replaceColor(args[5]).replace("_", " ");

				new Animation(server, fallback, name, timeBewMsg, time).start();
			} catch(Exception e){
				sendHelp(sender);
			}
		}
	}

	public void stopEvent(CommandSender sender){
		if(Animation.ANIMATION == null){
			sender.sendMessage(ChatColor.RED + "Aucune animation n'est en cours !");
		} else {
			Animation.ANIMATION.close();
		}
	}
}
