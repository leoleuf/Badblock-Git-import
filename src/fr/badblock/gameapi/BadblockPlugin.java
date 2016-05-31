package fr.badblock.gameapi;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Classe commune � tous les plugins utilisant l'API (poss�de des m�thodes permettant d'aller plus rapidement).
 * @author LeLanN
 */
public abstract class BadblockPlugin extends JavaPlugin {
	public GameAPI getAPI(){
		return GameAPI.getAPI();
	}
}
