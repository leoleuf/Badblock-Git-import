package fr.badblock.bungeecord.protocol.packet;

import fr.badblock.bungeecord.protocol.AbstractPacketHandler;
import fr.badblock.bungeecord.protocol.DefinedPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Handshake extends DefinedPacket
{

    private int protocolVersion;
    private String host;
    private int port;
    private int requestedProtocol;

    @Override
    public void read(ByteBuf buf)
    {
        protocolVersion = readVarInt( buf );
        host = readString( buf );
        port = buf.readUnsignedShort();
        requestedProtocol = readVarInt( buf );
    }

    @Override
    public void write(ByteBuf buf)
    {
        writeVarInt( protocolVersion, buf );
        writeString( host, buf );
        buf.writeShort( port );
        writeVarInt( requestedProtocol, buf );
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception
    {
        handler.handle( this );
    }
}
