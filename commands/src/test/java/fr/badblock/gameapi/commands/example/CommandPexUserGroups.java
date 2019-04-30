package fr.badblock.gameapi.commands.example;

import fr.badblock.gameapi.commands.ArgumentList;
import fr.badblock.gameapi.commands.CommandNode;

public class CommandPexUserGroups extends CommandNode<ExampleReceiver>
{
	public CommandPexUserGroups()
	{
		super("groups", "pex user <user> groups");

		this.setExecutable(this::execute);
	}

	public int execute(ArgumentList<ExampleReceiver> arg)
	{
		ExampleReceiver to = arg.getSource();

		String user = arg.get("user", String.class);
		to.sVal = String.format("%s groups: a, b", user);

		return 1;
	}
}