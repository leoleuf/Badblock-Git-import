package net.md_5.bungee.protocol.packet;

import java.util.List;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.md_5.bungee.protocol.AbstractPacketHandler;
import net.md_5.bungee.protocol.DefinedPacket;

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
