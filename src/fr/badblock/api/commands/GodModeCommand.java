package fr.badblock.api.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GodModeCommand extends AbstractCommand implements Listener{
	private static List<UUID> godModedPlayers = new ArrayList<UUID>();
	
	public static boolean isGodModed(Entity e){
		if(e instanceof Player)
			return godModedPlayers.contains(((Player)e).getUniqueId());
		else return false;
	}
	public static void setGodModed(Player p, boolean godModed){
		if(godModedPlayers.contains(p.getUniqueId()) && !godModed)
			godModedPlayers.remove(p.getUniqueId());
		else if(!godModedPlayers.contains(p.getUniqueId()) && godModed){
			godModedPlayers.add(p.getUniqueId());
		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onDamage(EntityDamageEvent e){
		if(isGodModed(e.getEntity())){
			e.setDamage(0d);
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void foodLevelChangeEvent(FoodLevelChangeEvent e){
		if(isGodModed(e.getEntity())){
			e.setFoodLevel(20);
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void onDisconnect(PlayerQuitEvent e){
		setGodModed(e.getPlayer(), false);
	}
	
	public GodModeCommand() {
		super("minigames.moderator", "%gold%Utilisation : /godmode (<player>)", true);
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
			if(!isGodModed(concerned)){
				HealCommand.heal(concerned);
				
				sendMessage(concerned, "%gold%Vous êtes maintenant en mode %red%Dieu%gold%.");
				if(args.length > 0){
					sendMessage(sender, "%gold%" + concerned.getName() + " est maintenant en mode Dieu%gold%.");
				}
				setGodModed(concerned, true);
			} else {
				setGodModed(concerned, false);

				sendMessage(concerned, "%gold%Vous n'êtes plus en mode %red%Dieu%gold%.");
				if(args.length > 0){
					sendMessage(sender, "%gold%" + concerned.getName() + " n'est plus en mode Dieu%gold%.");
				}
			}
		}
	}
}
