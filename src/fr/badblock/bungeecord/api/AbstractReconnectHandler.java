package fr.badblock.bungeecord.api;

import com.google.common.base.Preconditions;

import fr.badblock.bungeecord.api.config.ServerInfo;
import fr.badblock.bungeecord.api.connection.PendingConnection;
import fr.badblock.bungeecord.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ReconnectHandler;

public abstract class AbstractReconnectHandler implements ReconnectHandler
{

    @SuppressWarnings("deprecation")
	@Override
    public ServerInfo getServer(ProxiedPlayer player)
    {
        ServerInfo server = getForcedHost( player.getPendingConnection() );
        if ( server == null )
        {
            server = getStoredServer( player );
            if ( server == null )
            {
                server = ProxyServer.getInstance().getServerInfo( player.getPendingConnection().getListener().getDefaultServer() );
            }

            Preconditions.checkState( server != null, "Default server not defined" );
        }

        return server;
    }

    @SuppressWarnings("deprecation")
	public static ServerInfo getForcedHost(PendingConnection con)
    {
        if ( con.getVirtualHost() == null )
        {
            return null;
        }

        String forced = con.getListener().getForcedHosts().get( con.getVirtualHost().getHostString() );

        if ( forced == null && con.getListener().isForceDefault() )
        {
            forced = con.getListener().getDefaultServer();
        }
        return ProxyServer.getInstance().getServerInfo( forced );
    }

    protected abstract ServerInfo getStoredServer(ProxiedPlayer player);
}
