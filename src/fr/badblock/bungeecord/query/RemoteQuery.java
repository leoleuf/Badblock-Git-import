package fr.badblock.bungeecord.query;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import java.net.InetSocketAddress;

import fr.badblock.bungeecord.api.ProxyServer;
import fr.badblock.bungeecord.api.config.ListenerInfo;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RemoteQuery
{

    private final ProxyServer bungee;
    private final ListenerInfo listener;

    public void start(Class<? extends Channel> channel, InetSocketAddress address, EventLoopGroup eventLoop, ChannelFutureListener future)
    {
        new Bootstrap()
                .channel( channel )
                .group( eventLoop )
                .handler( new QueryHandler( bungee, listener ) )
                .localAddress( address )
                .bind().addListener( future );
    }
}
