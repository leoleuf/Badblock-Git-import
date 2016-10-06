package fr.badblock.protocol.packets;

import java.io.IOException;

import fr.badblock.protocol.PacketHandler;
import fr.badblock.protocol.buffers.ByteInputStream;
import fr.badblock.protocol.buffers.ByteOutputStream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Packet permettant l'échange de données :
 * - Demande d'envoit des données 			=> 1
 * - Envoit des données (ben oué) 			=> 2
 * - Modification des données 				=> 3
 * 
 * Différents types de données :
 * - Joueur									=> 1
 * - IP										=> 2
 * - Permissions							=> 3
 * - Commandes								=> 4
 * - Motd									=> 5s
 * 
 * @author LeLanN
 */
@Data@NoArgsConstructor@AllArgsConstructor
public class PacketPlayerData implements Packet {
	/**
	 * Le type de données
	 */
	private DataType	type;
	
	/**
	 * L'action à exécuter sur les données
	 */
	private DataAction	action;
	
	/**
	 * Le nom de la donnée
	 */
	private String		key;
	
	/**
	 * La donnée complète ou partielle (en fonction de l'action)
	 * => Normalement sous format JSON
	 */
	private String	 	data;
	
	@Override
	public void read(ByteInputStream input) throws IOException {
		type   = DataType.getType(input.readInt());
		action = DataAction.getAction(input.readInt());
		key	   = input.readUTF();
		data   = input.readUTF();
	}

	@Override
	public void write(ByteOutputStream output) throws IOException {
		output.writeInt(type.getId());
		output.writeInt(action.getId());
		output.writeUTF(key);
		output.writeUTF(data);
	}

	@Override
	public void handle(PacketHandler handler) throws Exception {
		handler.handle(this);
	}
	
	@Getter public enum DataAction {
		REQUEST(1),
		SEND(2),
		MODIFICATION(3);

		private final int id;

		private DataAction(int id){
			this.id 		 = id;
		}

		public static DataAction getAction(int id){
			for(final DataAction action : values()){
				if(action.getId() == id)
					return action;
			}

			return null;
		}
	}
	
	@Getter public enum DataType {
		PLAYER(1),
		IP(2),
		PERMISSION(3),
		COMMANDS(4),
		SERVERS(5),
		PLAYERS(6),
		MOTD(7),
		PLAYER_NUMBER(8);

		private final int id;

		private DataType(int id){
			this.id 		 = id;
		}

		public static DataType getType(int id){
			for(final DataType type : values()){
				if(type.getId() == id)
					return type;
			}

			return null;
		}
	}
}
