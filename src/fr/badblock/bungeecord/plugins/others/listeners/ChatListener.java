package fr.badblock.bungeecord.plugins.others.listeners;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import fr.badblock.utils.Flags;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class ChatListener implements Listener {

	private static List<String> cheatWords = Arrays
			.asList(new String[] { "cheat", "sheat", "ceat", "triche", "hack", "trice" });

	private static List<String> lagWords = Arrays.asList(new String[] { "lag", "sa bug", "ça bug", "ca bug" });

	private static List<String> modoWords = Arrays.asList(new String[] { "staff", "modo", "admin", "help", "aide" });

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onChat(ChatEvent event) {
		if (!(event.getSender() instanceof ProxiedPlayer))
			return;
		ProxiedPlayer player = (ProxiedPlayer) event.getSender();
		// Do stuff
		String message = event.getMessage();
		if (message.startsWith("/"))
			return;
		boolean cheat = contains(message, cheatWords);
		boolean lag = contains(message, lagWords);
		boolean modo = contains(message, modoWords);
		if (cheat) {
			player.sendMessage("§6[INFO] §fVous soupçonnez quelqu'un de triche ? Tapez /cheat <pseudo>");
			player.sendMessage("§6[INFO] §fNe déversez pas votre haine dans le t'chat.");
		} else if (lag) {
			String playerName = player.getName().toLowerCase();
			String flagName = "lagmsg_" + playerName;
			if (!Flags.has(flagName)) {
				Flags.set(flagName, 60_000);
				player.sendMessage("§6[INFO] §fVous pensez que le serveur lag ?");
				player.sendMessage("§6[INFO] §bFaites-le nous savoir en tapant /reportlag !");
				player.sendMessage("§6[INFO] §fSignaler les lags permettent de mieux les trouver & de les corriger.");
			}
		} else if (modo) {
			player.sendMessage("§6[INFO] §fVous avez besoin d'aide/d'un staff ? Tapez /modo");
		}
	}

	private static boolean contains(String message, List<String> list) {
		message = message.toLowerCase();
		String finalMessage = message;
		List<String> l = list.parallelStream().map(i -> i.toLowerCase()).collect(Collectors.toList());
		long count = l.stream().filter(string -> finalMessage.contains(string)).count();
		return count != 0;
	}

}
