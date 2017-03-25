package fr.badblock.bungeecord.plugins.others.commands;

import fr.badblock.bungeecord.api.CommandSender;
import fr.badblock.bungeecord.api.plugin.Command;
import fr.badblock.bungeecord.plugins.others.BadBlockBungeeOthers;

public class DoneCommand extends Command {
	public DoneCommand() {
		super("done", "badblock.done");
	}

	public void execute(CommandSender sender, String[] args) {
		BadBlockBungeeOthers.getInstance().setDone(true);
	}
}