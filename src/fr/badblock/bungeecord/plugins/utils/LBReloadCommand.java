package fr.badblock.bungeecord.plugins.utils;

import fr.badblock.bungeecord.api.CommandSender;
import fr.badblock.bungeecord.api.plugin.Command;
import fr.badblock.bungeecord.plugins.ladder.LadderBungee;


public class LBReloadCommand extends Command {

	public LBReloadCommand() {
		super("lbreload", "ladderbungee.reload", "lbrl");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		LadderBungee.getInstance().loadConfig();
		sender.sendMessage("§aConfiguration rechargée !");
	}

}
