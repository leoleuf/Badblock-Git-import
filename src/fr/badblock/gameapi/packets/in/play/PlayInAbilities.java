package fr.badblock.gameapi.packets.in.play;

import fr.badblock.gameapi.packets.BadblockInPacket;
import fr.badblock.gameapi.packets.BadblockInPackets;

/**
 * Packet envoy� par le client quand il se met � voler. La plupart des
 * informations du packets sont inutiles.
 * 
 * @author LeLanN
 */
public interface PlayInAbilities extends BadblockInPacket {
	/**
	 * Si le joueur est en godmode
	 * 
	 * @return Un boolean
	 */
	public boolean isGodmoded();

	/**
	 * Si le joueur est autoris� � voler
	 * 
	 * @return Un boolean
	 */
	public boolean isAllowedToFly();

	/**
	 * Si le joueur vole (seul param�tre utilis�)
	 * 
	 * @return Un boolean
	 */
	public boolean isFlying();

	/**
	 * Si le joueur est en cr�atif
	 * 
	 * @return Un boolean
	 */
	public boolean isCreative();

	/**
	 * R�cup�re la vitesse de vole du joueur
	 * 
	 * @return La vitesse
	 */
	public float getFlyingSpeed();

	/**
	 * R�cup�re la vitesse de marche du joueur
	 * 
	 * @return La vitesse
	 */
	public float getWalkingSpeed();

	@Override
	default BadblockInPackets getType() {
		return BadblockInPackets.PLAY_ABILITIES;
	}
}
