package fr.badblock.bungeecord.plugins.utils;

import fr.badblock.bungeecord.plugins.ladder.LadderBungee;
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
