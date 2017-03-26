package fr.badblock.bungeecord.plugins.utils;

import fr.badblock.bungeecord.plugins.ladder.LadderBungee;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

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
