package fr.badblock.bungeecord.plugins.others.commands;

import fr.badblock.bungeecord.plugins.ladder.LadderBungee;
import fr.badblock.common.protocol.packets.PacketPlayerQuit;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class FKickCommand extends Command {

	public FKickCommand() {
		super("fkick", "badblock.fkick");
	}

	@SuppressWarnings("deprecation")
	public void execute(CommandSender sender, String[] args) {
		if (args.length != 1) {
			sender.sendMessage("§cUsage: /fkick <pseudo>");
			return;
		}
		String playerName = args[0];
		PacketPlayerQuit packet = new PacketPlayerQuit(playerName, null);
		LadderBungee.getInstance().handle(packet, true);
		LadderBungee.getInstance().getClient().sendPacket(packet);
		sender.sendMessage("§aPacket de déconnexion envoyé à Ladder pour " + playerName + ".");
	}

}