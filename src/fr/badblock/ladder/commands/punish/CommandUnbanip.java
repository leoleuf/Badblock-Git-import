package fr.badblock.ladder.commands.punish;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.chat.ChatColor;
import fr.badblock.ladder.api.commands.Command;
import fr.badblock.ladder.api.entities.CommandSender;
import fr.badblock.ladder.api.entities.OfflinePlayer;
import fr.badblock.ladder.api.entities.PlayerIp;

public class CommandUnbanip extends Command {
	public CommandUnbanip() {
		super("unbanip", "ladder.command.unbanip", "pardonip");
	}

	@Override
	public void executeCommand(CommandSender sender, String[] args) {
		if(args.length == 0){
			sender.sendMessage(ChatColor.RED + "Utilisation : /unban <player>");
		} else {
			OfflinePlayer player = Ladder.getInstance().getOfflinePlayer(args[0]);
			if(!player.hasPlayed()){
				sender.sendMessage(ChatColor.RED + "Le joueur '" + args[0] + "' ne s'est jamais connecté sur BadBlock !");
			} else {
				PlayerIp ip = player.getIpData();
				if(ip == null) return;
				
				ip.getAsPunished().setBan(false);
				ip.savePunishions();
				ip.saveData();
				
				sender.sendMessage(ChatColor.GREEN + "Appliqué !");
			}
		}
	}
}