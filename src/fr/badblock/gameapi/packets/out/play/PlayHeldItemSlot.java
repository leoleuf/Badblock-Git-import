package fr.badblock.gameapi.packets.out.play;

import org.bukkit.inventory.PlayerInventory;

import fr.badblock.gameapi.packets.BadblockOutPacket;

/**
 * Packet envoy� par le serveur pour faire changer le slot s�l�ctionn� par le joueur.
 * Aucune m�thode ajout� car {@link PlayerInventory#setHeldItemSlot(int)} et {@link #setCancelled(boolean)} suffisent.
 * @author LeLanN
 */
public interface PlayHeldItemSlot extends BadblockOutPacket {

}
