package fr.badblock.bungeecord.command;

import java.util.Collection;

import fr.badblock.bungeecord.api.ChatColor;
import fr.badblock.bungeecord.api.CommandSender;
import fr.badblock.bungeecord.api.ProxyServer;
import fr.badblock.bungeecord.api.plugin.Command;
import fr.badblock.bungeecord.api.plugin.Plugin;

public class CommandPlugins extends Command
{

    public CommandPlugins()
    {
        super( "gplugins", "bungeecord.command.plugins", "gpl" );
    }

    @SuppressWarnings("deprecation")
	@Override
    public void execute(CommandSender sender, String[] args) {
    	Collection<Plugin> pl = ProxyServer.getInstance().getPluginManager().getPlugins();
    	String result = ChatColor.WHITE + "Plugins (" + ChatColor.RED + pl.size() + ChatColor.WHITE + "): " ;
    	boolean first = true;
    	
    	for(Plugin plugin : pl){
    		if(!first){
    			result += ChatColor.WHITE + ", " + ChatColor.GREEN;
    		} else {
    			result += ChatColor.WHITE + " " + ChatColor.GREEN;
    			first = false;
    		}
    		result += plugin.getDescription().getName();
    	}
    	
    	sender.sendMessage(result);
    }
}