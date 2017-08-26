package fr.badblock.bungeecord.plugins.others.listeners;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.mrpowergamerbr.temmiewebhook.DiscordEmbed;
import com.mrpowergamerbr.temmiewebhook.DiscordMessage;
import com.mrpowergamerbr.temmiewebhook.embed.ThumbnailEmbed;

import fr.badblock.bungeecord.plugins.others.BadBlockBungeeOthers;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class ChatListener implements Listener {

	private static List<String> cheatWords = Arrays.asList(new String[] {
			"cheat",
			"sheat",
			"ceat",
			"triche",
			"hack",
			"trice"
	});

	private static List<String> lagWords = Arrays.asList(new String[] {
			"lag",
			"sa bug",
			"ça bug",
			"ca bug"
	});

	private static Map<String, Long> lagging = new HashMap<>();
	private static long				 laggingExpire = 300_000L;

	@SuppressWarnings("deprecation")
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onChat(ChatEvent event) {
		if (!(event.getSender() instanceof ProxiedPlayer)) return;
		ProxiedPlayer player = (ProxiedPlayer) event.getSender();
		String message = event.getMessage();
		boolean cheat = contains(message, cheatWords);
		boolean lag = contains(message, lagWords);
		if (cheat) {
			event.setCancelled(true);
			player.sendMessage("§6[INFO] §fVous soupçonnez quelqu'un de triche ? Tapez /cheat <pseudo>");
		}else if (lag) {
			String playerName = player.getName().toLowerCase();
			long timestamp = System.currentTimeMillis();
			player.sendMessage("§6[INFO] §fVous pensez que le serveur lag ?");
			if (!lagging.containsKey(playerName) || lagging.get(playerName) < timestamp) {
				player.sendMessage("§6[INFO] §aUn rapport vient d'être enregistré suite à votre soupçon.");
				player.sendMessage("§6[INFO] §fIl sera étudié prochainement.");
				long expire = timestamp + laggingExpire;
				lagging.put(playerName, expire);
				DiscordEmbed de = new DiscordEmbed(player.getServer().getInfo().getName(), "Signalé par " + player.getName() + " | Message: " + event.getMessage());
				ThumbnailEmbed te = new ThumbnailEmbed();
				te.setUrl("http://forum.curvefever.com/sites/default/files/ideas14Mar/LagIcon.jpg");
				te.setHeight(96);
				te.setWidth(96);
				de.setThumbnail(te);
				DiscordMessage dm = new DiscordMessage("Lag Report", "", "http://forum.curvefever.com/sites/default/files/ideas14Mar/LagIcon.jpg");
				dm.getEmbeds().add(de);
				BadBlockBungeeOthers.getInstance().getTemmie().sendMessage(dm);
			}else{
				player.sendMessage("§6[INFO] §cVous avez déjà envoyé un rapport récemment.");
			}
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
