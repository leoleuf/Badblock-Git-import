package fr.badblock.bungeecord.plugins.others.listeners;

import java.util.ArrayList;
import java.util.List;

import fr.badblock.bungeecord.plugins.others.BadBlockBungeeOthers;
import fr.badblock.bungeecord.plugins.others.Player;
import fr.badblock.bungeecord.plugins.others.database.BadblockDatabase;
import fr.badblock.bungeecord.plugins.others.database.Request;
import fr.badblock.bungeecord.plugins.others.database.Request.RequestType;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class PreLoginListener implements Listener {

	public static List<String> players = new ArrayList<>();

	@EventHandler (priority = EventPriority.HIGHEST)
	public void onPostLogin(ServerConnectEvent event) {
		ProxiedPlayer proxiedPlayer = event.getPlayer();
		Player.get(proxiedPlayer);
		String playerName = proxiedPlayer.getName().toLowerCase();
		if (event.getTarget() != null)
			if (event.getTarget().getName() != null)
				if (!players.contains(playerName))
					players.add(playerName);
		BadblockDatabase.getInstance().addRequest(new Request("UPDATE friends SET uuid = '" + proxiedPlayer.getUniqueId() + "' WHERE pseudo = '" + proxiedPlayer.getName() + "'", RequestType.SETTER));
		if (players.size() >= 500) {
			BadBlockBungeeOthers.getInstance().setDone(true);
		}
	}
	
	@EventHandler
	public void onLogin(LoginEvent event) {
		/*if (BadBlockBungeeOthers.getInstance().getDeleteTime() != -1 && BadBlockBungeeOthers.getInstance().getDelete() == -1 && BadBlockBungeeOthers.getInstance().getDeleteTime() < System.currentTimeMillis()) {
			event.setCancelled(true);
			event.setCancelReason("§f[§eBadyBot§f] §cERR.. ERRE.. ERREUR! Nous n'avons pas pu vous connecter à notre plateforme de divertiseement. §6Peut-être utilisez-vous une autre adresse IP que play.badblock.fr ou badblock.fr pour vous connecter ? §3Dans le cas échéant, nous vous invitons à patienter, le serveur n'est peut-être pas préparé à vous recevoir, il sera stimulé si nécessaire pour pouvoir réaccepter sur lui des joueurs. §aVous pouvez également nous avertir depuis TeamSpeak ou sur le forum pour que notre ami BadyBot se recompose et vous redonne accès à notre plateforme de jeux.");
		}*/
	}
	
	//public static long getNotLoggedPlayers() {
		//return BungeeCord.getInstance().getPlayers().parallelStream().filter(player -> player.getServer() != null && player.getServer().getInfo() != null && !player.getServer().getInfo().getName().startsWith("login")).count();
//	}

}
