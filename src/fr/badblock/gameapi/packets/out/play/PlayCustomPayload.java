package fr.badblock.gameapi.packets.out.play;

import fr.badblock.gameapi.packets.BadblockOutPacket;

/**
 * Packet de 'plugin message'
 * 
 * @author LeLanN
 */
public interface PlayCustomPayload extends BadblockOutPacket {
	/**
	 * R�cup�re le channel du plugin message
	 * 
	 * @return Le channel
	 */
	public String getChannel();

	/**
	 * R�cup�re les donn�es du plugin message
	 * 
	 * @return Les donn�es
	 */
	public byte[] getData();
}
