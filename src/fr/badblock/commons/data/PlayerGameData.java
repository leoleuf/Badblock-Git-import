package fr.badblock.commons.data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import fr.badblock.commons.data.i18n.Locale;

public class PlayerGameData {
	public Locale								  		  locale	       = Locale.FRENCH_FRANCE;

	public int  				 						  badcoins     	   = 0;
	public int  				 						  shopPoints       = 0;
	public int  				 						  level	     	   = 1;
	public long 										  xp		       = 0L;
	//TODO private List<PlayerBooster>					  		  boosters		   = new ArrayList<>();
	public Map<String, Integer> 						  kits 		 	   = new ConcurrentHashMap<>();
	public Map<String, String>							  lastUsedKits 	   = new ConcurrentHashMap<>();
	//TODO private Map<String, PlayerAchievementState> 		  achievements 	   = new ConcurrentHashMap<>();

	public Map<String, Map<String, Double>> 			  stats   	 	   = new ConcurrentHashMap<>();
}
