package fr.badblock.commons.utils;

public class CommonFilter {

	public static String filterNames(String playerName) {
		return (playerName.length() >= 16 ? playerName.replace("Erisium_", "BadBloc_") : playerName.replace("Erisium_", "BadBlock_"));
	}

	public static String reverseFilterNames(String playerName) {
		return playerName.replace("BadBlock_", "Erisium_").replace("BadBloc_", "Erisium");
	}
	
}
