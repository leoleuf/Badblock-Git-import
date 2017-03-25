package fr.badblock.bungee.utils;

import fr.badblock.ladder.bungee.LadderBungee;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class AddPlayerCommand extends Command {

	public AddPlayerCommand() {
		super("addp", "bungeeutils.addplayer", "addplayer");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length == 0) return;
		LadderBungee.getInstance().totalPlayers.add(args[0]);
	}

}
