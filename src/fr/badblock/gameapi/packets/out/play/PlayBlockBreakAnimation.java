package fr.badblock.gameapi.packets.out.play;

import org.bukkit.block.Block;

import fr.badblock.gameapi.packets.BadblockOutPacket;

/**
 * Packet envoy� lorsqu'une entit� casse un bloc afin de faire l'animation du
 * block qui se casse;
 * 
 * @author LeLanN
 */
public interface PlayBlockBreakAnimation extends BadblockOutPacket {
	/**
	 * R�cup�re le block sur lequel jouer l'animation
	 * 
	 * @return Le block
	 */
	public Block getBlock();

	/**
	 * R�cup�re l'id de l'entit�
	 * 
	 * @return L'id
	 */
	public int getEntityId();

	/**
	 * R�cup�re la taille de la 'fissure'
	 * 
	 * @return La taille (voir {@link #setState(int)})
	 */
	public int getState();

	/**
	 * D�finit le block sur lequel jouer l'animation
	 * 
	 * @param block
	 *            Le block
	 * @return Le packet
	 */
	public PlayBlockBreakAnimation setBlock(Block block);

	/**
	 * D�finit l'id de l'entit�
	 * 
	 * @param entityId
	 *            L'id
	 * @return Le packet
	 */
	public PlayBlockBreakAnimation setEntityId(int entityId);

	/**
	 * D�finit la taille de la 'fissure'
	 * 
	 * @param state
	 *            La taille, de 0 � 9. Si c'est une autre valeur, plus
	 *            d'animation.
	 * @return Le packet
	 */
	public PlayBlockBreakAnimation setState(int state);
}
