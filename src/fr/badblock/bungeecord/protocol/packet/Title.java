package fr.badblock.bungeecord.protocol.packet;

import fr.badblock.bungeecord.protocol.AbstractPacketHandler;
import fr.badblock.bungeecord.protocol.DefinedPacket;
import fr.badblock.bungeecord.protocol.ProtocolConstants;
import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Title extends DefinedPacket
{

    private Action action;

    // TITLE & SUBTITLE
    private String text;

    // TIMES
    private int fadeIn;
    private int stay;
    private int fadeOut;

    @SuppressWarnings("incomplete-switch")
	@Override
    public void read(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion)
    {
        int index = readVarInt( buf );

        // If we're working on 1.10 or lower, increment the value of the index so we pull out the correct value.
        if ( protocolVersion <= ProtocolConstants.MINECRAFT_1_10 && index >= 2 )
        {
            index++;
        }

        action = Action.values()[index];
        switch ( action )
        {
            case TITLE:
            case SUBTITLE:
            case ACTIONBAR:
                text = readString( buf );
                break;
            case TIMES:
                fadeIn = buf.readInt();
                stay = buf.readInt();
                fadeOut = buf.readInt();
                break;
        }
    }

    @SuppressWarnings("incomplete-switch")
	@Override
    public void write(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion)
    {
        int index = action.ordinal();

        // If we're working on 1.10 or lower, increment the value of the index so we pull out the correct value.
        if ( protocolVersion <= ProtocolConstants.MINECRAFT_1_10 && index >= 2 )
        {
            index--;
        }

        writeVarInt( index, buf );
        switch ( action )
        {
            case TITLE:
            case SUBTITLE:
            case ACTIONBAR:
                writeString( text, buf );
                break;
            case TIMES:
                buf.writeInt( fadeIn );
                buf.writeInt( stay );
                buf.writeInt( fadeOut );
                break;
        }
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception
    {
        handler.handle( this );
    }

    public static enum Action
    {

        TITLE,
        SUBTITLE,
        ACTIONBAR,
        TIMES,
        CLEAR,
        RESET
    }
}