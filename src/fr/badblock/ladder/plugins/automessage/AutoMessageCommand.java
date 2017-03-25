package fr.badblock.ladder.plugins.automessage;

import fr.badblock.ladder.api.commands.Command;
import fr.badblock.ladder.api.entities.CommandSender;

public class AutoMessageCommand extends Command {

	public AutoMessageCommand() {
		super("amreload", "ladder.command.amreload", "amr");
	}

	@Override
	public void executeCommand(CommandSender sender, String[] args) {
		AutoMessage autoMessage = AutoMessage.getInstance();
		autoMessage.reloadConfiguration();
		sender.sendMessage("§aConfiguration des messages rechargée !");
	}	
	
}
