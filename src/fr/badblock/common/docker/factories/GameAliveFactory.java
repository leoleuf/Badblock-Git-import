package fr.badblock.common.docker.factories;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter public class GameAliveFactory {
	private String 		name;
	private boolean 	isJoinable;
	private int			players;
	private int			slots;
	
	public GameAliveFactory(String name, boolean isJoinable, int players, int slots) {
		this.setName(name);
		this.setJoinable(isJoinable);
		this.setPlayers(players);
		this.setSlots(slots);
	}
}
