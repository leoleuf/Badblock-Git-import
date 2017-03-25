package fr.badblock.bungeecord.protocol.packet;

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
public class Login extends DefinedPacket
{

    private int entityId;
    private short gameMode;
    private int dimension;
    private short difficulty;
    private short maxPlayers;
    private String levelType;
    private boolean reducedDebugInfo;

    @Override
    public void read(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion)
    {
        entityId = buf.readInt();
        gameMode = buf.readUnsignedByte();
        if ( protocolVersion > ProtocolConstants.MINECRAFT_1_9 )
        {
            dimension = buf.readInt();
        } else
        {
            dimension = buf.readByte();
        }
        difficulty = buf.readUnsignedByte();
        maxPlayers = buf.readUnsignedByte();
        levelType = readString( buf );
        if ( protocolVersion >= 29 )
        {
            reducedDebugInfo = buf.readBoolean();
        }
    }

    @Override
    public void write(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion)
    {
        buf.writeInt( entityId );
        buf.writeByte( gameMode );
        if ( protocolVersion > ProtocolConstants.MINECRAFT_1_9 )
        {
            buf.writeInt( dimension );
        } else
        {
            buf.writeByte( dimension );
        }
        buf.writeByte( difficulty );
        buf.writeByte( maxPlayers );
        writeString( levelType, buf );
        if ( protocolVersion >= 29 )
        {
            buf.writeBoolean( reducedDebugInfo );
        }
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception
    {
        handler.handle( this );
    }
}
