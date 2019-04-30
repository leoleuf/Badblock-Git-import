package fr.badblock.gameapi.commands.example;

import fr.badblock.gameapi.commands.ArgumentList;
import fr.badblock.gameapi.commands.CommandNode;

public class CommandAdd extends CommandNode<ExampleReceiver>
{
	public CommandAdd()
	{
		super("add", "ajoute deux entiers");

		addIntegerArgument("a");
		addIntegerArgument("b");
		setExecutable(this::execute);
	}

	public int execute(ArgumentList<ExampleReceiver> list)
	{
		int a = list.get("a", Integer.class);
		int b = list.get("b", Integer.class);

		list.getSource().val = a + b;
		
		return 1;
	}
}
