package fr.badblock.common.protocol;

import fr.badblock.common.protocol.buffers.ByteInputStream;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class ProtocolPacket {

	ByteInputStream input;
	PacketHandler handler;
	Protocol protocol;
	
}
