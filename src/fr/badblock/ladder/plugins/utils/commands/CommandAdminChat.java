package fr.badblock.ladder.plugins.utils.commands;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.chat.ChatColor;
import fr.badblock.ladder.api.commands.Command;
import fr.badblock.ladder.api.entities.CommandSender;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.ladder.api.utils.StringUtils;
import fr.badblock.permissions.PermissiblePlayer;

public class CommandAdminChat extends Command {
	public CommandAdminChat() {
		super("ac", "ladder.command.adminchat");
	}

	@Override
	public void executeCommand(CommandSender sender, String[] args) {
		if(args.length == 0) {
			sender.sendMessage(ChatColor.RED + "Veuillez préciser le message !");
			return;
		}
		if (!(sender instanceof Player)) return;
		Player player = (Player) sender;
		PermissiblePlayer permissiblePlayer = (PermissiblePlayer) player.getAsPermissible();
		String message = "§c§l[AC] §r" + ChatColor.translateAlternateColorCodes('&', permissiblePlayer.getDisplayName()) + "§7 > §e" + ChatColor.AQUA + StringUtils.join(args, " ");
		for(Player plo : Ladder.getInstance().getOnlinePlayers()){
			if(plo.hasPermission("ladder.command.adminchat"))
				plo.sendMessage(message);
		}
	}
}
