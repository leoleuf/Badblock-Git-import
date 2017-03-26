package fr.badblock.bungeecord.plugins.utils;

import fr.badblock.bungeecord.api.CommandSender;
import fr.badblock.bungeecord.api.plugin.Command;

public class BUReloadCommand extends Command {

	public BUReloadCommand() {
		super("bureload", "bungeeutils.reload", "burl");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		BungeeUtils.instance.loadConfig();
		sender.sendMessage("§aConfiguration rechargée !");
	}

}
