package fr.badblock.gameapi.packets.out.play;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import fr.badblock.gameapi.packets.BadblockOutPacket;

/**
 * Permet de t�l�porter le joueur<br>
 * Aucune m�thode ajout� car {@link Player#teleport(Location)} et {@link #setCancelled(boolean)} suffisent.
 * @author LeLanN
 */
public interface PlayPosition extends BadblockOutPacket {

}
