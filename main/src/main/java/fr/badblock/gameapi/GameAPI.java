package fr.badblock.gameapi;

import org.bukkit.plugin.java.JavaPlugin;

import fr.badblock.gameapi.commands.Command;

public class GameAPI extends JavaPlugin
{
	@Override
	public void onEnable()
	{
		Command.test();
	}
}