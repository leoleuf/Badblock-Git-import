package fr.badblock.gameapi.packets.in.play;

import fr.badblock.gameapi.packets.BadblockInPacket;
import fr.badblock.gameapi.packets.BadblockInPackets;

/**
 * Packet envoy� par un joueur pour controller un v�hicule
 * 
 * @author LelanN
 */
public interface PlayInSteerVehicle extends BadblockInPacket {
	/**
	 * R�cup�re la valeur du mouvement haut/bas demand� (positif = vers l'avant)
	 * 
	 * @return La valeur
	 */
	public float getForward();

	/**
	 * R�cup�re la valeur du mouvement lat�ral demand� (positif = gauche)
	 * 
	 * @return La valeur
	 */
	public float getSideways();

	@Override
	default BadblockInPackets getType() {
		return BadblockInPackets.PLAY_STEER_VEHICLE;
	}

	/**
	 * Si le joueur veut que le v�hicule saute
	 * 
	 * @return Un boolean
	 */
	public boolean isJump();

	/**
	 * Si le joueur veut descendre
	 * 
	 * @return Un boolean
	 */
	public boolean isUnmount();
}
