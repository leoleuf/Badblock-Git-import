package fr.badblock.bungee.commands.linked;

import fr.badblock.bungee.BadBungee;
import fr.badblock.commons.technologies.rabbitmq.RabbitPacketType;
import fr.badblock.commons.utils.Encodage;
import fr.badblock.commons.utils.StringUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class SendToAllCommand extends Command {

	public SendToAllCommand() {
		super("sendtoall", "badbungee.sendtoall", "sta");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length < 1) {
			sender.sendMessage("§cUtilisation: /sendtoall <commande>");
			return;
		}
		String command = StringUtils.join(args, " ");
		BadBungee.getInstance().getRabbitService().sendPacket("bungee.worker.executeCommand", command, Encodage.UTF8, RabbitPacketType.PUBLISHER, 10000, false);
		sender.sendMessage("§aCommande forcée sur tous les bungee (" + command + ")");
	}

}
