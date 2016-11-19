package fr.badblock.protocol;

import fr.badblock.protocol.buffers.ByteInputStream;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class ProtocolPacket {

	ByteInputStream input;
	PacketHandler handler;
	Protocol protocol;
	
}
