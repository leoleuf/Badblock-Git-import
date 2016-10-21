package fr.badblock.gameapi.events.api;

import org.bukkit.event.HandlerList;

import fr.badblock.gameapi.events.abstracts.BadblockPlayerEvent;
import fr.badblock.gameapi.players.BadblockPlayer;

/**
 * Event appel� lorsque les donn�es du joueur sont re�ues de Ladder. Ne pas
 * utiliser ces donn�es au join !
 * 
 * @author LeLanN
 */
public class PlayerLoadedEvent extends BadblockPlayerEvent {
	private static final HandlerList handlers = new HandlerList();

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public PlayerLoadedEvent(BadblockPlayer player) {
		super(player);
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}