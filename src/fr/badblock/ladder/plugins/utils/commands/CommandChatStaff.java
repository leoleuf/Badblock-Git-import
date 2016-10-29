package fr.badblock.ladder.plugins.utils.commands;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.chat.ChatColor;
import fr.badblock.ladder.api.commands.Command;
import fr.badblock.ladder.api.entities.CommandSender;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.ladder.api.utils.StringUtils;
import fr.badblock.permissions.PermissiblePlayer;

public class CommandChatStaff extends Command {
	public CommandChatStaff() {
		super("cs", "ladder.command.chatstaff");
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
		String message = "§6§l[CS] §r" + ChatColor.translateAlternateColorCodes('&', permissiblePlayer.getDisplayName()) + "§7 » §e" + StringUtils.join(args, " ");
		
		for(Player plo : Ladder.getInstance().getOnlinePlayers()){
			if(canReceiveMessage(plo, "chatstaff"))
				plo.sendMessage(message);
		}
	}
	
	public static boolean canReceiveMessage(Player player, String type){
		if(player.getBukkitServer() == null)
			return false;
		
		if(player.getBukkitServer().getName().startsWith("login"))
			return false;
		
		return player.hasPermission("ladder.command." + type);
	}
}
