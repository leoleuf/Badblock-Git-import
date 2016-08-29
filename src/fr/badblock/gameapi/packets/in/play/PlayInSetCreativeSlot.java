package fr.badblock.gameapi.packets.in.play;

import org.bukkit.inventory.ItemStack;

import fr.badblock.gameapi.packets.BadblockInPacket;
import fr.badblock.gameapi.packets.BadblockInPackets;

/**
 * Packet envoy� par le client lorsqu'il r�cup�re un item dans son inventaire
 * cr�atif
 * 
 * @author LeLanN
 */
public interface PlayInSetCreativeSlot extends BadblockInPacket {
	/**
	 * L'item concern�
	 * 
	 * @return L'item
	 */
	public ItemStack getItemStack();

	/**
	 * Le slot choisit. Si le joueur r�cup�re un item, -1, si il le place, le
	 * slot dans l'inventaire.
	 * 
	 * @return Le slot
	 */
	public int getSlot();

	@Override
	default BadblockInPackets getType() {
		return BadblockInPackets.PLAY_SET_CREATIVE_SLOT;
	}
}
