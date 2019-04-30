package fr.badblock.gameapi.commands.example;

import fr.badblock.gameapi.commands.ArgumentList;
import fr.badblock.gameapi.commands.CommandNode;

public class CommandPexUserGroup extends CommandNode<ExampleReceiver>
{
	public CommandPexUserGroup()
	{
		super("group", "pex user <user> group");

		addSubCommand(new CommandPexUserGroupAdd());
		addSubCommand(new CommandPexUserGroupRemove());
	}
}

class CommandPexUserGroupAdd extends CommandNode<ExampleReceiver>
{
	public CommandPexUserGroupAdd()
	{
		super("add", "pex user <user> group add");

		addWordArgument("group");
		setExecutable(this::execute);
	}

	public int execute(ArgumentList<ExampleReceiver> arg)
	{
		ExampleReceiver receiver = arg.getSource();

		String user = arg.get("user", String.class);
		String group = arg.get("group", String.class);

		receiver.sVal = String.format("add group %s to %s", group, user);
		return 1;
	}
}

class CommandPexUserGroupRemove extends CommandNode<ExampleReceiver>
{
	public CommandPexUserGroupRemove()
	{
		super("remove", "pex user <user> group remove");

		addWordArgument("group");
		setExecutable(this::execute);
	}

	public int execute(ArgumentList<ExampleReceiver> arg)
	{
		ExampleReceiver receiver = arg.getSource();

		String user = arg.get("user", String.class);
		String group = arg.get("group", String.class);

		receiver.sVal = String.format("remove group %s to %s", group, user);
		return 1;
	}
}