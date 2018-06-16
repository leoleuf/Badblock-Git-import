package fr.badblock.bungeecord.plugins.others.tasks;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;

public class FallbackTask extends TimerTask {

	private Map<String, Long> timestamps = new HashMap<>();

	public FallbackTask() {
		new Timer().schedule(this, 1000, 1000);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		for (ProxiedPlayer player : BungeeCord.getInstance().getPlayers()) {
			Server server = player.getServer();
			if (server == null)
				continue;
			if (server.getInfo() == null)
				continue;
			if (!server.getInfo().getName().equals("fallback"))
				continue;
			UserConnection userConnection = (UserConnection) player;
			if (userConnection.getLastServerName() != null) {
				ServerInfo serverInfo = BungeeCord.getInstance().getServerInfo(userConnection.getLastServerName());
				if (serverInfo == null) {
					String serverName = userConnection.isLogged() ? "lobby" : "skeleton";
					serverInfo = BungeeCord.getInstance().getServerInfo(serverName);
					long timestamp = System.currentTimeMillis();
					if (!timestamps.containsKey(player.getName()) || timestamps.get(player.getName()) < timestamp) {
						if (serverName.equals("lobby")) {
							player.sendMessages(
									"§b> §cLe serveur où vous avez tenté de vous connecter ou celui où vous étiez n'existe plus.",
									"§b> §cCelui-ci a sûrement dû être coupé automatiquement.",
									"§b> §aVous allez être retéléporté dans un hub.",
									"§b> §eVeuillez patienter, le temps de trouver un serveur.");
						} else {
							player.sendMessages("§b> §cL'instance de connexion a plantée.",
									"§b> §cCell-ci a sûrement dû être coupé automatiquement.",
									"§b> §aVous allez être retéléporté sur une autre instance.",
									"§b> §eVeuillez patienter, le temps de trouver un serveur.");
						}
						long expire = timestamp + 30_000L;
						timestamps.put(player.getName(), expire);
					}
					userConnection.connect(serverInfo, null, true, false);
				} else {
					long timestamp = System.currentTimeMillis();
					if (!timestamps.containsKey(player.getName()) || timestamps.get(player.getName()) < timestamp) {

						player.sendMessages(
								"§b> §cLe serveur où vous avez tenté de vous connecter ou celui où vous étiez a planté.",
								"§b> §aVous allez être retéléporté dessus une fois démarré.",
								"§b> §eVeuillez patienter quelques secondes voire une minute.");
						long expire = timestamp + 30_000L;
						timestamps.put(player.getName(), expire);
					}
					userConnection.connect(serverInfo, null, true, false);
				}
			}
		}
	}

}
