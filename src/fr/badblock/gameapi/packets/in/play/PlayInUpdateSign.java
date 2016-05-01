package fr.badblock.gameapi.packets.in.play;

import fr.badblock.gameapi.packets.BadblockInPacket;
import fr.badblock.gameapi.packets.BadblockInPackets;
import fr.badblock.gameapi.utils.selections.Vector3f;
import net.md_5.bungee.api.chat.BaseComponent;

/**
 * Packet envoy� par le client quand il vaut modifier un panneau
 * @author LeLanN
 */
public interface PlayInUpdateSign extends BadblockInPacket {
	/**
	 * R�cup�re la position du panneau
	 * @return La position
	 */
	public Vector3f getPosition();
	
	/**
	 * R�cup�re le contenu de la ligne 1
	 * @return Le contenu
	 */
	public BaseComponent[] getLine1();
	
	/**
	 * R�cup�re le contenu de la ligne 2
	 * @return Le contenu
	 */
	public BaseComponent[] getLine2();
	
	/**
	 * R�cup�re le contenu de la ligne 3
	 * @return Le contenu
	 */
	public BaseComponent[] getLine3();
	
	/**
	 * R�cup�re le contenu de la ligne 4
	 * @return Le contenu
	 */
	public BaseComponent[] getLine4();
	
	@Override
	default BadblockInPackets getType() {
		return BadblockInPackets.PLAY_UPDATE_SIGN;
	}
}
