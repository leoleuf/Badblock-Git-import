package fr.badblock.bungee.listeners;

import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.data.ip.BadIpData;
import fr.badblock.bungee.data.players.BadPlayer;
import fr.badblock.bungee.utils.RedisUtils;
import fr.badblock.commons.utils.Callback;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class DisconnectListener implements Listener {

	@EventHandler
	public void onPlayerDisconnect(PlayerDisconnectEvent event) {
		String playerName = event.getPlayer().getName().toLowerCase();
		BadPlayer.players.remove(playerName);
		BadBungee.getInstance().keepAlive();
		// Get player data and save it
		BadBungee.getInstance().getRedisService().getSyncObject(RedisUtils.PLAYERDATA_PATTERN + playerName, BadBungee.playerType, new Callback<BadPlayer>() {
			@Override
			public void done(BadPlayer result, Throwable error) {
				if (result == null) return;
				BadPlayer currentPlayer = BadBungee.getInstance().get(result.getName());
				if (currentPlayer == null) return;
				currentPlayer.updateDataFromClone(result);
				currentPlayer.setBukkitServer(null);
				currentPlayer.setBungee(null);
				currentPlayer.updateData();
				BadBungee.getInstance().getRedisService().delete(RedisUtils.PLAYERDATA_PATTERN + playerName);
			}
		});
		// Get IP data and save it
		BadBungee.getInstance().getRedisService().getSyncObject(RedisUtils.IPDATA_PATTERN + event.getPlayer().getAddress().getAddress().getHostAddress(), BadBungee.ipType, new Callback<BadIpData>() {
			@Override
			public void done(BadIpData result, Throwable error) {
				if (result == null) return;
				BadIpData currentIpData = BadBungee.getInstance().getIp(result.getIp());
				if (currentIpData == null) return;
				currentIpData.updateDataFromClone(result);
				currentIpData.updateData();
				BadBungee.getInstance().getRedisService().delete(RedisUtils.IPDATA_PATTERN + event.getPlayer().getAddress().getAddress().getHostAddress());
			}
		});
	}
	
}
