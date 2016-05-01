package fr.badblock.gameapi.packets.in.play;

import fr.badblock.gameapi.packets.BadblockInPacket;
import fr.badblock.gameapi.packets.BadblockInPackets;
import lombok.Getter;

/**
 * Packet envoy� par le client lorsqu'il change ses param�tres
 * @author LeLanN
 */
public interface PlayInSettings extends BadblockInPacket {
	/**
	 * La langue du joueur, sous la forme langue_PAYS (par exemple : fr_FR)
	 * @return La langue
	 */
	public String getLocale();
	
	/**
	 * R�cup�re la view distance du joueur en chunk
	 * @return La view distance
	 */
	public int getViewDistance();
	
	/**
	 * R�cup�re m'activation du chat du joueur
	 * @return L'activation
	 */
	public ChatMode getChatMode();
	
	/**
	 * Si la couleur est activ�e dans le chat du joueur
	 * @return Si la couleur est activ�e
	 */
	public boolean isColorEnabled();
	
	/**
	 * Les parties du skin que le joueur affiche :
	 * <ul>
	 * <li>0x01 : Cape activ�e
	 * <li>0x02 : Veste activ�e
	 * <li>0x04 : Manche gauche activ�e
	 * <li>0x08 : Manche droite activ�e
	 * <li>0x10 : Jambi�re de gauche activ�e
	 * <li>0x20 : Jambi�re de droite activ�e
	 * <li>0x40 : Chapeau activ�
	 * </ul>
	 * @return
	 */
	public int getDisplayedSkinParts();
	
	@Override
	default BadblockInPackets getType() {
		return BadblockInPackets.PLAY_SETTINGS;
	}
	
	/**
	 * Repr�sente les diff�rents modes de chats possibles
	 * @author LeLanN
	 */
	public enum ChatMode {
		ENABLED(0),
		COMMANDS_ONLY(1),
		HIDDEN(2);
		
		@Getter private int id;
		
		ChatMode(int id){
			this.id = id;
		}
		
		public static ChatMode getById(int id){
			for(ChatMode state : values())
				if(state.getId() == id)
					return state;
			return null;
		}
	}
}
