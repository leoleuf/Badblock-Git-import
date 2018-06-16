package fr.badblock.bungeecord.plugins.others.commands;

import fr.badblock.bungeecord.plugins.others.utils.TPS;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;

public class BTPS extends Command {

	public BTPS() {
		super("btps", "bungeecord.btps");
	}

	@SuppressWarnings("deprecation")
	public void execute(CommandSender sender, String[] args) {
		sender.sendMessage("§bBungeeCord "
				+ ProxyServer.getInstance().getConfig().getListeners().iterator().next().getHost().getHostString()
				+ " : " + TPS.round(TPS.tps, 2) + " TPS");
	}

}