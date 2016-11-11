package fr.badblock.ladder.commands.punish;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.commands.Command;
import fr.badblock.ladder.api.entities.CommandSender;
import fr.badblock.ladder.api.utils.StringUtils;

public class CommandAdmin extends Command {
	public CommandAdmin() {
		super("admin", "ladder.command.admin", "adm");
	}

	@Override
	public void executeCommand(CommandSender sender, String[] args) {
		Ladder.getInstance().broadcast("§r[&6§l" + sender.getName() + "§r] §b" + StringUtils.join(args, " "));
	}
}