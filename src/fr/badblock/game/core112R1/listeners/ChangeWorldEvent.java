package fr.badblock.game.core112R1.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import fr.badblock.game.core112R1.players.GameBadblockPlayer;
import fr.badblock.gameapi.BadListener;

public class ChangeWorldEvent extends BadListener {
	@EventHandler
	public void onTeleport(PlayerChangedWorldEvent e){
		GameBadblockPlayer player = (GameBadblockPlayer) e.getPlayer();
		player.setCustomEnvironment(null);
	}
}
