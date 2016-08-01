package fr.badblock.ladder.api.events;

import fr.badblock.ladder.api.entities.Player;
import lombok.Getter;

public abstract class PlayerEvent extends Event {
	@Getter protected Player player;
	
	public PlayerEvent(Player player){
		this.player = player;
	}
}
