package fr.badblock.api.utils.bukkit;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

public class ChatUtils {
	public static List<String> colorReplace(List<String> input){
		List<String> result = new ArrayList<>();
		
		for(String str : input)
			result.add(colorReplace(str));
		
		return result;
	}
	
	public static String colorReplace(String input){
		String output = null;
		output = input.replace("%black%", ChatColor.BLACK.toString());
		output = output.replace("%dblue%", ChatColor.DARK_BLUE.toString());
		output = output.replace("%dgreen%", ChatColor.DARK_GREEN.toString());
		output = output.replace("%darkaqua%", ChatColor.DARK_AQUA.toString());
		output = output.replace("%dred%", ChatColor.DARK_RED.toString());
		output = output.replace("%darkred%", ChatColor.DARK_RED.toString());
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

		output = ChatColor.translateAlternateColorCodes('&', output);
		return output;
	}
	public static String colorDelete(String input){
		String output = colorReplace(input);
		output = ChatColor.stripColor(output);
		return output;
	}
	
	/* Toute les possibilites d'envois de message */

	public static void broadcast(String message){
		Bukkit.broadcastMessage(colorReplace(message));
	}
	/* ARRAY */
	public static void sendMessagesPlayer(Player player, String... message){
		for(int i=0;i<message.length;i++)
			if(message[i] != null && player != null)
				player.sendMessage(colorReplace(message[i]));
	}
	
	public static void sendMessagePlayer(Player player, String... message){
		for(int i=0;i<message.length;i++)
			if(message[i] != null && player != null)
				player.sendMessage(colorReplace(message[i]));
	}
	public static void sendMessagePlayer(CommandSender player, String message[]){
		for(int i=0;i<message.length;i++)
			if(message[i] != null && player != null)
				player.sendMessage(colorReplace(message[i]));
	}
	public static void sendMessagePlayer(HumanEntity player, String message[]){
		for(int i=0;i<message.length;i++)
			if(message[i] != null && player != null)
				((CommandSender) player).sendMessage(message[i]);
	}

	/* SIMPLE */
	public static void sendMessagePlayer(Player player, String message){
		if(player == null || message == null) return;
		sendMessagePlayer(player, message.split(";"));
	}
	public static void sendMessagePlayer(CommandSender player, String message){
		if(player == null || message == null) return;
		sendMessagePlayer(player, message.split(";"));
	}
	public static void sendMessagePlayer(HumanEntity player, String message) {
		if(player == null || message == null) return;
		sendMessagePlayer(((CommandSender) player), message.split(";"));
	}
}
