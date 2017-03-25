package fr.badblock.bungeecord.protocol.packet;

import io.netty.buffer.ByteBuf;
import java.util.List;

import fr.badblock.bungeecord.protocol.AbstractPacketHandler;
import fr.badblock.bungeecord.protocol.DefinedPacket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TabCompleteResponse extends DefinedPacket
{

    private List<String> commands;

    @Override
    public void read(ByteBuf buf)
    {
        commands = readStringArray( buf );
    }

    @Override
    public void write(ByteBuf buf)
    {
        writeStringArray( commands, buf );
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception
    {
        handler.handle( this );
    }
}
