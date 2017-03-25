package fr.badblock.ladder.plugins.others.commands;

import java.net.InetSocketAddress;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.commands.Command;
import fr.badblock.ladder.api.entities.CommandSender;

public class AddServerCommand extends Command {

	public AddServerCommand() {
		super("addserver", "others.addserver");
	}

	@Override
	public void executeCommand(CommandSender sender, String[] args) {
		try {
			if (args.length != 2) {
				sender.sendMessage("§cUtilisation: /addserver <name> <ip:port>");
				return;
			}
			if (Ladder.getInstance().getBukkitServer(args[0]) != null) {
				sender.sendMessage("§cCe serveur existe déjà.");
				return;
			}
			Ladder.getInstance().addBukkitServer(new InetSocketAddress(args[1].split(":")[0], Integer.parseInt(args[1].split(":")[1])), args[0]);
			sender.sendMessage("§aServeur '" + args[0] + "' ajouté!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
