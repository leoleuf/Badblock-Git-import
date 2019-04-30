package fr.badblock.gameapi.commands.spigot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public class OldCommand extends Command
{
	protected OldCommand(CommandSpigot cmd)
	{
		super(cmd.getName(), cmd.getDescription(), null, new ArrayList<>());
	}

	private String getCommand(String commandLabel, String[] args)
	{
		StringBuilder cmd = new StringBuilder(commandLabel);

		for (String arg : args)
		{
			cmd.append(" ");
			cmd.append(arg);
		}

		return cmd.toString();
	}
	
	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args)
	{
		try
		{
			RegistryOld.dispatcher.execute(getCommand(commandLabel, args), sender);
		}
		catch (CommandSyntaxException e)
		{
			sender.sendMessage(e.getMessage());
		}

		return true;
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException
	{
		ParseResults<Object> result = RegistryOld.dispatcher.parse(getCommand(alias, args), sender);

		try
		{
			return RegistryOld.dispatcher.getCompletionSuggestions(result)
						.get().getList().stream().map(s -> s.getText())
						.collect(Collectors.toList());
		}
		catch (InterruptedException | ExecutionException e)
		{
			e.printStackTrace();
			return new ArrayList<>();
		}
	}
}
