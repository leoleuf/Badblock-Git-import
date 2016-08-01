package fr.badblock.ladder.commands;

import fr.badblock.ladder.Proxy;
import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.chat.ChatColor;
import fr.badblock.ladder.api.commands.Command;
import fr.badblock.ladder.api.entities.CommandSender;
import fr.badblock.ladder.api.utils.StringUtils;

public class CommandAlert extends Command {
	
	public CommandAlert() {
		super("lalert", "ladder.command.alert", "alert");
	}

	@Override
	public void executeCommand(CommandSender sender, String[] args) {
		if (args.length == 0) return;
		
		String message = StringUtils.join(args, " ");
		
		if (message.startsWith("&h")) {
			message = message.substring(2);
		}else{
			message = Proxy.getInstance().getAlertPrefix() + message;
		}
		
		message = ChatColor.replaceColor(message);
		Ladder.getInstance().broadcast(message);
		
	}
}
