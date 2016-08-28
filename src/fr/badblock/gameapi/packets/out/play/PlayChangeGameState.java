package fr.badblock.gameapi.packets.out.play;

import fr.badblock.gameapi.packets.BadblockOutPacket;
import lombok.Getter;

/**
 * Packet envoy� lorsque une donn�e de jeu change.
 * 
 * @author LeLanN
 */
public interface PlayChangeGameState extends BadblockOutPacket {
	/**
	 * Repr�sente les diff�rentes choses pouvant �tre appel�e avec
	 * {@link PlayChangeGameState}
	 * 
	 * @author LeLanN
	 */
	public static enum GameState {
		INVALID_BED(0), RAINING_END(1), RAINING_START(2),
		/**
		 * Valeur : valeur num�rique du gamemode (0, 1, 2, 3)
		 */
		GAMEMODE_CHANGE(3), ENTER_CREDITS(4),
		/**
		 * Valeurs :
		 * <ul>
		 * <li>0 : montre l'image de d�mo</li>
		 * <li>101 : montre le message indiquant comment se d�placer</li>
		 * <li>102 : montre le message indiquant comment sauter
		 * <li>
		 * <li>103 : montre le message indiquant comment ouvrir l'inventaire
		 * <li>
		 * </ul>
		 */
		DEMO_MESSAGE(5), ARROW_HIT_PLAYER(6),
		/**
		 * La luminosit� actuelle, entre 0 et 1 (0 = lumineux, 1 = sombre)
		 */
		FADE_VALUE(7),
		/**
		 * L'heure du monde en ticks (entre 0 et 24000)
		 */
		FADE_TIME(8), PLAY_MOB_APPEARANCE(10);

		public static GameState getFromId(int id) {
			for (GameState a : values())
				if (a.getValue() == id)
					return a;
			return null;
		}

		@Getter
		private final int value;

		GameState(int value) {
			this.value = value;
		}
	}

	/**
	 * R�cup�re ce qu'il faut changer
	 * 
	 * @return Ce qu'il faut changer
	 */
	public GameState getState();

	/**
	 * R�cup�re la valeur du gamestate. Voir {@link GameState} POur conna�tre
	 * les valeurs possibles (rien �crit = pas de valeur particuli�re).
	 * 
	 * @return La valeur
	 */
	public float getValue();

	/**
	 * D�finit ce qu'il faut changer
	 * 
	 * @param state
	 *            Ce qu'il faut changer
	 * @return Le packet
	 */
	public PlayChangeGameState setState(GameState state);

	/**
	 * D�finit la valeur du gamestate. Voir {@link GameState} POur conna�tre les
	 * valeurs possibles (rien �crit = pas de valeur particuli�re).
	 * 
	 * @param value
	 *            La valeur
	 * @return Le packet
	 */
	public PlayChangeGameState setValue(float value);
}
