package fr.badblock.ladder.plugins.others.commands.big;

import fr.badblock.ladder.api.commands.Command;
import fr.badblock.ladder.api.entities.CommandSender;
import fr.badblock.ladder.plugins.others.commands.mod.punish.CommandUnban;
import fr.badblock.ladder.plugins.others.commands.mod.punish.CommandUnbanip;

public class BigPardonCommand extends Command {

	public BigPardonCommand() {
		super("bigpardon", "guardian.modo", "bpardon", "bp");
	}

	@Override
	public void executeCommand(CommandSender sender, String[] args) {
		new Thread() {
			@Override
			public void run() {
				CommandUnban.instance.executeCommand(sender, args);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				CommandUnbanip.instance.executeCommand(sender, args);
			}
		}.start();
	}
}
