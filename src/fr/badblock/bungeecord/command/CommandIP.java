package fr.badblock.bungeecord.command;

import fr.badblock.bungeecord.api.ChatColor;
import fr.badblock.bungeecord.api.CommandSender;
import fr.badblock.bungeecord.api.ProxyServer;
import fr.badblock.bungeecord.api.connection.ProxiedPlayer;

@SuppressWarnings("deprecation")
public class CommandIP extends PlayerCommand
{

    public CommandIP()
    {
        super( "ip", "bungeecord.command.ip" );
    }

    @Override
    public void execute(CommandSender sender, String[] args)
    {
        if ( args.length < 1 )
        {
            sender.sendMessage( ChatColor.RED + "Please follow this command by a user name" );
            return;
        }
        ProxiedPlayer user = ProxyServer.getInstance().getPlayer( args[0] );
        if ( user == null )
        {
            sender.sendMessage( ChatColor.RED + "That user is not online" );
        } else
        {
            sender.sendMessage( ChatColor.BLUE + "IP of " + args[0] + " is " + user.getAddress() );
        }
    }
}
