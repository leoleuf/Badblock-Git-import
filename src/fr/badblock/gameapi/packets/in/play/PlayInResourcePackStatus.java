package fr.badblock.gameapi.packets.in.play;

import fr.badblock.gameapi.packets.BadblockInPacket;
import fr.badblock.gameapi.packets.BadblockInPackets;
import lombok.Getter;

/**
 * Packet envoy� par le joueur pour donner le statut du t�l�chargement du
 * ressource pack.
 * 
 * @author LeLanN
 */
public interface PlayInResourcePackStatus extends BadblockInPacket {
	/**
	 * Repr�sente les diff�rents statuts possible pour le ressource pack
	 * 
	 * @author LeLanN
	 */
	public enum ResourcePackStatus {
		SUCCESSFULLY_LOADED(0), DECLINED(1), FAILED_DOWNLOAD(2), ACCEPTED(3);

		public static ResourcePackStatus getById(int id) {
			for (ResourcePackStatus state : values())
				if (state.getId() == id)
					return state;
			return null;
		}

		@Getter
		private int id;

		ResourcePackStatus(int id) {
			this.id = id;
		}
	}

	/**
	 * Une cl� envoy� par le packet pour envoy� le resource pack
	 * 
	 * @return La cl�
	 */
	public String getHash();

	/**
	 * R�cup�re le statut actuel du t�l�chargement
	 * 
	 * @return Le statut
	 */
	public ResourcePackStatus getStatus();

	@Override
	default BadblockInPackets getType() {
		return BadblockInPackets.PLAY_RESOURCEPACK_STATUS;
	}

}
