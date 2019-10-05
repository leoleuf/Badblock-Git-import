package fr.badblock.tech.protocol;


import fr.badblock.tech.protocol.buffers.ByteInputStream;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class ProtocolPacket {

    ByteInputStream input;
    PacketHandler handler;
    Protocol protocol;

}


