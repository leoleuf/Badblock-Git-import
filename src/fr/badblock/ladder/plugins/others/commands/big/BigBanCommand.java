package fr.badblock.ladder.plugins.others.commands.big;

import fr.badblock.ladder.api.commands.Command;
import fr.badblock.ladder.api.entities.CommandSender;
import fr.badblock.ladder.plugins.others.commands.mod.punish.CommandBan;
import fr.badblock.ladder.plugins.others.commands.mod.punish.CommandBanip;

public class BigBanCommand extends Command {

	public BigBanCommand() {
		super("bigban", "guardian.modo", "bban", "bba");
	}

	@Override
	public void executeCommand(CommandSender sender, String[] args) {
		new Thread() {
			@Override
			public void run() {
				CommandBan.instance.executeCommand(sender, args);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				CommandBanip.instance.executeCommand(sender, args);
			}
		}.start();
	}

}
