package fr.badblock.gameapi.commands.example;

import fr.badblock.gameapi.commands.ArgumentList;
import fr.badblock.gameapi.commands.CommandNode;

public class CommandAddMultiple extends CommandNode<ExampleReceiver>
{
	public CommandAddMultiple()
	{
		super("add_m", "ajoute deux entiers");

		addIntegerArgument("a");
		addIntegerArgument("b");
		setExecutable(this::execute);
		addIntegerArgument("c");
		addIntegerArgument("d");
	}

	public int execute(ArgumentList<ExampleReceiver> list)
	{
		int a = list.get("a", Integer.class);
		int b = list.get("b", Integer.class);
		int c = list.get("c", Integer.class, 0);
		int d = list.get("d", Integer.class, 0);

		list.getSource().val = a + b + c + d;
		
		return 1;
	}
}