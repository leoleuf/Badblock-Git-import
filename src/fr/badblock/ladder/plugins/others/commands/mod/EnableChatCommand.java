package fr.badblock.ladder.plugins.others.commands.mod;

import fr.badblock.ladder.api.commands.Command;
import fr.badblock.ladder.api.entities.CommandSender;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.ladder.plugins.others.friends.FriendPlayer;
import fr.badblock.ladder.plugins.others.utils.I18N;

public class EnableChatCommand extends Command {
	
	public EnableChatCommand() {
		super("enablechat", "modo.enablechat");
	}

	@Override
	public void executeCommand(CommandSender sender, String[] args) {
		if (!(sender instanceof Player))
		{
			sender.sendMessage("§cOnly players can execute this command.");
			return;
		}
		Player player = (Player) sender;
		FriendPlayer friendPlayer = FriendPlayer.get(player);
		if (friendPlayer == null)
		{
			sender.sendMessage("§cSomething gone wrong.");
			return;
		}
		if (friendPlayer.enableChat)
		{
			player.sendMessage(I18N.getTranslatedMessage("msg.enableChat.disabled"));
		}
		else
		{
			player.sendMessage(I18N.getTranslatedMessage("msg.enableChat.enabled"));	
		}
		friendPlayer.enableChat = !friendPlayer.enableChat;
	}

}