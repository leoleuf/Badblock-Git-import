package fr.badblock.gameapi.packets.out.play;

import fr.badblock.gameapi.packets.BadblockOutPacket;

/**
 * Packet utiliser par le client pour update les abilities du joueur (ce qu'il
 * est autoris� � faire)
 * 
 * @author LeLanN
 */
public interface PlayAbilities extends BadblockOutPacket {
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

	/**
	 * Si le joueur est autoris� � voler
	 * 
	 * @return Un boolean
	 */
	public boolean isAllowedToFly();

	/**
	 * Si le joueur est en cr�atif
	 * 
	 * @return Un boolean
	 */
	public boolean isCreative();

	/**
	 * Si le joueur vole (seul param�tre utilis�)
	 * 
	 * @return Un boolean
	 */
	public boolean isFlying();

	/**
	 * Si le joueur est en godmode
	 * 
	 * @return Un boolean
	 */
	public boolean isGodmoded();
}
