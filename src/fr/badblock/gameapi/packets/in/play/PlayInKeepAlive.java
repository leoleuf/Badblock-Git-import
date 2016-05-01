package fr.badblock.gameapi.packets.in.play;

import fr.badblock.gameapi.packets.BadblockInPacket;
import fr.badblock.gameapi.packets.BadblockInPackets;

/**
 * Packet envoy� pour v�rifier que la connection entre le joueur et le client est toujours active.
 * @author LeLanN
 */
public interface PlayInKeepAlive extends BadblockInPacket {
	/**
	 * Une cl� al�atoire envoy� pr�cedemment par le serveur.
	 * @return La cl� al�atoire
	 */
	public int getKeepAliveId();
	
	@Override
	default BadblockInPackets getType() {
		return BadblockInPackets.PLAY_KEEPALIVE;
	}
}
