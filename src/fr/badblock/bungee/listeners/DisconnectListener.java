package fr.badblock.bungee.listeners;

import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.data.players.BadPlayer;
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
		BadBungee.getInstance().getRedisService().getSyncObject("badblock.player." + playerName, BadBungee.playerType, new Callback<BadPlayer>() {
			@Override
			public void done(BadPlayer result, Throwable error) {
				if (result == null) return;
				result.updateData();
			}
		});
	}
	
}
