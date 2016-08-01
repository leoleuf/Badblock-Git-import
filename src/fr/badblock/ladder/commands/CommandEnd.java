package fr.badblock.ladder.commands;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.chat.ChatColor;
import fr.badblock.ladder.api.commands.Command;
import fr.badblock.ladder.api.entities.CommandSender;

public class CommandEnd extends Command {
	public CommandEnd() {
		super("lend", "ladder.command.end");
	}

	@Override
	public void executeCommand(CommandSender sender, String[] args) {
		sender.sendMessage(ChatColor.GREEN + "Ladder va s'éteindre ... Bye !");
		Ladder.getInstance().stopLadder();
	}
}
