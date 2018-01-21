package fr.badblock.bungeecord.plugins.utils;

import fr.badblock.bungeecord.plugins.ladder.LadderBungee;
import fr.badblock.common.protocol.packets.PacketPlayerChat;
import fr.badblock.common.protocol.packets.PacketPlayerChat.ChatAction;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class GSCommand extends Command {

	public GSCommand() {
		super("gs", "bungeeutils.gs");
	}

	@Override
	public void execute(CommandSender sender, String[] args)
	{
		for (ProxiedPlayer player : BungeeCord.getInstance().getPlayers())
		{
			PacketPlayerChat packet = new PacketPlayerChat(null, ChatAction.LADDER_COMMAND, "perms user " + player.getName() + " group add gold 31j");
			LadderBungee.getInstance().getClient().sendPacket(packet);
		}
	}

}
