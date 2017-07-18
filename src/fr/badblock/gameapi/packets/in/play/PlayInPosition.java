package fr.badblock.gameapi.packets.in.play;

import fr.badblock.gameapi.packets.BadblockInPackets;

/**
 * Packet envoy� pour d�finir la position
 * 
 * @author LeLanN
 */
public interface PlayInPosition extends PlayInFlying {

	@Override
	default BadblockInPackets getType() {
		return BadblockInPackets.PLAY_POSITION;
	}
}
