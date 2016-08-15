package fr.badblock.protocol.packets;

import java.io.IOException;
import java.util.UUID;

import fr.badblock.protocol.PacketHandler;
import fr.badblock.protocol.buffers.ByteInputStream;
import fr.badblock.protocol.buffers.ByteOutputStream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Packet permettant l'utilisation du Chat :
 * ===============================================
 * -> Chat message FLAT			=> 1
 * -> Chat message JSON			=> 2
 * -> Tablist footer/header		=> 3
 * -> ActionBar					=> 4
 * -> Title / Subtitle			=> 5
 * -> Commande					=> 6
 * 
 * => Timings					=> If Title/Subtitle or ActionBar
 * ===============================================
 * @author LeLanN
 */
@Data@NoArgsConstructor@AllArgsConstructor
public class PacketPlayerChat implements Packet {
	/**
	 * Les utilisateurs � qui envoyer le message, si * => broadcast (ou commande console)
	 */
	private UUID       user;
	
	/**
	 * Type de message
	 */
	private ChatAction type;

	/**
	 * Le message envoy� (sous forme d'array)
	 * ==> Array :
	 * 	=> Si message normal : plusieurs messages
	 *  => Si tablist		 : haut / bas
	 *  => Si actionbar		 : message unique
	 *  => Si title			 : deux messages (title / subtitle)
	 *  => Si commande		 : plusieurs commandes
	 */
	private String[]   messages;
	
	private int 	   fadeIn, 
					   stay, 
					   fadeOut;
	
	public PacketPlayerChat(UUID user, ChatAction type, String... messages){
		this(user, type, messages, 0, 0, 0);
	}
	
	@Override
	public void read(ByteInputStream input) throws IOException {
		if(input.readBoolean())
			user     = input.readUUID();
		else user    = null;
		type  	 = ChatAction.getAction(input.readInt());
		messages = input.readArrayUTF();
		
		if(type.isNeedTimings()){
			fadeIn  = input.readInt();
			stay    = input.readInt();
			fadeOut = input.readInt();
		}
	}

	@Override
	public void write(ByteOutputStream output) throws IOException {
		output.writeBoolean(user != null);
		if(user != null)
			output.writeUUID(user);
		output.writeInt(type.getId());
		output.writeArrayUTF(messages);
		
		if(type.isNeedTimings()){
			output.writeInt(fadeIn);
			output.writeInt(stay);
			output.writeInt(fadeOut);
		}
	}
	

	@Override
	public void handle(PacketHandler handler) throws Exception {
		handler.handle(this);
	}
	
	@Getter public enum ChatAction {
		MESSAGE_FLAT(1),
		MESSAGE_JSON(2),
		TABLIST(3),
		ACTION_BAR(4, true),
		TITLE(5, true),
		COMMAND(6),
		LADDER_COMMAND(7);
		
		private final int 	  id;
		private final boolean needTimings;
		
		private ChatAction(int id){
			this(id, false);
		}

		private ChatAction(int id, boolean needTimings){
			this.id 		 = id;
			this.needTimings = needTimings;
		}
		
		public static ChatAction getAction(int id){
			for(final ChatAction action : values()){
				if(action.getId() == id)
					return action;
			}
			
			return null;
		}
	}
}