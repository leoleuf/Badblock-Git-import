package fr.badblock.api.utils.bukkit;

import org.bukkit.entity.Player;

public class PlayerUtils {
	public static boolean isValid(Player p){
		return p != null && p.isOnline() && p.isValid();
	}
}
