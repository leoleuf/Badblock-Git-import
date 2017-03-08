package fr.badblock.bungee.loaders;

public interface Loader {

	/**
	 * Load the loader
	 */
	public void load();
	
	/**
	 * Enable the loader
	 */
	public void enable();
	
	/**
	 * Disable the loader
	 */
	public void disable();
	
}
