package fr.badblock.ladder.api.events.all;

import fr.badblock.ladder.api.entities.Player;
import fr.badblock.ladder.api.events.Cancellable;
import fr.badblock.ladder.api.events.PlayerEvent;
import lombok.Getter;
import lombok.Setter;

public class CommandEvent extends PlayerEvent implements Cancellable {
	@Getter@Setter private boolean cancelled;
	@Getter@Setter private String message;
	
	public CommandEvent(Player player, String message) {
		super(player);
		this.cancelled = false;
		this.message = message;
	}
}
