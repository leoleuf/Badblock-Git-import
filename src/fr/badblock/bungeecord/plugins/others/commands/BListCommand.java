package fr.badblock.bungeecord.plugins.others.commands;

import fr.badblock.bungeecord.plugins.ladder.LadderBungee;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class BListCommand extends Command {
	public BListCommand() {
		super("blist");
	}

	@SuppressWarnings("deprecation")
	public void execute(CommandSender sender, String[] args) {
		sender.sendMessage("Count: " + LadderBungee.getInstance().bungeePlayerList.size());
	}
}