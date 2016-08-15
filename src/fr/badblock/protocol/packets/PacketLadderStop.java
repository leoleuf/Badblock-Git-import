package fr.badblock.protocol.packets;

import java.io.IOException;

import fr.badblock.protocol.PacketHandler;
import fr.badblock.protocol.buffers.ByteInputStream;
import fr.badblock.protocol.buffers.ByteOutputStream;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Packet d'extinction de Ladder
 * @author LeLanN
 */
@Data@NoArgsConstructor
public class PacketLadderStop implements Packet {
	@Override public void read(ByteInputStream input) throws IOException {}

	@Override
	public void write(ByteOutputStream output) throws IOException {}

	@Override
	public void handle(PacketHandler handler) throws Exception {
		handler.handle(this);
	}

}
