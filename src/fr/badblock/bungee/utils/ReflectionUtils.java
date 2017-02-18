package fr.badblock.bungee.utils;

import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

import com.google.gson.reflect.TypeToken;

import fr.badblock.bungee.bobjects.Bungee;
import fr.badblock.bungee.data.ip.BadIpData;
import fr.badblock.bungee.data.players.BadPlayer;

public class ReflectionUtils {

	// Reflection data
	public static final Type bungeeType 		= new TypeToken<Bungee>() {}.getType();
	public static final Type playerType 		= new TypeToken<BadPlayer>() {}.getType();
	public static final Type ipType 			= new TypeToken<BadIpData>() {}.getType();
	public static final Type stringListType 	= new TypeToken<List<String>>() {}.getType();
	public static final Type uuidListType 		= new TypeToken<List<UUID>>() {}.getType();
	
}
