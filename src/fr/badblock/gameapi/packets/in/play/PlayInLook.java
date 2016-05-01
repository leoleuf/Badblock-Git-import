package fr.badblock.gameapi.packets.in.play;

import fr.badblock.gameapi.packets.BadblockInPacket;
import fr.badblock.gameapi.packets.BadblockInPackets;

/**
 * Packet envoy� pour d�finir la direction regard�e
 * @author LeLanN
 */
public interface PlayInLook extends BadblockInPacket {
	/**
	 * Le nouveau yaw
	 * @return Le yaw
	 */
	public float getYaw();
	
	/**
	 * Le nouveau pitch
	 * @return Le pitch
	 */
	public float getPitch();

	@Override
	default BadblockInPackets getType() {
		return BadblockInPackets.PLAY_LOOK;
	}
}
