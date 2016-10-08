package fr.badblock.bungee.utils;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;

public class OtherHub {
	private String startsWith, endsWith, server18, otherHub;
	
	public boolean is(ProxiedPlayer player){
		if(!startsWith.isEmpty() && player.getServer().getInfo().getName().toLowerCase().startsWith(startsWith))
			return true;
		else if(!endsWith.isEmpty() && player.getServer().getInfo().getName().toLowerCase().endsWith(endsWith))
			return true;
		else return false;
	}
	public boolean is18(ProxiedPlayer player, ServerInfo server){
		if(server18 == null || server18.isEmpty()) return false;
		if(player.getPendingConnection().getVersion() < 47 && server.getName().equalsIgnoreCase(server18))
			return true;
		else return false;
	}
	public ServerInfo getServer(){
		return BungeeUtils.getServer(otherHub);
	}
	public OtherHub(Configuration config, String start){
		startsWith = config.getString(start + ".startsWith").toLowerCase();
		endsWith = config.getString(start + ".endsWith").toLowerCase();
		otherHub = config.getString(start + ".otherHub");
		server18 = config.getString(start + ".server18").toLowerCase();
	}
}
