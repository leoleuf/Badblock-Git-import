package fr.badblock.gameapi.packets.in.play;

import fr.badblock.gameapi.packets.BadblockInPacket;
import fr.badblock.gameapi.packets.BadblockInPackets;

/**
 * Packet envoy� par le joueur lorsqu'il utilise "tab" dans le chat
 * @author LeLanN
 */
public interface PlayInTabComplete extends BadblockInPacket {
	/**
	 * R�cup�re le message avant le curseur
	 * @return Le message
	 */
	public String getMessage();
	
	@Override
	default BadblockInPackets getType() {
		return BadblockInPackets.PLAY_TAB_COMPLETE;
	}
}
