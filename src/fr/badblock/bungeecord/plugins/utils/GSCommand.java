package fr.badblock.bungeecord.plugins.utils;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class GSCommand extends Command {

	public GSCommand() {
		super("gs", "bungeeutils.gs");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		StringBuilder stringBuilder = new StringBuilder("");
		for (ProxiedPlayer player : BungeeCord.getInstance().getPlayers())
			stringBuilder.append("perms user " + player.getName() + " group add gold 31j" + System.lineSeparator());
		System.out.println(stringBuilder.toString());
	}

}
