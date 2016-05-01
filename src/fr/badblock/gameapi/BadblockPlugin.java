package fr.badblock.gameapi;

import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Classe commune � tous les plugins utilisant l'API (poss�de des m�thodes permettant d'aller plus rapidement).
 * @author LeLanN
 */
public abstract class BadblockPlugin extends JavaPlugin {
	public void registerListener(Listener listener){
		getServer().getPluginManager().registerEvents(listener, this);
	}
	
	public void registerCommand(String name, CommandExecutor command){
		getServer().getPluginCommand(name).setExecutor(command); 
	}
	
	public GameAPI getAPI(){
		return GameAPI.getAPI();
	}
}
