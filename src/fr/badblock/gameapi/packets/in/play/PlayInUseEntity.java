package fr.badblock.gameapi.packets.in.play;

import fr.badblock.gameapi.packets.BadblockInPacket;
import fr.badblock.gameapi.packets.BadblockInPackets;
import fr.badblock.gameapi.utils.selections.Vector3f;
import lombok.Getter;

/**
 * Packet envoy� par le joueur lorsqu'il int�ragit avec une entit�
 * 
 * @author LeLanN
 */
public interface PlayInUseEntity extends BadblockInPacket {
	/**
	 * Repr�sente les diff�rentes actions pour {@link PlayInUseEntity}.
	 * 
	 * @author LeLanN
	 */
	public enum UseEntityAction {
		INTERACT(0), ATTACK(1), INTERACT_AT(2);

		public static UseEntityAction getById(int id) {
			for (UseEntityAction state : values())
				if (state.getId() == id)
					return state;
			return null;
		}

		@Getter
		private int id;

		UseEntityAction(int id) {
			this.id = id;
		}
	}

	/**
	 * R�cup�re l'action effectu�e par le joueur
	 * 
	 * @return L'action
	 */
	public UseEntityAction getAction();

	/**
	 * R�cup�re l'action effectu�e par le joueur
	 * 
	 * @return L'action
	 */
	public void setAction(UseEntityAction action);
	
	/**
	 * R�cup�re l'ID de l'entit� vis�e
	 * 
	 * @return L'ID
	 */
	public int getEntityId();
	
	/**
	 * R�cup�re l'ID de l'entit� vis�e
	 * 
	 * @return L'ID
	 */
	public void setEntityId(int id);

	/**
	 * R�cup�re la position du target (uniquement pour
	 * {@link UseEntityAction#INTERACT_AT})
	 * 
	 * @return La position
	 */
	public Vector3f getTargetPosition();

	@Override
	default BadblockInPackets getType() {
		return BadblockInPackets.PLAY_USE_ENTITY;
	}
}
