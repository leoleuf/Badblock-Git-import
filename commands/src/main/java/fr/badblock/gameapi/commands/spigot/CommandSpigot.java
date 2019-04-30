package fr.badblock.gameapi.commands.spigot;

import org.bukkit.command.CommandSender;

import fr.badblock.gameapi.commands.CommandNode;

/**
 * Commande ou sous-commande Spigot.
 * @author LeLanN
 */
public class CommandSpigot extends CommandNode<CommandSender>
{
	private String permission;

	public CommandSpigot(String name, String description)
	{
		super(name, description);
	}

	/**
	 * Définis la permission nécessaire pour éxécuter cette commande
	 * @param permission
	 */
	public void setPermission(String permission)
	{
		this.permission = permission;
	}

	@Override
	public boolean isAllowed(CommandSender source)
	{
		return super.isAllowed(source) && (permission == null || source.hasPermission(permission));
	}

	@Override
	public CommandSender getSource(Object obj)
	{
		if (obj instanceof CommandSender) // < 1.13
			return (CommandSender) obj;

		try
		{
			return (CommandSender) obj.getClass().getMethod("getBukkitSender").invoke(obj);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * Ajoute la commande à Spigot
	 */
	public void register()
	{
		Registry.registry.register(this);
	}
}
