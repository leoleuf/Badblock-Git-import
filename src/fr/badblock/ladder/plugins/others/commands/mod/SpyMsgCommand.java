package fr.badblock.ladder.plugins.others.commands.mod;

import fr.badblock.ladder.api.commands.Command;
import fr.badblock.ladder.api.entities.CommandSender;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.ladder.plugins.others.friends.FriendPlayer;
import fr.badblock.ladder.plugins.others.utils.I18N;

public class SpyMsgCommand extends Command {

	public static String prefix = "§8[§bMP §c§lSPY§8] §f";

	public SpyMsgCommand() {
		super("spymsg", "others.mod.spymsg", "sg", "spywhisper", "spym", "spymp", "spyw", "spytellraw", "spytell",
				"minecraft:spytell", "minecraft:spytellraw", "minecraft:spywhisper", "minecraft:spyw");
	}

	@Override
	public void executeCommand(CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(I18N.getTranslatedMessage("msg.spymode.permission"));
			return;
		}
		Player player = (Player) sender;
		FriendPlayer fromPlayer = FriendPlayer.get(player);
		if (fromPlayer == null)
			return;
		if (fromPlayer.spy) {
			fromPlayer.spy = false;
			player.sendMessage(I18N.getTranslatedMessage("msg.spymode.disabled"));
			fromPlayer.hasNewChanges = true;
		} else {
			fromPlayer.spy = true;
			player.sendMessage(I18N.getTranslatedMessage("msg.spymode.enabled"));
			fromPlayer.hasNewChanges = true;
		}
	}

}
