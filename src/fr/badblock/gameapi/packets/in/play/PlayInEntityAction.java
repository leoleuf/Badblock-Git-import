package fr.badblock.gameapi.packets.in.play;

import fr.badblock.gameapi.packets.BadblockInPacket;
import fr.badblock.gameapi.packets.BadblockInPackets;
import lombok.Getter;

/**
 * Packet envoyé par le client lorsque il exécute une action précise.
 * 
 * @author LeLanN
 */
public interface PlayInEntityAction extends BadblockInPacket {
	/**
	 * Représente les différentes actions possibles pour
	 * {@link PlayInEntityAction}
	 * 
	 * @author LeLanN
	 */
	public enum EntityActions {
		START_SNEAKING(0), STOP_SNEAKING(1),
		/**
		 * Sortie du lit
		 */
		STOP_SLEEPING(2), START_SPRINTING(3), STOP_SPRINTING(4),
		/**
		 * Saute avec un cheval
		 */
		RIDING_JUMP(5),
		/**
		 * Ouvre l'inventaire d'un cheval en course
		 */
		OPEN_INVENTORY(6);

		public static EntityActions getById(int id) {
			for (EntityActions state : values())
				if (state.getId() == id)
					return state;
			return null;
		}

		@Getter
		private int id;

		EntityActions(int id) {
			this.id = id;
		}
	}

	/**
	 * Récupčre l'action faite par le joueur
	 * 
	 * @return L'action
	 */
	public EntityActions getAction();

	/**
	 * La valeur de l'action. Actuellement, utilisé uniquement par
	 * {@link EntityActions#RIDING_JUMP} (représente la force du saut, de 0 ŕ
	 * 100).
	 * 
	 * @return La valeur de l'action
	 */
	public int getActionParameter();

	/**
	 * Récupčre l'ID de l'entité (en réalité, celle du joueur)
	 * 
	 * @return L'ID
	 */
	public int getEntityId();

	@Override
	default BadblockInPackets getType() {
		return BadblockInPackets.PLAY_ENTITY_ACTION;
	}
}
