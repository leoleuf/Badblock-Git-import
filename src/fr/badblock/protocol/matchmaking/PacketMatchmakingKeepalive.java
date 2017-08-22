package fr.badblock.protocol.matchmaking;

import java.io.IOException;

import fr.badblock.protocol.PacketHandler;
import fr.badblock.protocol.buffers.ByteInputStream;
import fr.badblock.protocol.buffers.ByteOutputStream;
import fr.badblock.protocol.packets.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Packet permettant � un serveur Bukkit d'indiquer qu'il si il est disponible ou non
 * @author LeLanN
 */
@Data@NoArgsConstructor@AllArgsConstructor
public class PacketMatchmakingKeepalive implements Packet {
	private ServerStatus status = ServerStatus.WAITING;
	private int			 players = 0;
	private int			 slots = 4;

	@Override
	public void read(ByteInputStream input) throws IOException {
	}

	@Override
	public void write(ByteOutputStream output) throws IOException {
	}

	@Override
	public void handle(PacketHandler handler) throws Exception {
		handler.handle(this);
	}
	
	@Getter public enum ServerStatus {
		WAITING(1),
		RUNNING(2),
		FINISHED(3),
		STOPING(4);

		private final int id;

		private ServerStatus(int id){
			this.id = id;
		}

		public static ServerStatus getStatus(int id){
			for(final ServerStatus status : values()){
				if(status.getId() == id)
					return status;
			}

			return null;
		}
	}
}
