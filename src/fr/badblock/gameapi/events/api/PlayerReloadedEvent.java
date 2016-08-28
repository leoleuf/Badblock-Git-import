package fr.badblock.gameapi.events.api;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import fr.badblock.gameapi.players.BadblockPlayer;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Event appel� lorsque les donn�es du joueur sont � nouveau re�ues de Ladder.
 * 
 * @author LeLanN
 */
@AllArgsConstructor
public class PlayerReloadedEvent extends Event {
	private static final HandlerList handlers = new HandlerList();

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Getter
	private BadblockPlayer player;

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}