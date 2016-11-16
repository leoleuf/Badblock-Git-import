package fr.badblock.ladder.bungee;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class BTest extends Command {

	public BTest() {
		super("btest", "ladder.command.btest");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		sender.sendMessage("Count: " + LadderBungee.getInstance().bungeePlayers.parallelStream().filter(p -> p != null).mapToInt(p -> 1).sum());
	}

}
