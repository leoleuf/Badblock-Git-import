package fr.badblock.api.utils.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class SoundUtils {
	public static void broadcastSound(Sound sound){
		for(Player player : Bukkit.getOnlinePlayers()){
			player.playSound(player.getLocation(), sound, 3.0f, 1.0f);
		}
	}
	
	public static void broadcastSound(Location loc, Sound sound){
		for(Entity e : loc.getWorld().getEntities()){
			if(e.getType() == EntityType.PLAYER){
				((Player) e).playSound(loc, sound, 3.0f, 1.0f);
			}
		}
	}
	
	public static void broadcastSound(Player p, Sound sound){
		for(Entity e : p.getNearbyEntities(20.0f, 20.0f, 20.0f)){
			if(e.getType() == EntityType.PLAYER){
				playSound((Player) e, sound);
			}
		}
	}
	
	public static void playSound(Player player, Sound sound){
		player.playSound(player.getLocation(), sound, 3.0f, 1.0f);
	}
}
