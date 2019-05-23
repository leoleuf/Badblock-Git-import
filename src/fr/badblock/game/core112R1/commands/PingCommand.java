package fr.badblock.game.core112R1.commands;

import org.bukkit.command.CommandSender;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class PingCommand extends AbstractCommand {
	public PingCommand() {
		super("ping", new TranslatableString("commands.ping.usage"), GamePermission.PLAYER, "pong", "echo");
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		GameAPI.i18n().sendMessage(sender, "commands.ping.pong");
		return true;
	}
}