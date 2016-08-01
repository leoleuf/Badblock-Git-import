package fr.badblock.ladder.commands.punish;

import java.util.ArrayList;
import java.util.List;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.chat.ChatColor;
import fr.badblock.ladder.api.commands.Command;
import fr.badblock.ladder.api.entities.CommandSender;
import fr.badblock.ladder.api.entities.OfflinePlayer;
import fr.badblock.ladder.api.utils.Punished;

public class CommandSanction extends Command {
	public CommandSanction() {
		super("sanction", "ladder.command.sanction");
	}

	@Override
	public void executeCommand(CommandSender sender, String[] args) {
		if(args.length == 0){
			sender.sendMessage(ChatColor.RED + "Utilisation : /sanction <player>");
		} else {
			OfflinePlayer player = Ladder.getInstance().getOfflinePlayer(args[0]);
			if(!player.hasPlayed()){
				sender.sendMessage(ChatColor.RED + "Le joueur '" + args[0] + "' ne s'est jamais connecté sur BadBlock !");
			} else {
				List<String> sanctions = new ArrayList<>();
				
				sanctions.addAll(build(player.getName(), player.getAsPunished()));
				
				if(player.getLastAddress() != null)
					sanctions.addAll(build(player.getLastAddress().getHostAddress(), player.getIpAsPunished()));
				
				if(sanctions.isEmpty()){
					sender.sendMessage(ChatColor.RED + "Ce joueur n'a pas de sanctions !");
				} else {
					sender.sendMessage(ChatColor.GOLD + "Les sanctions de " + ChatColor.RED + player.getName() + ChatColor.GOLD + " sont :");
				
					for(String sanction : sanctions)
						sender.sendMessage(sanction);
				}
			}
		}
	}
	
	private List<String> build(String name, Punished punished){
		List<String> sanctions = new ArrayList<>();

		if(punished.isBan()){
			sanctions.add(ChatColor.RED + "> " + ChatColor.GOLD + name + " banni (" + punished.getBanReason() + ") par " + punished.getBanner() + " (" + punished.buildBanTime() + ")");
		}
		
		if(punished.isMute()){
			sanctions.add(ChatColor.RED + "> " + ChatColor.GOLD + name + " mute (" + punished.getMuteReason() + ") par " + punished.getMuter() + " (" + punished.buildMuteTime() + ")");
		}
		
		return sanctions;
	}
}