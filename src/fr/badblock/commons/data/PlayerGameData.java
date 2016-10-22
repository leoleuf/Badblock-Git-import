package fr.badblock.commons.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.JsonObject;

import fr.badblock.commons.data.i18n.Locale;

public class PlayerGameData {
	public Locale								  		  locale	       = Locale.FRENCH_FRANCE;

	private int  				 						  badcoins     	   = 0;
	public  int  				 						  shopPoints       = 0;
	private int  				 						  level	     	   = 1;
	private long 										  xp		       = 0L;
	//TODO private List<PlayerBooster>					  		  boosters		   = new ArrayList<>();
	private Map<String, Integer> 						  kits 		 	   = new ConcurrentHashMap<>();
	private Map<String, String>							  lastUsedKits 	   = new ConcurrentHashMap<>();
	//TODO private Map<String, PlayerAchievementState> 		  achievements 	   = new ConcurrentHashMap<>();

	private Map<String, Map<String, Double>> 			  stats   	 	   = new ConcurrentHashMap<>();
}
