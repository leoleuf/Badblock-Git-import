package fr.badblock.ladder.plugins.utils.commands;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.chat.ChatColor;
import fr.badblock.ladder.api.commands.Command;
import fr.badblock.ladder.api.entities.CommandSender;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.ladder.api.utils.StringUtils;

public class CommandAdminChat extends Command {
	public CommandAdminChat() {
		super("ac", "ladder.command.adminchat");
	}

	@Override
	public void executeCommand(CommandSender sender, String[] args) {
		if(args.length == 0) {
			sender.sendMessage(ChatColor.RED + "Veuillez préciser le message !");
			return;
		}
		
		String message = ChatColor.RED + "[AdminChat ~ " + sender.getName() + "] " + ChatColor.AQUA + StringUtils.join(args, " ");
		for(Player player : Ladder.getInstance().getOnlinePlayers()){
			if(player.hasPermission("ladder.command.adminchat"))
				player.sendMessage(message);
		}
	}
}
