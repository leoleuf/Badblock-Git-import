package fr.badblock.bungeecord.command;

import java.util.Collections;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import fr.badblock.bungeecord.api.ChatColor;
import fr.badblock.bungeecord.api.CommandSender;
import fr.badblock.bungeecord.api.ProxyServer;
import fr.badblock.bungeecord.api.config.ServerInfo;
import fr.badblock.bungeecord.api.connection.ProxiedPlayer;
import fr.badblock.bungeecord.api.plugin.Command;
import fr.badblock.bungeecord.api.plugin.TabExecutor;

public class CommandServer extends Command implements TabExecutor
{

    public CommandServer()
    {
        super( "server", "bungeecord.command.server" );
    }

    @SuppressWarnings("deprecation")
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
