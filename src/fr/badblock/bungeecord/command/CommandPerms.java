package fr.badblock.bungeecord.command;

import java.util.HashSet;
import java.util.Set;

import fr.badblock.bungeecord.Util;
import fr.badblock.bungeecord.api.ChatColor;
import fr.badblock.bungeecord.api.CommandSender;
import fr.badblock.bungeecord.api.ProxyServer;
import fr.badblock.bungeecord.api.plugin.Command;

public class CommandPerms extends Command
{

    public CommandPerms()
    {
        super( "perms" );
    }

    @SuppressWarnings("deprecation")
	@Override
    public void execute(CommandSender sender, String[] args)
    {
        Set<String> permissions = new HashSet<>();
        for ( String group : sender.getGroups() )
        {
            permissions.addAll( ProxyServer.getInstance().getConfigurationAdapter().getPermissions( group ) );
        }
        sender.sendMessage( ChatColor.GOLD + "You have the following groups: " + Util.csv( sender.getGroups() ) );

        for ( String permission : permissions )
        {
            sender.sendMessage( ChatColor.BLUE + "- " + permission );
        }
    }
}
