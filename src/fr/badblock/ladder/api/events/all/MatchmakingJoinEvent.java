package fr.badblock.ladder.api.events.all;

import fr.badblock.ladder.api.entities.Player;
import fr.badblock.ladder.api.events.PlayerEvent;
import lombok.Getter;
import lombok.Setter;

public class MatchmakingJoinEvent extends PlayerEvent {
	@Getter@Setter private String server;
	
	public MatchmakingJoinEvent(Player player, String server) {
		super(player);
	
		this.server = server;
	}
}
