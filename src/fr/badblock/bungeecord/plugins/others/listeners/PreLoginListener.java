package fr.badblock.bungeecord.plugins.others.listeners;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import fr.badblock.bungeecord.plugins.ladder.LadderBungee;
import fr.badblock.bungeecord.plugins.others.BadBlockBungeeOthers;
import fr.badblock.bungeecord.plugins.others.Player;
import fr.badblock.bungeecord.plugins.others.database.BadblockDatabase;
import fr.badblock.bungeecord.plugins.others.database.Request;
import fr.badblock.bungeecord.plugins.others.database.Request.RequestType;
import fr.badblock.common.commons.technologies.rabbitmq.RabbitPacketType;
import fr.badblock.common.commons.utils.Encodage;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class PreLoginListener implements Listener {

	//public static List<String> players = new ArrayList<>();

	private static Gson gson = new Gson();

	@EventHandler (priority = EventPriority.HIGHEST)
	public void onPostLogin(ServerConnectEvent event) {
		ProxiedPlayer proxiedPlayer = event.getPlayer();
		Player.get(proxiedPlayer);
		/*if (event.getTarget() != null)
			if (event.getTarget().getName() != null)
				if (!players.contains(playerName))
					players.add(playerName);*/
		UserConnection userConnection = (UserConnection) proxiedPlayer;
		BadblockDatabase.getInstance().addSyncRequest(new Request("SELECT nick FROM nick WHERE playerName = '" + BadblockDatabase.getInstance().mysql_real_escape_string(proxiedPlayer.getName().toLowerCase()) + "'", RequestType.GETTER)
		{
			@Override
			public void done(ResultSet resultSet)
			{
				try
				{
					if (resultSet.next())
					{
						userConnection.getPendingConnection().getLoginRequest().setData(resultSet.getString("nick"));
					}
					else
					{
						userConnection.getPendingConnection().getLoginRequest().setData(proxiedPlayer.getName());
					}
				}
				catch(Exception error)
				{
					error.printStackTrace();
				}
			}
		});
		BadblockDatabase.getInstance().addRequest(new Request("UPDATE friends SET uuid = '" + proxiedPlayer.getUniqueId() + "' WHERE pseudo = '" + BadblockDatabase.getInstance().mysql_real_escape_string(proxiedPlayer.getName()) + "'", RequestType.SETTER));
		if (LadderBungee.getInstance().bungeePlayerList.size() >= BadBlockBungeeOthers.getInstance().getMaxPlayers()) {
			BadBlockBungeeOthers.getInstance().setDone(true);
			BadBlockBungeeOthers.getInstance().deleteDNS();
		}
		Map<String, Integer> map = new HashMap<>();
		map.put(proxiedPlayer.getName(), proxiedPlayer.getPing());
		BadBlockBungeeOthers.getInstance().getRabbitService().sendPacket("playerPing", gson.toJson(map), Encodage.UTF8, RabbitPacketType.PUBLISHER, 5000, false);
	}

	@EventHandler
	public void onLogin(LoginEvent event) {
		/*if (BadBlockBungeeOthers.getInstance().getDeleteTime() != -1 && BadBlockBungeeOthers.getInstance().getDelete() == -1 && BadBlockBungeeOthers.getInstance().getDeleteTime() < System.currentTimeMillis()) {
			event.setCancelled(true);
			event.setCancelReason("§f[§eBadyBot§f] §cERR.. ERRE.. ERREUR! Nous n'avons pas pu vous connecter à notre plateforme de divertiseement. §6Peut-être utilisez-vous une autre adresse IP que play.badblock.fr ou badblock.fr pour vous connecter ? §3Dans le cas échéant, nous vous invitons à patienter, le serveur n'est peut-être pas préparé à vous recevoir, il sera stimulé si nécessaire pour pouvoir réaccepter sur lui des joueurs. §aVous pouvez également nous avertir depuis TeamSpeak ou sur le forum pour que notre ami BadyBot se recompose et vous redonne accès à notre plateforme de jeux.");
		}*/
	}
}
