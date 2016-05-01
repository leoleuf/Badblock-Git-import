package fr.badblock.gameapi.packets.out.play;

import org.bukkit.potion.PotionEffectType;

import fr.badblock.gameapi.packets.BadblockOutPacket;

/**
 * Packet envoy� pour enlev� un effet de potion d'une entit�
 * @author LeLanN
 */
public interface PlayRemoveEntityEffect extends BadblockOutPacket {
	/**
	 * R�cup�re l'entit� (id)
	 * @return L'id
	 */
	public int getEntityId();
	
	/**
	 * D�finit l'entit� (id)
	 * @param entityId L'id
	 * @return Le packet
	 */
	public PlayRemoveEntityEffect setEntityId(int entityId);
	
	/**
	 * R�cup�re l'effect � enlever
	 * @return L'effect
	 */
	public PotionEffectType getEffect();
	
	/**
	 * D�finit l'effect � enlever
	 * @param effect L'effect
	 * @return Le packet
	 */
	public PlayRemoveEntityEffect setEffect(PotionEffectType effect);
}
