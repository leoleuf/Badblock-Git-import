package fr.badblock.ladder.plugins.utils.commands;

import fr.badblock.ladder.api.chat.ChatColor;
import fr.badblock.ladder.api.commands.Command;
import fr.badblock.ladder.api.entities.CommandSender;
import fr.badblock.ladder.plugins.utils.LadderUtils;

public class CommandPic extends Command {
	private LadderUtils plugin;
	
	public CommandPic(LadderUtils plugin) {
		super("pic", "ladder.command.pic");
	
		this.plugin = plugin;
	}

	@Override
	public void executeCommand(CommandSender sender, String[] args) {
		sender.sendMessage(ChatColor.GREEN + "Le pic de connecté aujourd'hui est de : " + plugin.getPicBestToday());
		sender.sendMessage(ChatColor.GREEN + "Le record absolu est : " + plugin.getPicBest());
	}
}
