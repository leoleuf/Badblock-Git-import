package fr.badblock.gameapi.packets.out.play;

import org.bukkit.entity.Player;

import fr.badblock.gameapi.packets.BadblockOutPacket;

/**
 * Packet envoy� par le serveur pour forcer le client � fermer un inventaire.<br>
 * Aucune m�thode ajout� car {@link Player#closeInventory()} et {@link #setCancelled(boolean)} suffisent.
 * @author LeLanN
 */
public interface PlayCloseWindow extends BadblockOutPacket {

}
