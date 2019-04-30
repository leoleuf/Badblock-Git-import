package fr.badblock.gameapi.commands.example;

import fr.badblock.gameapi.commands.CommandNode;

public class CommandNoExecution extends CommandNode<ExampleReceiver>
{
	public CommandNoExecution()
	{
		super("noexec", "no execution");

		addGreedyStringArgument("hey");
	}
}