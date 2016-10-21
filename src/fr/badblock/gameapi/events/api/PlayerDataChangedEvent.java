package fr.badblock.gameapi.events.api;

import org.bukkit.event.HandlerList;

import fr.badblock.gameapi.events.abstracts.BadblockPlayerEvent;
import fr.badblock.gameapi.players.BadblockPlayer;

/**
 * Event appel� lorsque des donn�es du joueur ont �t� modifi�es
 * 
 * @author LeLanN
 */
public class PlayerDataChangedEvent extends BadblockPlayerEvent {
	private static final HandlerList handlers = new HandlerList();

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public PlayerDataChangedEvent(BadblockPlayer player) {
		super(player);
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}