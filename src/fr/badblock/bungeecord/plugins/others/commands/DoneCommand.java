package fr.badblock.bungeecord.plugins.others.commands;

import fr.badblock.bungeecord.plugins.others.BadBlockBungeeOthers;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class DoneCommand extends Command {
	public DoneCommand() {
		super("done", "badblock.done");
	}

	public void execute(CommandSender sender, String[] args) {
		BadBlockBungeeOthers.getInstance().setDone(true);
	}
}