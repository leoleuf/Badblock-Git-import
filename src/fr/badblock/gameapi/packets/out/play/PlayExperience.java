package fr.badblock.gameapi.packets.out.play;

import org.bukkit.entity.Player;

import fr.badblock.gameapi.packets.BadblockOutPacket;

/**
 * Packet envoy� au client pour changer son XP. Aucune m�thode ajout� car
 * {@link Player#setLevel(int)} (et autres) et {@link #setCancelled(boolean)}
 * suffisent, d'autant plus que cela pourrait causer des probl�mes si le serveur
 * n'est pas au courant de l'update.
 * 
 * @author LeLanN
 */
public interface PlayExperience extends BadblockOutPacket {

}
