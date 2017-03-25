package fr.badblock.ladder.plugins.others.commands.mod.punish;

import fr.badblock.ladder.api.commands.Command;
import fr.badblock.ladder.api.entities.CommandSender;

public abstract class SanctionCommand extends Command {

	public SanctionCommand(String command, String permission, String... aliases) {
		super(command, permission, aliases);
	}

	public SanctionCommand(String command) {
		super(command);
	}

	public abstract void run(CommandSender sender, String[] args);

	@Override
	public void executeCommand(CommandSender sender, String[] args) {
		this.ron(sender, args, false);
	}

	public void ron(CommandSender sender, String[] args, boolean force) {
		if (!force) {
			if (!sender.hasPermission(this.getPermission())) {
				sender.sendMessage("§cVous n'avez pas la permission d'utiliser cette commande.");
				return;
			}
		}
		run(sender, args);
	}

	public void ron(CommandSender sender, String... args) {
		this.ron(sender, args, true);
	}

}
