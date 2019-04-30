package fr.badblock.gameapi.commands.example;

import fr.badblock.gameapi.commands.CommandNode;

public class CommandPexUser extends CommandNode<ExampleReceiver>
{
	public CommandPexUser()
	{
		super("user", "pex user");

		addWordArgument("user");
		
		addSubCommand(new CommandPexUserPermission());
		addSubCommand(new CommandPexUserGroup());
		addSubCommand(new CommandPexUserGroups());
	}
}
