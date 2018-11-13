package fr.badblock.game.core112R1.commands;

import org.bukkit.command.CommandSender;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.general.StringUtils;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class ModCommand extends AbstractCommand {
	
	public ModCommand() {
		super("mod", new TranslatableString("commands.mod.usage"), GamePermission.MODERATOR, "md");
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		if (args.length == 0) return true;
		GameAPI.i18n().sendMessage(sender, "chat.mod", sender.getName(), StringUtils.join(args, " ", 0));
		return true;
	}
	
}
