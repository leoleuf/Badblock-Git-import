package fr.badblock.gameapi.packets.out.play;

import fr.badblock.gameapi.packets.BadblockOutPacket;
import lombok.Getter;
import net.md_5.bungee.api.chat.BaseComponent;

/**
 * Packet envoy� lorsque l'on veut envoyer un message chat/actionbar au joueur
 * @author LeLanN
 */
public interface PlayChat extends BadblockOutPacket {
	/**
	 * R�cup�re le type de message
	 * @return Le type
	 */
	public ChatType getType();
	
	/**
	 * D�finit le type de message
	 * @param type Le type
	 * @return Le packet
	 */
	public PlayChat setType(ChatType type);
	
	/**
	 * R�cup�re les messages � envoyer
	 * @return Les messages
	 */
	public BaseComponent[] getComponents();
	
	/**
	 * D�finit les messages � envoyer
	 * @param components Les messages
	 * @return Le packet
	 */
	public PlayChat setComponents(BaseComponent[] components);
	
	/**
	 * Repr�sente les diff�rents types de messages envoyables gr�ce � {@link PlayChat}
	 * @author LeLanN
	 */
	public enum ChatType {
		/**
		 * Message dans le chat
		 */
	    CHAT(0),
	    /**
	     * Message (syst�me) dans le chat
	     */
	    SYSTEM(1),
	    /**
	     * ActionBar
	     */
	    ACTION(2);
		
		@Getter private byte value;
		
		ChatType(int value){
			this.value = (byte) value;
		}
		
		public static ChatType getByValue(byte value){
			for(ChatType c : values())
				if(c.getValue() == value)
					return c;
			return null;
		}
	}
}
