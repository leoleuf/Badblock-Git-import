package fr.badblock.gameapi.packets.out.play;

import org.bukkit.entity.Player;

import fr.badblock.gameapi.packets.BadblockOutPacket;

/**
 * Packet envoy� au joueur quand un autre joueur entre dans son champs de vision
 * @author LeLanN
 */
public interface PlayNamedEntitySpawn extends BadblockOutPacket {
	/**
	 * R�cup�re le joueur qui doit �tre affich�
	 * @return Le joueur
	 */
	public Player getPlayer();
	
	/**
	 * D�finit le joueur qui doit �tre affich�
	 * @param player Le joueur
	 * @return Le packet
	 */
	public PlayNamedEntitySpawn setPlayer(Player player);
}
