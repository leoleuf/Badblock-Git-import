package fr.badblock.bungeecord.plugins.others.commands;

import fr.badblock.bungeecord.api.CommandSender;
import fr.badblock.bungeecord.api.plugin.Command;
import fr.badblock.ladder.bungee.LadderBungee;

public class BListCommand extends Command {
    public BListCommand() {
        super("blist");
    }

    @SuppressWarnings("deprecation")
	public void execute(CommandSender sender, String[] args) {
        sender.sendMessage("Count: " + LadderBungee.getInstance().bungeePlayerList.size());
    }
}