package fr.badblock.gameapi.packets.out.play;

import org.bukkit.block.Block;

import fr.badblock.gameapi.packets.BadblockOutPacket;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

/**
 * Packet envoy� par le serveur pour changer le contenu d'un panneau
 * 
 * @author LeLanN
 */
public interface PlayUpdateSign extends BadblockOutPacket {
	/**
	 * R�cup�re le block concern�
	 * 
	 * @return Le block
	 */
	public Block getBlock();

	/**
	 * R�cup�re les lignes du tableau
	 * 
	 * @return Les lignes
	 */
	public String[] getLines();

	/**
	 * R�cup�re les lignes du tableau en i18n
	 * 
	 * @return Les lignes
	 */
	public TranslatableString getLinesI18n();

	/**
	 * D�finit le block concern�
	 * 
	 * @param block
	 *            Le block
	 * @return Le packet
	 */
	public PlayUpdateSign setBlock(Block block);

	/**
	 * D�finit les lignes du tableau
	 * 
	 * @param lines
	 *            Les lignes
	 * @return Le packet
	 */
	public PlayUpdateSign setLines(String[] lines);

	/**
	 * D�finit les lignes du tableau en i18n
	 * 
	 * @param string
	 *            Les lignes
	 * @return Le packet
	 */
	public PlayUpdateSign setLinesI18n(TranslatableString string);
}
