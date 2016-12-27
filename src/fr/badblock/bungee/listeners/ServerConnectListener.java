package fr.badblock.bungee.listeners;

import fr.badblock.bungee.BadBungee;
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
		BadBungee.getInstance().getRedisService().getSyncObject(RedisUtils.PLAYERDATA_PATTERN + playerName, BadBungee.playerType, new Callback<BadPlayer>() {
			@Override
			public void done(BadPlayer result, Throwable error) {
				if (result == null) return;
				BadPlayer currentPlayer = BadBungee.getInstance().get(result.getName());
				if (currentPlayer == null) return;
				currentPlayer.updateDataFromClone(result);
				if (event.getTarget() != null) currentPlayer.setBukkitServer(event.getTarget().getName());
				currentPlayer.updateData();
				BadBungee.getInstance().getRedisService().set(RedisUtils.PLAYERDATA_PATTERN + playerName, currentPlayer);
			}
		});
	}

}
