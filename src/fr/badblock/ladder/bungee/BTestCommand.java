package fr.badblock.ladder.bungee;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class BTestCommand extends Command {

	public BTestCommand() {
		super("btest", "ladder.command.btest");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		System.out.println("Count: " + LadderBungee.getInstance().bungeePlayers.size());
	}

}
