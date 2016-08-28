package fr.badblock.gameapi.packets.in.play;

import java.util.UUID;

import fr.badblock.gameapi.packets.BadblockInPacket;
import fr.badblock.gameapi.packets.BadblockInPackets;

/**
 * Packet envoy� par le client lorsqu'il veut se t�l�port� � un joueur en tant
 * que spectateur
 * 
 * @author LelanN
 */
public interface PlayInSpectate extends BadblockInPacket {
	/**
	 * R�cup�re l'UUID du joueur vis�
	 * 
	 * @return L'UUID
	 */
	public UUID getPlayerUID();

	@Override
	default BadblockInPackets getType() {
		return BadblockInPackets.PLAY_SPECCTATE;
	}
}
