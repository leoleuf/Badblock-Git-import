package fr.badblock.gameapi.commands.spigot;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;

import com.mojang.brigadier.CommandDispatcher;

public class RegistryNew extends Registry
{
	private CommandDispatcher<Object> dispatcher;

	@SuppressWarnings("unchecked")
	public RegistryNew() throws Exception
	{
		Object server = Bukkit.class.getClassLoader().loadClass("net.minecraft.server.v1_13_R2.MinecraftServer")
						.getMethod("getServer").invoke(null);

		Object dispatcher = server.getClass().getField("commandDispatcher").get(server);

		Field f = dispatcher.getClass().getDeclaredField("b");
		f.setAccessible(true);

		dispatcher = (CommandDispatcher<Object>) f.get(dispatcher);
	}
	
	@Override
	public void register(CommandSpigot command)
	{
		this.dispatcher.register(command.createCommand());
	}
}
