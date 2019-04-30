package fr.badblock.gameapi.commands.spigot;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;

import com.mojang.brigadier.CommandDispatcher;

public class RegistryOld extends Registry 
{
	public static final CommandDispatcher<Object> dispatcher = new CommandDispatcher<Object>();

	@Override
	public void register(CommandSpigot command)
	{
		OldCommand cmd = new OldCommand(command);

		try
		{
			Field f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
			f.setAccessible(true);
			Object map = f.get(Bukkit.getServer());

			map.getClass().getMethod("register", String.class, Command.class).invoke(map, "badblock", cmd);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
}
