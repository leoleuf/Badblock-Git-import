package fr.badblock.bungeecord;

import java.net.InetSocketAddress;

import com.google.common.base.Preconditions;

import fr.badblock.bungeecord.api.chat.BaseComponent;
import fr.badblock.bungeecord.api.connection.Server;
import fr.badblock.bungeecord.netty.ChannelWrapper;
import fr.badblock.bungeecord.protocol.DefinedPacket;
import fr.badblock.bungeecord.protocol.packet.PluginMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class ServerConnection implements Server
{

    @Getter
    private final ChannelWrapper ch;
    @Getter
    private final BungeeServerInfo info;
    @Getter
    @Setter
    private boolean isObsolete;
    @Getter
    private final boolean forgeServer = false;

    private final Unsafe unsafe = new Unsafe()
    {
        @Override
        public void sendPacket(DefinedPacket packet)
        {
            ch.write( packet );
        }
    };

    @Override
    public void sendData(String channel, byte[] data)
    {
        unsafe().sendPacket( new PluginMessage( channel, data, forgeServer ) );
    }

    @Override
    public void disconnect(String reason)
    {
        disconnect();
    }

    @Override
    public void disconnect(BaseComponent... reason)
    {
        Preconditions.checkArgument( reason.length == 0, "Server cannot have disconnect reason" );

        ch.delayedClose( null );
    }

    @Override
    public void disconnect(BaseComponent reason)
    {
        disconnect();
    }

    @Override
    public InetSocketAddress getAddress()
    {
        return getInfo().getAddress();
    }

    @Override
    public boolean isConnected()
    {
        return !ch.isClosed();
    }

    @Override
    public Unsafe unsafe()
    {
        return unsafe;
    }
}
