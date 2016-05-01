package fr.badblock.gameapi.packets.in.play;

import fr.badblock.gameapi.packets.BadblockInPacket;
import fr.badblock.gameapi.packets.BadblockInPackets;

/**
 * Packet envoy� par un joueur quand il �crit un message dans le chat (ou commande).
 * @author LeLanN
 */
public interface PlayInChat extends BadblockInPacket {
	/**
	 * Le message envoy� par le joueur
	 * @return Le message
	 */
	public String getMessage();
	
	@Override
	default BadblockInPackets getType() {
		return BadblockInPackets.PLAY_CHAT;
	}
}
