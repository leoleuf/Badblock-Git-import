package fr.badblock.bungee.loaders.main;

import net.md_5.bungee.api.plugin.Plugin;

/**
 * The main loader is working as a loader of syncLoader with Bungee functions (onLoad/onEnable/onDisable)
 * @author root
 */
public class MainLoader extends Plugin {

	private SyncLoader syncLoader = new SyncLoader();
	
	@Override
	public void onLoad() {
		syncLoader.load();
	}
	
	@Override
	public void onEnable() {
		syncLoader.enable();
	}
	
	@Override
	public void onDisable() {
		syncLoader.disable();
	}
	
}
