package fr.badblock.gameapi.events.api;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import fr.badblock.gameapi.players.BadblockPlayer;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Event appel� lorsque les donn�es du joueur sont re�ues de Ladder. Ne pas
 * utiliser ces donn�es au join !
 * 
 * @author LeLanN
 */
@AllArgsConstructor
public class PlayerLoadedEvent extends Event {
	private static final HandlerList handlers = new HandlerList();

	@Getter
	private BadblockPlayer player;

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}