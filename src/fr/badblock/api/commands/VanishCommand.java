package fr.badblock.api.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class VanishCommand extends AbstractCommand implements Listener{
	private static List<UUID> vanishedPlayers = new ArrayList<UUID>();

	public static boolean isVanished(Entity e){
		if(e instanceof Player)
			return vanishedPlayers.contains(e.getUniqueId());
		else return false;
	}

	public static void setVanished(Player p, boolean vanished){
		if(vanishedPlayers.contains(p.getUniqueId()) && !vanished){
			vanishedPlayers.remove(p.getUniqueId());
			for(Player player : Bukkit.getOnlinePlayers()){
				if(!player.canSee(p))
					player.showPlayer(p);
			}
		} else if(!vanishedPlayers.contains(p.getUniqueId()) && vanished){
			vanishedPlayers.add(p.getUniqueId());
			for(Player player : Bukkit.getOnlinePlayers()){
				if(!player.canSee(p))
					player.showPlayer(p);
				if(!new VanishCommand().can(player) && !p.getUniqueId().equals(player.getUniqueId()))
					player.hidePlayer(p);
			}
		}
	}

	@EventHandler
	public void onConnect(PlayerJoinEvent e){
		if(!can(e.getPlayer()))
			for(UUID uniqueId : vanishedPlayers){
				Player concerned = Bukkit.getPlayer(uniqueId);
				if(concerned != null)
					e.getPlayer().hidePlayer(concerned);
			}
	}
	@EventHandler
	public void onDisconnect(PlayerQuitEvent e){
		setVanished(e.getPlayer(), false);
	}

	public VanishCommand() {
		super("minigames.moderator", "%gold%Utilisation : /vanish (<player>)", true);
	}

	@Override
	public void run(CommandSender sender, String[] args) {
		Player concerned = null;
		if(args.length == 0 && !(sender instanceof Player)){
			sendHelp(sender);
		} else if(args.length > 0){
			concerned = Bukkit.getPlayer(args[0]);
		} else {
			concerned = (Player) sender;
		}

		if(concerned == null){
			sendMessage(sender, "%red%Le joueur " + args[0] + " est introuvable.");
		} else {
			if(!isVanished(concerned)){
				setVanished(concerned, true);

				sendMessage(concerned, "%gold%Vous êtes maintenant invisible.");
				if(args.length > 0){
					sendMessage(sender, "%gold%" + concerned.getName() + " est maintenant invisible%gold%.");
				}
			} else {
				setVanished(concerned, false);

				sendMessage(concerned, "%gold%Vous êtes maintenant visible.");
				if(args.length > 0){
					sendMessage(sender, "%gold%" + concerned.getName() + " est maintenant visible%gold%.");
				}
			}
		}
	}
}