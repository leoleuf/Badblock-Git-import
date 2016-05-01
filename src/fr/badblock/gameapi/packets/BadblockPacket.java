package fr.badblock.gameapi.packets;

/**
 * Repr�sente un packet BadBlock
 * @author audra
 *
 */
public interface BadblockPacket {
	/**
	 * V�rifie si le packet sera trait� par le serveur
	 * @return Si il sera trait�
	 */
	public boolean isCancelled();
	
	/**
	 * D�finit si le packet sera trait� par le serveur
	 * @param cancelled Si il sera trait�
	 */
	public void setCancelled(boolean cancelled);
}
