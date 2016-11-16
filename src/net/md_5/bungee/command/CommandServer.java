package net.md_5.bungee.command;

import java.util.Collections;
import java.util.Map;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

public class CommandServer extends Command implements TabExecutor
{

    public CommandServer()
    {
        super( "server", "bungeecord.command.server" );
    }

    @Override
    public void execute(CommandSender sender, String[] args)
    {
        if ( !( sender instanceof ProxiedPlayer ) )
        {
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) sender;
        Map<String, ServerInfo> servers = ProxyServer.getInstance().getServers();
        if ( args.length == 0 ) {
            player.sendMessage(ChatColor.DARK_AQUA + "Vous êtes connecté au serveur " + ChatColor.AQUA + player.getServer().getInfo().getName() + ChatColor.DARK_AQUA + ".");
        } else {
            ServerInfo server = servers.get( args[0] );
            if ( server == null ) {
                player.sendMessage(ChatColor.RED + "Le serveur spécifié est introuvable !");
            } else {
                player.connect( server );
            }
        }
    }

    @SuppressWarnings("unchecked")
	@Override
    public Iterable<String> onTabComplete(final CommandSender sender, String[] args)
    {
        return ( args.length != 0 ) ? Collections.EMPTY_LIST : Iterables.transform( Iterables.filter( ProxyServer.getInstance().getServers().values(), new Predicate<ServerInfo>()
        {
            @Override
            public boolean apply(ServerInfo input)
            {
                return input.canAccess( sender );
            }
        } ), new Function<ServerInfo, String>()
        {
            @Override
            public String apply(ServerInfo input)
            {
                return input.getName();
            }
        } );
    }
}
