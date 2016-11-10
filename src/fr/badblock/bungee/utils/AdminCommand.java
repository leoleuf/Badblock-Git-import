package fr.badblock.bungee.utils;

import fr.badblock.bungee.rabbitconnector.RabbitPacketType;
import fr.badblock.ladder.bungee.LadderBungee;
import fr.badblock.utils.Encodage;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class AdminCommand extends Command {

	public AdminCommand() {
		super("admin", "bungeeutils.admin", "adm");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		LadderBungee.getInstance().rabbitService.sendPacket("admin_brodacast", "§r[&6§l" + sender.getName() + "§r] &8> §b" + StringUtils.join(args, " "), Encodage.UTF8, RabbitPacketType.PUBLISHER, 5000, false);
	}

}
