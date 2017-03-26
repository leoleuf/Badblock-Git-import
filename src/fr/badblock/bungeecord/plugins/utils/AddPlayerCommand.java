package fr.badblock.bungeecord.plugins.utils;

import fr.badblock.bungeecord.api.CommandSender;
import fr.badblock.bungeecord.api.plugin.Command;
import fr.badblock.bungeecord.plugins.ladder.LadderBungee;

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
