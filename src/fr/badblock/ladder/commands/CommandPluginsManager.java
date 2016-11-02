package fr.badblock.ladder.commands;

import java.io.File;
import java.util.logging.Level;

import fr.badblock.ladder.Proxy;
import fr.badblock.ladder.api.chat.ChatColor;
import fr.badblock.ladder.api.commands.Command;
import fr.badblock.ladder.api.entities.CommandSender;
import fr.badblock.ladder.api.plugins.Plugin;
import fr.badblock.ladder.api.plugins.Plugin.PluginInfo;
import fr.badblock.ladder.api.plugins.PluginsManager;

public class CommandPluginsManager extends Command {

	public CommandPluginsManager() {
		super("lpluginsmanager", "ladder.command.pluginsmanager", "lplm" );
	}

	@Override
	public void executeCommand(CommandSender sender, String[] args) {
		Proxy ladder = Proxy.getInstance();
		PluginsManager pluginManager = ladder.getPluginsManager();
		pluginManager.getPlugins();
		if(args.length == 0){
			sender.sendMessage(ChatColor.GREEN + "Utilisation : /lplm load|unload|reload <nom>");
		} else if(args[0].equalsIgnoreCase("load")){
			if(args.length == 1){
				sender.sendMessage(ChatColor.RED + "Utilisation : /lplm load <nom>");
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
			PluginInfo pluginInfo = pluginManager.detectPlugin(f);
			try {
				pluginManager.loadPlugin(pluginInfo);
				Plugin plugin = pluginManager.getPlugin(pluginInfo);
				pluginManager.enablePlugin(plugin);
				sender.sendMessage(ChatColor.GREEN + pluginInfo.getName() + " version " + pluginInfo.getVersion() + " a été chargé !");
			} catch (Throwable e) {
				ladder.getLogger().log(Level.SEVERE, "Impossible de charger " + args[1] + ".jar", e);
				sender.sendMessage(ChatColor.RED + "Impossible de charger le plugin, regardez les logs pour plus d'informations.");
			}
		} else if(args[0].equalsIgnoreCase("unload")){
			if(args.length == 1){
				sender.sendMessage(ChatColor.RED + "Utilisation : /lplm unload <nom>");
				return;
			}
			Plugin plugin = pluginManager.getPlugin(args[1]);
			try {
				pluginManager.disablePlugin(plugin);
				pluginManager.unloadPlugin(plugin);
				sender.sendMessage(ChatColor.GREEN + plugin.getPluginInfo().getName() + " version " + plugin.getPluginInfo().getVersion() + " a été déchargé !");
			} catch (Throwable e) {
				ladder.getLogger().log(Level.SEVERE, "Impossible de désactiver " + args[1] + ".jar", e);
				sender.sendMessage(ChatColor.RED + "Impossible de désactiver le plugin, regardez les logs pour plus d'informations.");
			}
		} else if(args[0].equalsIgnoreCase("reload")){
			if(args.length == 1){
				sender.sendMessage(ChatColor.RED + "Utilisation : /lplm reload <nom>");
				return;
			}
			Plugin p = pluginManager.getPlugin(args[1]);
			File f = p.getPluginInfo().getJarFile();
			try {
				pluginManager.disablePlugin(p);
				pluginManager.unloadPlugin(p);
				PluginInfo pluginInfo = pluginManager.detectPlugin(f);
				pluginManager.loadPlugin(pluginInfo);
				Plugin plugin = pluginManager.getPlugin(pluginInfo);
				pluginManager.enablePlugin(plugin);
				sender.sendMessage(ChatColor.GREEN + plugin.getPluginInfo().getName() + " version " + plugin.getPluginInfo().getVersion() + " a été rechargé !");
			} catch(Throwable e){
				ladder.getLogger().log(Level.SEVERE, "Impossible de recharger " + args[1] + ".jar", e);
				sender.sendMessage(ChatColor.RED + "Impossible de recharger le plugin, regardez les logs pour plus d'informations.");
			}
		}
	}
}