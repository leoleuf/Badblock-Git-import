package fr.badblock.bungee.modules.modo.commands.subcommands;

import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacket;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacketEncoder;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacketMessage;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacketType;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.modules.modo.commands.AbstractModCommand;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * 
 * BadCommand
 * 
 * @author xMalware
 *
 */
public class GhostCommand extends AbstractModCommand {

	/**
	 * Constructor
	 */
	public GhostCommand() {
		// Super!
		super("ghost", new String[] { "g" });
	}

	/**
	 * Run
	 */
	@Override
	public void run(CommandSender sender, String[] args) {
		// If arg length != 2
		if (args.length != 2) {
			// Send the message
			I19n.sendMessage(sender, getPrefix("usage"), null);
			// So we stop there
			return;
		}

		if (!(sender instanceof ProxiedPlayer))
		{
			// Send the message
			I19n.sendMessage(sender, "bungee.commands.playersonly", null);
			// So we stop there
			return;
		}

		String playerName = args[1];

		BungeeManager bungeeManager = BungeeManager.getInstance();

		if (!bungeeManager.hasUsername(playerName)) {
			I19n.sendMessage(sender, getPrefix("offline"), null, playerName);
			return;
		}

		BadPlayer badPlayer = bungeeManager.getBadPlayer(playerName);

		if (badPlayer == null) {
			I19n.sendMessage(sender, getPrefix("offline"), null, playerName);
			return;
		}

		ProxiedPlayer proxiedPlayer = (ProxiedPlayer) sender;

		String currentServer = badPlayer.getCurrentServer();

		if (currentServer == null) {
			I19n.sendMessage(sender, getPrefix("unknownserver"), null, currentServer);
			return;
		}
		
		ServerInfo serverInfo = BungeeCord.getInstance().getServerInfo(currentServer);

		if (serverInfo == null) {
			I19n.sendMessage(sender, getPrefix("unknownserver"), null, currentServer);
			return;
		}

		boolean already = false;
		
		proxiedPlayer.connect(serverInfo);
		if (proxiedPlayer.getServer() != null && proxiedPlayer.getServer().getInfo() != null) {
			if (proxiedPlayer.getServer().getInfo().equals(serverInfo)) {
				already = true;
			}
		}
		
		String rawMessage = sender.getName() + ";" + playerName;
		RabbitPacketMessage message = new RabbitPacketMessage(30000, rawMessage);
		RabbitPacket rabbitPacket = new RabbitPacket(message, "gameapi.ghost", false, RabbitPacketEncoder.UTF8,
				RabbitPacketType.PUBLISHER);
		BadBungee.getInstance().getRabbitService().sendPacket(rabbitPacket);
		
		if (!already)
		{
			proxiedPlayer.connect(serverInfo);
		}


		I19n.sendMessage(sender, getPrefix("teleported"), null, badPlayer.getName(), serverInfo.getName());
	}

}