package fr.badblock.gameapi.packets;

/**
 * Repr�sente un packet envoy� par un joueur. Le packet est interc�pt� et r�cup�rable gr�ce aux listeners.
 * 
 * Si un packet n'est pas assez document�, penser � se documenter gr�ce � http://wiki.vg/Protocol<br>
 * Pour instancier un packet, voir {@link fr.badblock.gameapi.GameAPI#createPacket(Class)}<br>
 * La classe n'est pas � confondre avec {@link BadblockOutPacket}
 * 
 * @author LeLanN
 */
public interface BadblockInPacket extends BadblockPacket {
	/**
	 * R�cup�re le type de packet
	 * @return Le type de packet
	 */
	public BadblockInPackets getType();
}
