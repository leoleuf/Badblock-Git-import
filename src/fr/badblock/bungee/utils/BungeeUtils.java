package fr.badblock.bungee.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fr.badblock.bungee.Bungee;

public class BungeeUtils {
	
	public static Map<String, Bungee> bungees = new HashMap<>();
	
	public static List<Bungee> getAvailableBungees() {
		return bungees.values().stream().filter(bungee -> bungee.isAvailable()).collect(Collectors.toList());
	}
	
}
