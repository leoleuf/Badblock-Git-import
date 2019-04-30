package fr.badblock.gameapi.commands.example;

import fr.badblock.gameapi.commands.ArgumentList;
import fr.badblock.gameapi.commands.CommandNode;

public class CommandPexGroups extends CommandNode<ExampleReceiver>
{
	public CommandPexGroups()
	{
		super("groups", "pex groups");

		this.setExecutable(this::execute);
	}

	public int execute(ArgumentList<ExampleReceiver> arg)
	{
		ExampleReceiver to = arg.getSource();

		to.sVal = String.format("groups: a, b, c");

		return 1;
	}
}