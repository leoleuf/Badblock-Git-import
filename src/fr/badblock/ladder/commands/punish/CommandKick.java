package fr.badblock.ladder.commands.punish;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.chat.ChatColor;
import fr.badblock.ladder.api.commands.Command;
import fr.badblock.ladder.api.entities.CommandSender;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.ladder.api.utils.StringUtils;

public class CommandKick extends Command {
	public CommandKick() {
		super("kick", "ladder.command.kick");
	}

	@Override
	public void executeCommand(CommandSender sender, String[] args) {
		if(args.length == 0){
			sender.sendMessage(ChatColor.RED + "Utilisation : /kick <player> <raison>");
		} else if(args.length == 1){
			sender.sendMessage(ChatColor.RED + "Veuillez préciser la raison !");
		} else {
			Player player = Ladder.getInstance().getPlayer(args[0]);
			if(player == null){
				sender.sendMessage(ChatColor.RED + "Le joueur '" + args[0] + "' n'est pas connecté sur BadBlock !");
			} else if((sender instanceof Player) && player.hasPermission("ladder.command.kick")){
				sender.sendMessage(ChatColor.RED + "Vous ne pouvez pas kick ce joueur !");
			} else {
				String reason = ChatColor.replaceColor(StringUtils.join(args, " ", 1));
				player.disconnect(ChatColor.RED + "Vous avez été éjecté : " + ChatColor.WHITE + reason);

				sender.sendMessage(ChatColor.GREEN + "Appliqué !");
			}
		}
	}
}