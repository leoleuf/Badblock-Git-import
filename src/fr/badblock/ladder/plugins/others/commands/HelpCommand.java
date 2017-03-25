package fr.badblock.ladder.plugins.others.commands;

import fr.badblock.ladder.api.chat.ChatColor;
import fr.badblock.ladder.api.commands.Command;
import fr.badblock.ladder.api.entities.CommandSender;
import fr.badblock.ladder.plugins.others.BadBlockOthers;

public class HelpCommand extends Command {

	public HelpCommand() {
		super("help", null, "?", "bukkit:help", "minecraft:help", "craftbukkit:help", "bukkit:?", "minecraft:?",
				"craftbukkit:?");
	}

	@Override
	public void executeCommand(CommandSender sender, String[] args) {
		String[] array = new String[BadBlockOthers.getInstance().help.size()];
		BadBlockOthers.getInstance().help.toArray(array);
		int o = -1;
		for (String a : array) {
			o++;
			array[o] = ChatColor.translateAlternateColorCodes('&', a);
		}
		sender.sendMessages(array);
	}

}
