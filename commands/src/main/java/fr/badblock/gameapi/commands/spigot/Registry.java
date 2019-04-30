package fr.badblock.gameapi.commands.spigot;

public abstract class Registry
{
	public static final Registry registry;

	static
	{
		Registry tmp;
		
		try
		{
			tmp = new RegistryNew();
		}
		catch (Exception e)
		{
			tmp = new RegistryOld();
		}
		
		registry = tmp;
	}

	public abstract void register(CommandSpigot command);
}
