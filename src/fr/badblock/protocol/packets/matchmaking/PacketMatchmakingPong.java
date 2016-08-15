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
 * Réponse de Ladder au ping. Renvoit le nombre de joueurs.
 * @author LeLanN
 */
@Data@NoArgsConstructor@AllArgsConstructor
public class PacketMatchmakingPong implements Packet {
	private int id;
	private int playerCount;

	@Override
	public void read(ByteInputStream input) throws IOException {
		id 			= input.readInt();
		playerCount = input.readInt();
	}

	@Override
	public void write(ByteOutputStream output) throws IOException {
		output.writeInt(id);
		output.writeInt(playerCount);
	}

	@Override
	public void handle(PacketHandler handler) throws Exception {
		handler.handle(this);
	}
}
