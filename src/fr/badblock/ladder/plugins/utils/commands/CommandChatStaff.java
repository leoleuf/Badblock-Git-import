package fr.badblock.ladder.plugins.utils.commands;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.chat.ChatColor;
import fr.badblock.ladder.api.commands.Command;
import fr.badblock.ladder.api.entities.CommandSender;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.ladder.api.utils.StringUtils;

public class CommandChatStaff extends Command {
	public CommandChatStaff() {
		super("cs", "ladder.command.chatstaff");
	}

	@Override
	public void executeCommand(CommandSender sender, String[] args) {
		if(args.length == 0) {
			sender.sendMessage(ChatColor.RED + "Veuillez préciser le message !");
			return;
		}
		
		String message = ChatColor.GOLD + "[ChatStaff ~ " + sender.getName() + "] " + ChatColor.AQUA + StringUtils.join(args, " ");
		for(Player player : Ladder.getInstance().getOnlinePlayers()){
			if(player.hasPermission("ladder.command.chatstaff"))
				player.sendMessage(message);
		}
	}
}
