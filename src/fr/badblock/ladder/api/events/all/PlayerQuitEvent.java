package fr.badblock.ladder.api.events.all;

import fr.badblock.ladder.api.entities.Player;
import fr.badblock.ladder.api.events.PlayerEvent;

public class PlayerQuitEvent extends PlayerEvent {
	public PlayerQuitEvent(Player player) {
		super(player);
	}
}
