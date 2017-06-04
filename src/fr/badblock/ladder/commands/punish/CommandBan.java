package fr.badblock.ladder.commands.punish;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.chat.ChatColor;
import fr.badblock.ladder.api.commands.Command;
import fr.badblock.ladder.api.entities.CommandSender;
import fr.badblock.ladder.api.entities.OfflinePlayer;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.ladder.api.utils.StringUtils;
import fr.badblock.ladder.entities.LadderPlayer;

public class CommandBan extends Command {
	public CommandBan() {
		super("ban", "ladder.command.ban");
	}

	@Override
	public void executeCommand(CommandSender sender, String[] args) {
		if(args.length == 0){
			sender.sendMessage(ChatColor.RED + "Utilisation : /ban <player> <raison>");
		} else if(args.length == 1){
			sender.sendMessage(ChatColor.RED + "Veuillez préciser la raison !");
		} else {
			OfflinePlayer player = Ladder.getInstance().getOfflinePlayer(args[0]);
			if(!player.hasPlayed()){
				sender.sendMessage(ChatColor.RED + "Le joueur '" + args[0] + "' ne s'est jamais connecté sur BadBlock !");
			} else if((sender instanceof Player) && player.hasPermission("ladder.command.ban")){
				sender.sendMessage(ChatColor.RED + "Vous ne pouvez pas ban ce joueur !");
			} else {

				try {
					String reason = ChatColor.replaceColor(StringUtils.join(args, " ", 1));

					player.getAsPunished().setBan(true);
					player.getAsPunished().setBanEnd(-1);
					player.getAsPunished().setBanner(sender.getName());
					player.getAsPunished().setBanReason(reason);
					player.savePunishions();

					if(player instanceof LadderPlayer){
						LadderPlayer connected = (LadderPlayer) player;
						if(connected.getBukkitServer() != null){
							connected.getBukkitServer().broadcast(ChatColor.RED + player.getName() + 
									ChatColor.YELLOW + " ban " + ChatColor.YELLOW + " par " + 
									ChatColor.RED + sender.getName() + " " + ChatColor.YELLOW + "pour " + 
									ChatColor.BOLD + reason + ".");
						}

						//connected.disconnect(player.getAsPunished().buildBanReason());
					}

					sender.sendMessage(ChatColor.GREEN + "Appliqué !");

					player.saveData();
				} catch(Exception e){
					e.printStackTrace();
				}
				
			}
		}
	}
}