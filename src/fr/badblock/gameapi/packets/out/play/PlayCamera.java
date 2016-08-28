package fr.badblock.gameapi.packets.out.play;

import fr.badblock.gameapi.packets.BadblockOutPacket;

/**
 * Lorsque envoy� au joueur il 'devient' une autre entit� (gamemode 3)
 * 
 * @author LeLanN
 */
public interface PlayCamera extends BadblockOutPacket {
	/**
	 * R�cup�re l'entit�
	 * 
	 * @return L'ID
	 */
	public int getEntityId();

	/**
	 * D�finit l'entit�
	 * 
	 * @param entityId
	 *            L'ID
	 * @return Le packet
	 */
	public PlayCamera setEntityId(int entityId);
}
