package fr.badblock.common.protocol.packets;

import java.io.IOException;

import fr.badblock.common.protocol.PacketHandler;
import fr.badblock.common.protocol.buffers.ByteInputStream;
import fr.badblock.common.protocol.buffers.ByteOutputStream;
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
