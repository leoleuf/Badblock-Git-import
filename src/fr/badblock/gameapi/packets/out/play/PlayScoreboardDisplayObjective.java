package fr.badblock.gameapi.packets.out.play;

import fr.badblock.gameapi.packets.BadblockOutPacket;
import lombok.Getter;

/**
 * Le packet envoy� pour dire au client d'afficher un objectif d�j� connu.
 * @author LeLanN
 */
public interface PlayScoreboardDisplayObjective extends BadblockOutPacket {
	/**
	 * D�finit la position de l'objectif
	 * @param objectivePosition La position
	 * @return Le packet
	 */
	public PlayScoreboardDisplayObjective setObjectivePosition(ObjectivePosition objectivePosition);
	
	/**
	 * D�finit le nom (interne) de l'objectif en question
	 * @param objectiveName Le nom
	 * @return Le packet
	 */
	public PlayScoreboardDisplayObjective setObjectiveName(String objectiveName);

	/**
	 * Repr�sente les diff�rentes positions possibles du scoreboard
	 * @author LeLanN
	 */
	public enum ObjectivePosition {
		/**
		 * Dans la tablist (� c�t� du pseudo du joueur), g�n�ralement la vie du joueur
		 */
		LLST((byte) 0),
		/**
		 * Le scoreboard en lui-m�me
		 */
		SIDEBAR((byte) 1),
		/**
		 * En dessous du pseudo du joueur
		 */
		BELOW_NAME((byte) 2);
		
		@Getter
		private byte data;
		
		ObjectivePosition(byte data){
			this.data = data;
		}
		
		public static ObjectivePosition getByValue(byte value){
			for(ObjectivePosition c : values())
				if(c.getData() == value)
					return c;
			return null;
		}
	}
}