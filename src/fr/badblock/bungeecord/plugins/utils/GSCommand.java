package fr.badblock.bungeecord.plugins.utils;

import fr.badblock.bungeecord.BungeeCord;
import fr.badblock.bungeecord.api.CommandSender;
import fr.badblock.bungeecord.api.connection.ProxiedPlayer;
import fr.badblock.bungeecord.api.plugin.Command;

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
