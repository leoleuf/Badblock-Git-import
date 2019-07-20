package fr.badblock.gameapi;

import org.bukkit.plugin.java.JavaPlugin;

import fr.badblock.gameapi.listeners.PlayerJoinListener;

public class GameAPI extends JavaPlugin
{
	@Override
	public void onEnable()
	{
		this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
	}
}