package fr.badblock.gameapi.commands.example;

import fr.badblock.gameapi.commands.ArgumentList;
import fr.badblock.gameapi.commands.CommandNode;

public class CommandPexUserPermission extends CommandNode<ExampleReceiver>
{
	public CommandPexUserPermission()
	{
		super("permission", "pex user <user> permission");

		addSubCommand(new CommandPexUserPermissionAdd());
		addSubCommand(new CommandPexUserPermissionRemove());
	}
}

class CommandPexUserPermissionAdd extends CommandNode<ExampleReceiver>
{
	public CommandPexUserPermissionAdd()
	{
		super("add", "pex user <user> permission add");

		addSimpleArgument("permission");
		setExecutable(this::execute);
	}

	public int execute(ArgumentList<ExampleReceiver> arg)
	{
		ExampleReceiver receiver = arg.getSource();

		String user = arg.get("user", String.class);
		String permission = arg.get("permission", String.class);

		receiver.sVal = String.format("add %s to %s", permission, user);
		return 1;
	}
}

class CommandPexUserPermissionRemove extends CommandNode<ExampleReceiver>
{
	public CommandPexUserPermissionRemove()
	{
		super("remove", "pex user <user> permission remove");

		addSimpleArgument("permission");
		setExecutable(this::execute);
	}

	public int execute(ArgumentList<ExampleReceiver> arg)
	{
		ExampleReceiver receiver = arg.getSource();

		String user = arg.get("user", String.class);
		String permission = arg.get("permission", String.class);

		receiver.sVal = String.format("remove %s to %s", permission, user);
		return 1;
	}
}