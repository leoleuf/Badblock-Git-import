package fr.badblock.gameapi.packets.out.play;

import org.bukkit.entity.Entity;

import fr.badblock.gameapi.packets.BadblockOutPacket;

/**
 * Packet envoy� au joueur quand des entit�s sont d�truites (c'est � dire, {@link Entity#remove()})
 * @author LeLanN
 */
public interface PlayEntityDestroy extends BadblockOutPacket {
	/**
	 * R�cup�re entit�s d�truites
	 * @return Les entit�s
	 */
	public int[] getEntities();
	
	/**
	 * D�finit les entit�s d�truites
	 * @param entities Les entit�s
	 * @return Le packet
	 */
	public PlayEntityDestroy setEntities(int[] entities);
}
