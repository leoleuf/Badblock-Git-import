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
 * Permet à un serveur (Bukkit) de demander des informations sur le nombre de joueurs / parties
 * @author LeLanN
 */
@Data@NoArgsConstructor@AllArgsConstructor
public class PacketMatchmakingPing implements Packet {
	private int      id;
	private String[] servers;

	@Override
	public void read(ByteInputStream input) throws IOException {
		id 		= input.readInt();
		servers = input.readArrayUTF();
	}

	@Override
	public void write(ByteOutputStream output) throws IOException {
		output.writeInt(id);
		output.writeArrayUTF(servers);
	}

	@Override
	public void handle(PacketHandler handler) throws Exception {
		handler.handle(this);
	}
}
