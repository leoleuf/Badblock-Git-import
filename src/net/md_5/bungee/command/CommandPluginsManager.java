package net.md_5.bungee.command;

import java.io.File;
import java.util.logging.Level;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginDescription;

public class CommandPluginsManager extends Command
{

	public CommandPluginsManager()
	{
		super( "gpluginsmanager", "bungeecord.command.pluginsmanager", "gplm" );
	}
	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		ProxyServer.getInstance().getPluginManager().getPlugins();
		if(args.length == 0){
			sender.sendMessage(ChatColor.GREEN + "Utilisation : /gplm load|unload|reload <nom>");
		} else if(args[0].equalsIgnoreCase("load")){
			if(args.length == 1){
				sender.sendMessage(ChatColor.RED + "Utilisation : /gplm load <nom>");
				return;
			}
			File f = new File("plugins/" + args[1] + ".jar");
			if(!f.exists()){
				sender.sendMessage(ChatColor.RED + args[1] + ".jar introuvable !");
				return;
			} else if(f.isDirectory()){
				sender.sendMessage(ChatColor.RED + args[1] + ".jar est un dossier !");
				return;
			}
			PluginDescription pluginDescription = null;
			try {
				pluginDescription = ProxyServer.getInstance().getPluginManager().loadPlugin(f);
				ProxyServer.getInstance().getPluginManager().loadPlugins();
				ProxyServer.getInstance().getPluginManager().getPlugin(pluginDescription.getName()).onEnable();
			} catch(Throwable e){
				ProxyServer.getInstance().getLogger().log(Level.SEVERE, "Impossible de charger " + args[1] + ".jar", e);
				sender.sendMessage(ChatColor.RED + "Impossible de charger le plugin, regardez les logs pour plus d'informations.");
			}
			sender.sendMessage(ChatColor.GREEN + pluginDescription.getName() + " version " + pluginDescription.getVersion() + " par " + pluginDescription.getAuthor() + " a été chargé !");
		} else if(args[0].equalsIgnoreCase("unload")){
			if(args.length == 1){
				sender.sendMessage(ChatColor.RED + "Utilisation : /gplm unload <nom>");
				return;
			}
			Plugin p = ProxyServer.getInstance().getPluginManager().getPlugin(args[1]);
			try {
				ProxyServer.getInstance().getPluginManager().unloadPlugin(p);
			} catch(Throwable e){
				ProxyServer.getInstance().getLogger().log(Level.SEVERE, "Impossible de désactiver " + args[1] + ".jar", e);
				sender.sendMessage(ChatColor.RED + "Impossible de désactiver le plugin, regardez les logs pour plus d'informations.");
			}
		} else if(args[0].equalsIgnoreCase("reload")){
			if(args.length == 1){
				sender.sendMessage(ChatColor.RED + "Utilisation : /gplm reload <nom>");
				return;
			}
			Plugin p = ProxyServer.getInstance().getPluginManager().getPlugin(args[1]);
			File f = p.getFile();
			PluginDescription pluginDescription = null;
			try {
				ProxyServer.getInstance().getPluginManager().unloadPlugin(p);
				pluginDescription = ProxyServer.getInstance().getPluginManager().loadPlugin(f);
				ProxyServer.getInstance().getPluginManager().loadPlugins();
				ProxyServer.getInstance().getPluginManager().getPlugin(pluginDescription.getName()).onEnable();
			} catch(Throwable e){
				ProxyServer.getInstance().getLogger().log(Level.SEVERE, "Impossible de recharger " + args[1] + ".jar", e);
				sender.sendMessage(ChatColor.RED + "Impossible de recharger le plugin, regardez les logs pour plus d'informations.");
			}
			sender.sendMessage(ChatColor.GREEN + pluginDescription.getName() + " version " + pluginDescription.getVersion() + " par " + pluginDescription.getAuthor() + " a été rechargé !");
		}
	}
}