package fr.badblock.gameapi.events.api;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import fr.badblock.gameapi.events.abstracts.BadblockPlayerEvent;
import fr.badblock.gameapi.game.GameServer;
import fr.badblock.gameapi.game.GameServer.WhileRunningConnectionTypes;
import fr.badblock.gameapi.players.BadblockPlayer;
import lombok.Getter;
import lombok.Setter;

/**
 * Si un joueur d�connecte en pleine partie et que le type de gestion de
 * connection est correctement d�finit
 * [{@link GameServer#whileRunningConnection(WhileRunningConnectionTypes)} cet
 * event sera appel� pour v�rifier que le joueur y ai bien autoris�
 * 
 * @author LeLanN
 */
@Getter
@Setter
public class PlayerReconnectionPropositionEvent extends BadblockPlayerEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();

	public static HandlerList getHandlerList() {
		return handlers;
	}	
	
	private boolean isCancelled = false;
	
	public PlayerReconnectionPropositionEvent(BadblockPlayer player) {
		this(player, false);
	}
	
	public PlayerReconnectionPropositionEvent(BadblockPlayer player, boolean isCancelled) {
		super(player);
		this.isCancelled = isCancelled;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
