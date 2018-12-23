package fr.badblock.bungee.modules.modo.commands.subcommands;

import fr.badblock.bungee.modules.modo.commands.AbstractModCommand;
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
public class ConnectServerCommand extends AbstractModCommand {

	/**
	 * Constructor
	 */
	public ConnectServerCommand() {
		// Super!
		super("cs", new String[] {});
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

		String serverName = args[1];

		ServerInfo serverInfo = BungeeCord.getInstance().getServerInfo(serverName);

		if (serverInfo == null) {
			I19n.sendMessage(sender, getPrefix("unknownserver"), null, serverName);
			return;
		}

		ProxiedPlayer proxiedPlayer = (ProxiedPlayer) sender;

		if (proxiedPlayer.getServer() != null && proxiedPlayer.getServer().getInfo() != null) {
			if (proxiedPlayer.getServer().getInfo().equals(serverInfo)) {
				I19n.sendMessage(sender, getPrefix("alreadyconnected"), null, serverName);
				return;
			}
		}

		proxiedPlayer.connect(serverInfo);

		I19n.sendMessage(sender, getPrefix("teleported"), null, serverInfo.getName());
	}

}