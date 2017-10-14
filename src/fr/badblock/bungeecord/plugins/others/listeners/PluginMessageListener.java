package fr.badblock.bungeecord.plugins.others.listeners;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PluginMessageListener implements Listener {

	 @EventHandler
	    public void onPluginMessage(PluginMessageEvent e) {
	        if (e.getTag().equalsIgnoreCase("BungeeCord")) {
	            DataInputStream in = new DataInputStream(new ByteArrayInputStream(e.getData()));
	            try {
	                String channel = in.readUTF(); // channel we delivered
	                if (channel.equals("Auth")){
	                    String name = in.readUTF(); // the inputstring
	                    ProxiedPlayer player = BungeeCord.getInstance().getPlayer(name);
	                    if (player != null)
	                    {
	                    	UserConnection userConnection = (UserConnection) player;
	                    	userConnection.setLogged(true);
	                    }
	                }
	            } catch (IOException e1) {
	                e1.printStackTrace();
	            }
	      
	        }
	    }
	
}