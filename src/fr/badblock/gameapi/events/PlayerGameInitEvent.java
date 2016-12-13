package fr.badblock.gameapi.events;

import org.bukkit.event.HandlerList;

import fr.badblock.gameapi.events.abstracts.BadblockPlayerEvent;
import fr.badblock.gameapi.players.BadblockPlayer;

/**
 * Appel�e lorsque qu'un joueur doit �tre initialis� dans une partie en plein cours de la partie
 * 
 * @author LeLanN
 */
public class PlayerGameInitEvent extends BadblockPlayerEvent {
	private static final HandlerList handlers = new HandlerList();

	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	public PlayerGameInitEvent(BadblockPlayer player) {
		super(player);
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
}
