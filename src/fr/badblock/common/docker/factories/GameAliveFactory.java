package fr.badblock.common.docker.factories;

import fr.badblock.common.docker.GameState;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter public class GameAliveFactory {
	private String 		name;
	private boolean 	isJoinable;
	private GameState   gameState;
	private int			players;
	private int			slots;
	
	public GameAliveFactory(String name, GameState gameState, boolean isJoinable, int players, int slots) {
		this.setName(name);
		this.setGameState(gameState);
		this.setJoinable(isJoinable);
		this.setPlayers(players);
		this.setSlots(slots);
	}
}
