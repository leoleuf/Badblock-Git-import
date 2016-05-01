package fr.badblock.gameapi.packets.out.play;

import fr.badblock.gameapi.packets.BadblockOutPacket;

/**
 * Packet envoy� pour changer les lignes au-dessus et en-dessous des joueurs dans la tablist
 * @author LelanN
 */
public interface PlayPlayerListHeaderFooter extends BadblockOutPacket {
	/**
	 * R�cup�re les message du header
	 * @return Le messages
	 */
	public String getHeader();
	
	/**
	 * D�finit le message du header
	 * @param header Le message
	 * @return Le packet
	 */
	public PlayPlayerListHeaderFooter setHeader(String header);
	
	/**
	 * R�cup�re les message du footer
	 * @return Le message
	 */
	public String getFooter();
	
	/**
	 * D�finit le message du footer
	 * @param footer Le message
	 * @return Le packet
	 */
	public PlayPlayerListHeaderFooter setFooter(String footer);
}
