package fr.badblock.gameapi.commands.example;

import fr.badblock.gameapi.commands.ArgumentList;
import fr.badblock.gameapi.commands.CommandNode;

public class CommandTpA extends CommandNode<ExampleReceiver>
{
	public CommandTpA()
	{
		super("tp", "tp a");

		addWordArgument("destination");
		setExecutable(this::execute);
	}

	public int execute(ArgumentList<ExampleReceiver> list)
	{
		ExampleReceiver receiver = list.getSource();

		String destination = list.get("destination", String.class);
		receiver.sVal = "to " + destination;

		return 1;
	}
}