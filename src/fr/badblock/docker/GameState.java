package fr.badblock.docker;

import lombok.Getter;

/**
 * Cette classe repr�sente les diff�rents statuts de la partie
 * 
 * @author LeLanN
 */
@Getter
public enum GameState {
	WAITING(1), RUNNING(2), FINISHED(3), STOPPING(4);

	public static GameState getStatus(int id) {
		for (final GameState status : values()) {
			if (status.getId() == id)
				return status;
		}

		return null;
	}

	private final int id;

	private GameState(int id) {
		this.id = id;
	}
}