package net.md_5.bungee.command;

import java.util.Collection;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

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