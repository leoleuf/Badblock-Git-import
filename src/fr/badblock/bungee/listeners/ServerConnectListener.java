package fr.badblock.bungee.listeners;

import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.data.ip.BadIpData;
import fr.badblock.bungee.data.players.BadPlayer;
import fr.badblock.bungee.utils.RedisUtils;
import fr.badblock.commons.utils.Callback;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerConnectListener implements Listener {

	@EventHandler
	public void onServerConnect(ServerConnectEvent event) {
		String playerName = event.getPlayer().getName().toLowerCase();
		// Get player data and save it
		BadBungee.getInstance().getRedisPlayerDataService().getSyncObject(RedisUtils.PLAYERDATA_PATTERN + playerName, BadBungee.playerType, new Callback<BadPlayer>() {
			@Override
			public void done(BadPlayer result, Throwable error) {
				if (result == null) return;
				BadPlayer currentPlayer = BadBungee.getInstance().get(result.getName());
				if (currentPlayer == null) return;
				currentPlayer.updateDataFromClone(result);
				if (event.getTarget() != null) currentPlayer.setBukkitServer(event.getTarget().getName());
				currentPlayer.updateData();
				BadBungee.getInstance().getRedisPlayerDataService().set(RedisUtils.PLAYERDATA_PATTERN + playerName, currentPlayer.getData().toString());
			}
		}, true);
		// Get ip data and save it
		BadBungee.getInstance().getRedisPlayerDataService().getSyncObject(RedisUtils.IPDATA_PATTERN + event.getPlayer().getAddress().getAddress().getHostAddress(), BadBungee.ipType, new Callback<BadIpData>() {
			@Override
			public void done(BadIpData result, Throwable error) {
				if (result == null) return;
				System.out.println(result.getIp());
				BadIpData currentIpData = BadBungee.getInstance().getIp(result.getIp());
				if (currentIpData == null) return;
				currentIpData.updateDataFromClone(result);
				currentIpData.updateData();
				BadBungee.getInstance().getRedisPlayerDataService().set(RedisUtils.IPDATA_PATTERN + event.getPlayer().getAddress().getAddress().getHostAddress(), currentIpData.getData().toString());
			}
		}, true);
	}

}
