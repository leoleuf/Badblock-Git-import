package fr.badblock.ladder.plugins.others.commands.big;

import fr.badblock.ladder.api.commands.Command;
import fr.badblock.ladder.api.entities.CommandSender;
import fr.badblock.ladder.plugins.others.commands.mod.punish.CommandTempban;
import fr.badblock.ladder.plugins.others.commands.mod.punish.CommandTempbanip;

public class BigTempBanCommand extends Command {

	public BigTempBanCommand() {
		super("bigtempban", "guardian.modo", "btempban", "btb");
	}

	@Override
	public void executeCommand(CommandSender sender, String[] args) {
		new Thread() {
			@Override
			public void run() {
				try {
					CommandTempban.instance.executeCommand(sender, args);
					CommandTempbanip.instance.executeCommand(sender, args);
				}catch(Exception error) {
					error.printStackTrace();
				}
			}
		}.start();
	}

}
