package fr.badblock.gameapi.packets.in;

import fr.badblock.gameapi.packets.BadblockInPacket;
import fr.badblock.gameapi.packets.BadblockInPackets;

/**
 * Packet envoy� pour conna�tre le ping du joueur avec le serveur (temps de
 * r�ponse).
 * 
 * @author LeLanN
 */
public interface StatusPing extends BadblockInPacket {
	/**
	 * Un nombre qui n'a pas de r�elle valeur, simplement utilis� pour �tre s�r
	 * que la r�ponse au ping est la bonne.<br>
	 * 
	 * @return Le nombre
	 */
	public long getLongValue();

	@Override
	default BadblockInPackets getType() {
		return BadblockInPackets.STATUS_PING;
	}
}
