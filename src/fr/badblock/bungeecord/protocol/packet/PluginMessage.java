package fr.badblock.bungeecord.protocol.packet;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;

import fr.badblock.bungeecord.protocol.AbstractPacketHandler;
import fr.badblock.bungeecord.protocol.DefinedPacket;
import fr.badblock.bungeecord.protocol.ProtocolConstants;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PluginMessage extends DefinedPacket
{

    public static final Predicate<PluginMessage> SHOULD_RELAY = new Predicate<PluginMessage>()
    {
        @Override
        public boolean apply(PluginMessage input)
        {
            return input.getTag().equals( "REGISTER" ) || input.getTag().equals( "MC|Brand" );
        }
    };
    //
    private String tag;
    private byte[] data;

    /**
     * Allow this packet to be sent as an "extended" packet.
     */
    private boolean allowExtendedPacket = false;

    @Override
    public void read(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion)
    {
        tag = readString( buf );
        int maxSize = direction == ProtocolConstants.Direction.TO_SERVER ? Short.MAX_VALUE : 0x100000;
        Preconditions.checkArgument( buf.readableBytes() < maxSize );
        data = new byte[ buf.readableBytes() ];
        buf.readBytes( data );
    }

    @Override
    public void write(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion)
    {
        writeString( tag, buf );
        buf.writeBytes( data );
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception
    {
        handler.handle( this );
    }

    public DataInput getStream()
    {
        return new DataInputStream( new ByteArrayInputStream( data ) );
    }
}