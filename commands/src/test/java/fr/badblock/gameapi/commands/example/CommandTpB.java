package fr.badblock.gameapi.commands.example;

import fr.badblock.gameapi.commands.ArgumentList;
import fr.badblock.gameapi.commands.CommandNode;

public class CommandTpB extends CommandNode<ExampleReceiver>
{
	public CommandTpB()
	{
		super("tp", "tp a b");

		addWordArgument("target");
		addWordArgument("destination");

		setExecutable(this::execute);
	}

	public int execute(ArgumentList<ExampleReceiver> list)
	{
		ExampleReceiver receiver = list.getSource();

		String target = list.get("target", String.class);
		String destination = list.get("destination", String.class);

		receiver.sVal = target + " to " + destination;

		return 1;
	}
}