package fr.badblock.gameapi.packets.out.play;

import fr.badblock.gameapi.packets.BadblockOutPacket;

/**
 * Packet envoy� quand une entit� est 'attach�e' � une autre (v�hicule ou
 * laisse).
 * 
 * @author LeLanN
 */
public interface PlayAttachEntity extends BadblockOutPacket {
	/**
	 * R�cup�re l'ID de l'entit� attach�e
	 * 
	 * @return L'ID
	 */
	public int getEntityId();

	/**
	 * D�finit l'ID de l'entit� attach�e
	 * 
	 * @param entityId
	 *            L'ID
	 * @return Le packet
	 */
	public PlayAttachEntity setEntityId(int entityId);

	/**
	 * R�cup�re l'ID du v�hicule (si -1, l'entit� descendra du v�hicle)
	 * 
	 * @return L'ID
	 */
	public int getVehicleId();

	/**
	 * D�finit l'ID du v�hicule (si -1, l'entit� descendra du v�hicule)
	 * 
	 * @param vehicleId
	 *            L'ID
	 * @return Le packet
	 */
	public PlayAttachEntity setVehicleId(int vehicleId);

	/**
	 * Si il s'agit d'une laisse
	 * 
	 * @return Un boolean
	 */
	public boolean isLeashes();

	/**
	 * Si il s'agit d'une laisse
	 * 
	 * @param leashes
	 *            Si true, c'est une laisse
	 * @return Le packet
	 */
	public PlayAttachEntity setLeashes(boolean leashes);
}
