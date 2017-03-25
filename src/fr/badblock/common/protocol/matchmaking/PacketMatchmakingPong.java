package fr.badblock.common.protocol.matchmaking;

import java.io.IOException;

import fr.badblock.common.protocol.PacketHandler;
import fr.badblock.common.protocol.buffers.ByteInputStream;
import fr.badblock.common.protocol.buffers.ByteOutputStream;
import fr.badblock.common.protocol.packets.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * R�ponse de Ladder au ping. Renvoit le nombre de joueurs.
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
