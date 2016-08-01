package fr.badblock.ladder.api.events.all;

import fr.badblock.ladder.api.entities.BungeeCord;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.ladder.api.events.Cancellable;
import fr.badblock.ladder.api.events.PlayerEvent;
import lombok.Getter;
import lombok.Setter;

public class PlayerJoinEvent extends PlayerEvent implements Cancellable {
	@Getter private BungeeCord bungeeCord;
	@Getter@Setter private boolean cancelled;
	@Getter@Setter private String cancelReason;
	
	public PlayerJoinEvent(Player player, BungeeCord bungeecordServer) {
		super(player);
		this.bungeeCord = bungeecordServer;
		this.cancelled = false;
		this.cancelReason = "";
	}
}
