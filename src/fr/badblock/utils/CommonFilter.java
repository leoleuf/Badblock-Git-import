package fr.badblock.utils;

public class CommonFilter {

	public static String filterNames(String playerName) {
		return (playerName.length() >= 16 ? playerName.replace("Erisium_", "BadBloc_") : playerName.replace("Erisium_", "BadBlock_"));
	}
	
}
