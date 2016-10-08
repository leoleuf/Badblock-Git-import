package com.lelann.bungee.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ChatUtils {
	public static String colorReplace(String input) {
        String output = null;
        output = input.replace("%black%", ChatColor.BLACK.toString());
        output = output.replace("%dblue%", ChatColor.DARK_BLUE.toString());
        output = output.replace("%dgreen%", ChatColor.DARK_GREEN.toString());
        output = output.replace("%darkaqua%", ChatColor.DARK_AQUA.toString());
        output = output.replace("%dred%", ChatColor.DARK_RED.toString());
        output = output.replace("%dpurple%", ChatColor.DARK_PURPLE.toString());
        output = output.replace("%gold%", ChatColor.GOLD.toString());
        output = output.replace("%gray%", ChatColor.GRAY.toString());
        output = output.replace("%dgray%", ChatColor.DARK_GRAY.toString());
        output = output.replace("%blue%", ChatColor.BLUE.toString());
        output = output.replace("%green%", ChatColor.GREEN.toString());
        output = output.replace("%aqua%", ChatColor.AQUA.toString());
        output = output.replace("%red%", ChatColor.RED.toString());
        output = output.replace("%lpurple%", ChatColor.LIGHT_PURPLE.toString());
        output = output.replace("%yellow%", ChatColor.YELLOW.toString());
        output = output.replace("%white%", ChatColor.WHITE.toString());
        output = output.replace("%bold%", ChatColor.BOLD.toString());
        output = output.replace("%italic%", ChatColor.ITALIC.toString());
        output = output.replace("%magic%", ChatColor.MAGIC.toString());
        output = output.replace("%default%", ChatColor.RESET.toString());

        return ChatColor.translateAlternateColorCodes('&', output);
    }

	public static String colorDelete(String input){
		return ChatColor.stripColor(colorReplace(input));
	}
	/** Toute les possibilitées d'envois de message **/
		public static void broadcast(String[] message){
			for(final String msg : message){
				ProxyServer.getInstance().broadcast(colorReplace(msg));
			}
		}
		public static void broadcast(String message){
			broadcast(message.split(";"));
		}
		public static void sendMessagePlayer(ProxiedPlayer player, String... message){
			sendMessagePlayer((CommandSender) player, message);
		}
		public static void sendMessagePlayer(CommandSender player, String... message){
			for(int i=0;i<message.length;i++)
				player.sendMessage(colorReplace(message[i]));
	    }
		public static void sendMessagePlayer(Connection p, String... message){
			if(p instanceof ProxiedPlayer)
				sendMessagePlayer((CommandSender) p, message);
	    }
		public static void sendMessagePlayer(ProxiedPlayer player, String message){
			sendMessagePlayer(player, message.split(";"));
		}
		public static void sendMessagePlayer(Connection player, String message){
			sendMessagePlayer(player, message.split(";"));
		}
		public static void sendMessagePlayer(CommandSender player, String message){
			sendMessagePlayer(player, message.split(";"));
		}
	/** Fin */
}
