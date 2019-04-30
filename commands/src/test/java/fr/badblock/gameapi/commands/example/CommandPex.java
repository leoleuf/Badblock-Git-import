package fr.badblock.gameapi.commands.example;

import fr.badblock.gameapi.commands.CommandNode;

public class CommandPex extends CommandNode<ExampleReceiver>
{
	public CommandPex()
	{
		super("pex", "pex");

		addSubCommand(new CommandPexUser());
		addSubCommand(new CommandPexGroups());
	}

}
