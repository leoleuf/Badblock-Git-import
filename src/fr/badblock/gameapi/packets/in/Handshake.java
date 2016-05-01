package fr.badblock.gameapi.packets.in;

import fr.badblock.gameapi.packets.BadblockInPacket;
import fr.badblock.gameapi.packets.BadblockInPackets;
import lombok.Getter;

/**
 * Premier packet envoy� par le joueur. Il n'est pas modifiable.
 * @author LeLanN
 */
public interface Handshake extends BadblockInPacket {
	/**
	 * R�cup�re la version du protocol souhait� par le joueur
	 * @return La version du protocol
	 */
	public int getProtocolVersion();
	
	/**
	 * R�cup�re l'addresse du serveur selon le joueur (contient aussi, avec Bungee, des informations sur le joueur)
	 * @return L'addresse du serveur
	 */
	public String getServerAddress();
	
	/**
	 * R�cup�re le port du serveur selon le joueur
	 * @return Le port du serveur
	 */
	public int getServerPort();

	/**
	 * R�cup�re le protocol que le joueur souhaite pour la prochaine �tape
	 * @return La prochaine �tape
	 */
	public NextState getNextState();
	
	@Override
	default BadblockInPackets getType() {
		return BadblockInPackets.HANDSHAKE;
	}
	
	/**
	 * Repr�sente les diff�rents protocoles pouvant �tre demand� par le joueur lors de l'handhsake
	 * @author LeLanN
	 */
	public enum NextState {
		/**
		 * Le client veut ping le serveur
		 */
		STATUS(1),
		/**
		 * Le client veut jouer
		 */
		LOGIN(2);
		
		@Getter private int id;
		
		NextState(int id){
			this.id = id;
		}
		
		public static NextState getById(int id){
			for(NextState state : values())
				if(state.getId() == id)
					return state;
			return null;
		}
	}
}
