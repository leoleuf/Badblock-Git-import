package fr.badblock.bungeecord.plugins.utils;

import java.net.InetSocketAddress;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Command;

public class AddBServerCommand extends Command {

	public AddBServerCommand() {
		super("addbserver", "bungeeutils.addbserver");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length == 0) return;
		try {
			if (args.length != 2) {
				sender.sendMessage("§cUtilisation: /addserver <name> <ip:port>");
				return;
			}
			if (BungeeCord.getInstance().getServerInfo(args[0]) != null) {
				sender.sendMessage("§cCe serveur existe déjà.");
				return;
			}
			ServerInfo server = BungeeCord.getInstance().constructServerInfo(args[0], new InetSocketAddress(args[1].split(":")[0], Integer.parseInt(args[1].split(":")[1])), args[0], false);
			BungeeCord.getInstance().getServers().put(args[0], server);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
