package fr.badblock.gameapi.packets;

import fr.badblock.gameapi.players.BadblockPlayer;

/**
 * Permet d'�couter de mani�re simple les packets venant du client
 * 
 * @author LeLanN
 *
 * @param <T>
 *            Le type de packet � �couter
 * @see fr.badblock.gameapi.GameAPI#listenAtPacket(GlobalPacketListener)
 */
public abstract class GlobalPacketListener {

	/**
	 * Appel la classe avant le packet re�u
	 * 
	 * @param packet
	 *            La classe
	 */
	public abstract void listen(BadblockPlayer player, Object packet);

}
