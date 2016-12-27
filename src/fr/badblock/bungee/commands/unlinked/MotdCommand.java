package fr.badblock.bungee.commands.unlinked;

import fr.badblock.bungee.BadBungee;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class MotdCommand extends Command {

	public MotdCommand() {
		super("motd", "badbungee.motd");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		BadBungee.getInstance().reloadMotd();
		sender.sendMessage("§aMessage du jour mis à jour!");
	}

}
