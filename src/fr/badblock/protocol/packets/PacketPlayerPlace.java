package fr.badblock.protocol.packets;

import java.io.IOException;

import fr.badblock.protocol.PacketHandler;
import fr.badblock.protocol.buffers.ByteInputStream;
import fr.badblock.protocol.buffers.ByteOutputStream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Packet permettant de changer le serveur Spigot du joueur
 * @author LeLanN
 */
@Data@NoArgsConstructor@AllArgsConstructor
public class PacketPlayerPlace implements Packet {
	private String playerName;
	private String serverName;

	@Override
	public void read(ByteInputStream input) throws IOException {
		playerName	= input.readUTF();
		serverName  = input.readUTF();
	}

	@Override
	public void write(ByteOutputStream output) throws IOException {
		output.writeUTF(playerName);
		output.writeUTF(serverName);
	}

	@Override
	public void handle(PacketHandler handler) throws Exception {
		handler.handle(this);
	}
}
