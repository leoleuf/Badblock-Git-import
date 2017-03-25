package fr.badblock.ladder.plugins.others.commands;

import fr.badblock.ladder.api.commands.Command;
import fr.badblock.ladder.api.entities.CommandSender;

public class BrohoofCommand extends Command {


	public BrohoofCommand() {
		super(")");
	}

	@Override
	public void executeCommand(CommandSender sender, String[] args) {
		sender.sendMessage("§cBrohoof! /)(\\");
	}

}
