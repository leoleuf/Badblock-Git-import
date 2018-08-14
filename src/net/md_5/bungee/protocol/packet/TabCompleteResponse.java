package net.md_5.bungee.protocol.packet;

import java.util.LinkedList;
import java.util.List;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.md_5.bungee.protocol.AbstractPacketHandler;
import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.ProtocolConstants;

@ToString
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TabCompleteResponse extends DefinedPacket
{

    private int transactionId;
    private int start;
    private int length;
    private List<CommandMatch> matches;
    private List<String> commands;

    public TabCompleteResponse(int transactionId, int start, int length, List<CommandMatch> matches)
    {
        this.transactionId = transactionId;
        this.start = start;
        this.length = length;
        this.matches = matches;
    }

    public TabCompleteResponse(List<String> commands)
    {
        this.commands = commands;
    }

    @Override
    public void read(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion)
    {
        if ( protocolVersion >= ProtocolConstants.MINECRAFT_1_13 )
        {
            transactionId = readVarInt( buf );
            start = readVarInt( buf );
            length = readVarInt( buf );

            int cnt = readVarInt( buf );
            matches = new LinkedList<>();
            for ( int i = 0; i < cnt; i++ )
            {
                String match = readString( buf );
                String tooltip = buf.readBoolean() ? readString( buf ) : null;

                matches.add( new CommandMatch( match, tooltip ) );
            }
        }

        if ( protocolVersion < ProtocolConstants.MINECRAFT_1_13 )
        {
            commands = readStringArray( buf );
        }
    }

    @Override
    public void write(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion)
    {
        if ( protocolVersion >= ProtocolConstants.MINECRAFT_1_13 )
        {
            writeVarInt( transactionId, buf );
            writeVarInt( start, buf );
            writeVarInt( length, buf );

            writeVarInt( matches.size(), buf );
            for ( CommandMatch match : matches )
            {
                writeString( match.match, buf );
                buf.writeBoolean( match.tooltip != null );
                writeString( match.tooltip, buf );
            }
        }

        if ( protocolVersion < ProtocolConstants.MINECRAFT_1_13 )
        {
            writeStringArray( commands, buf );
        }
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception
    {
        handler.handle( this );
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommandMatch
    {

        private String match;
        private String tooltip;
    }
}