package fr.badblock.gameapi.packets.out.play;

import fr.badblock.gameapi.packets.BadblockOutPacket;
import lombok.Getter;

/**
 * Packet qui fait joueur une 'animation' (mouvement) � une entit�.
 * 
 * @author LeLanN
 */
public interface PlayAnimation extends BadblockOutPacket {
	/**
	 * Repr�sente les diff�rentes animations possibles pour
	 * {@link PlayAnimation}
	 * 
	 * @author LeLanN
	 */
	public enum Animation {
		SWING_ARM(0), LEAVE_BED(2), EAT_FOOD(3), CRITICAL_EFFECT(4), MAGICAL_CRITICAL_EFFECT(5);

		public static Animation getFromId(int id) {
			for (Animation a : values())
				if (a.getId() == id)
					return a;
			return null;
		}

		@Getter
		private final int id;

		Animation(int id) {
			this.id = id;
		}
	}

	/**
	 * R�cup�re l'animation � jouer
	 * 
	 * @return L'animation
	 */
	public Animation getAnimation();

	/**
	 * R�cup�re l'id de l'entit� devant faire le mouuvement
	 * 
	 * @return L'id
	 */
	public int getEntityId();

	/**
	 * D�finit l'animation � jouer
	 * 
	 * @param animation
	 *            L'animation
	 * @return Le packet
	 */
	public PlayAnimation setAnimation(Animation animation);

	/**
	 * D�finit l'id de l'entit� devant faire le mouvement
	 * 
	 * @param entityId
	 *            L'id
	 * @return Le packet
	 */
	public PlayAnimation setEntityId(int entityId);
}
