package fr.badblock.gameapi.packets.out.play;

import org.bukkit.Material;

import fr.badblock.gameapi.packets.BadblockOutPacket;
import fr.badblock.gameapi.utils.selections.Vector3f;

/**
 * Packet envoy� par le serveur lors de :
 * <ul>
 * <li>Ouverture et fermeture de coffre
 * <li>Activation/D�sactivation de pistons
 * <li>Noteblock jouant une note
 * <li>Mise � jour de balise
 * </ul>
 * <br>
 * @see "http://wiki.vg/Block_Actions"
 * @author LeLanN
 */
public interface PlayBlockAction extends BadblockOutPacket {
	/**
	 * R�cup�re les coordonn�es du bloc
	 * @return Les coordonn�es
	 */
	public Vector3f getBlockPosition();
	
	/**
	 * D�finit les coordonn�es du bloc
	 * @param position Les coordonn�es
	 * @return Le packet
	 */
	public PlayBlockAction setBlockPosition(Vector3f position);
	
	/**
	 * R�cup�re la premi�re donn�e.
	 * @see "http://wiki.vg/Block_Actions"
	 * @return La premi�re donn�e
	 */
	public byte getByte1();
	
	/**
	 * D�finit la premi�re donn�e.
	 * @see "http://wiki.vg/Block_Actions"
	 * @param value La premi�re donn�e
	 * @return Le packet
	 */
	public PlayBlockAction setByte1(byte value);
	
	/**
	 * R�cup�re la deuxi�me donn�e.
	 * @see "http://wiki.vg/Block_Actions"
	 * @return La deuxi�me donn�e
	 */
	public byte getByte2();
	
	/**
	 * D�finit la deuxi�me donn�e.
	 * @see "http://wiki.vg/Block_Actions"
	 * @param value La deuxi�me donn�e
	 * @return Le packet
	 */
	public PlayBlockAction setByte2(byte value);
	
	/**
	 * R�cup�re le type de block
	 * @return Le type de block
	 */
	public Material getBlockType();
	
	/**
	 * D�finit le type de block
	 * @param type Le type de block
	 * @return Le packet
	 */
	public PlayBlockAction setBlockType(Material type);
}
