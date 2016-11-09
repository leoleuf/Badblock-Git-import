package fr.badblock.bungee.utils;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

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
