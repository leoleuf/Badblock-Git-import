package fr.badblock.protocol.packets.matchmaking;

import java.io.IOException;

import fr.badblock.protocol.PacketHandler;
import fr.badblock.protocol.buffers.ByteInputStream;
import fr.badblock.protocol.buffers.ByteOutputStream;
import fr.badblock.protocol.packets.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Packet permettant à un serveur d'indiquer qu'un joueur veux jouer
 * @author LeLanN
 */
@Data@NoArgsConstructor@AllArgsConstructor
public class PacketMatchmakingJoin implements Packet {
	private String serverName;
	private String playerName;
	
	@Override
	public void read(ByteInputStream input) throws IOException {
		serverName = input.readUTF();
		playerName = input.readUTF();
	}
	
	@Override
	public void write(ByteOutputStream output) throws IOException {
		output.writeUTF(serverName);
		output.writeUTF(playerName);
	}
	
	@Override
	public void handle(PacketHandler handler) throws Exception {
		handler.handle(this);
	}
}
