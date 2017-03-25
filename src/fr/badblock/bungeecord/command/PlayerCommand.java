package fr.badblock.bungeecord.command;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import fr.badblock.bungeecord.api.CommandSender;
import fr.badblock.bungeecord.api.ProxyServer;
import fr.badblock.bungeecord.api.connection.ProxiedPlayer;
import fr.badblock.bungeecord.api.plugin.Command;
import fr.badblock.bungeecord.api.plugin.TabExecutor;

/**
 * @deprecated internal use only
 */
@Deprecated
public abstract class PlayerCommand extends Command implements TabExecutor
{

    public PlayerCommand(String name)
    {
        super( name );
    }

    public PlayerCommand(String name, String permission, String... aliases)
    {
        super( name, permission, aliases );
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args)
    {
        final String lastArg = ( args.length > 0 ) ? args[args.length - 1].toLowerCase() : "";
        return Iterables.transform( Iterables.filter( ProxyServer.getInstance().getPlayers(), new Predicate<ProxiedPlayer>()
        {
            @Override
            public boolean apply(ProxiedPlayer player)
            {
                return player.getName().toLowerCase().startsWith( lastArg );
            }
        } ), new Function<ProxiedPlayer, String>()
        {
            @Override
            public String apply(ProxiedPlayer player)
            {
                return player.getName();
            }
        } );
    }
}
