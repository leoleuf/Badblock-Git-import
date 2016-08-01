package fr.badblock.ladder.commands.punish;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.chat.ChatColor;
import fr.badblock.ladder.api.commands.Command;
import fr.badblock.ladder.api.entities.CommandSender;
import fr.badblock.ladder.api.entities.OfflinePlayer;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.ladder.api.utils.StringUtils;
import fr.badblock.ladder.api.utils.Time;
import fr.badblock.ladder.entities.LadderPlayer;

public class CommandMute extends Command {
	public CommandMute() {
		super("mute", "ladder.command.mute");
	}

	@Override
	public void executeCommand(CommandSender sender, String[] args) {
		if(args.length == 0){
			sender.sendMessage(ChatColor.RED + "Utilisation : /mute <player> <time> <raison>");
		} else if(args.length == 1){
			sender.sendMessage(ChatColor.RED + "Veuillez préciser le temps et la raison !");
		} else if(args.length == 2){
			sender.sendMessage(ChatColor.RED + "Veuillez préciser la raison !");
		} else {
			OfflinePlayer player = Ladder.getInstance().getOfflinePlayer(args[0]);
			player.getAsPunished().checkEnd();
			
			if(!player.hasPlayed()){
				sender.sendMessage(ChatColor.RED + "Le joueur '" + args[0] + "' ne s'est jamais connecté sur BadBlock !");
			} else if((sender instanceof Player) && player.hasPermission("ladder.command.mute")){
				sender.sendMessage(ChatColor.RED + "Vous ne pouvez pas mute ce joueur !");
			} else if(player.getAsPunished().isMute()){
				sender.sendMessage(ChatColor.RED + "Ce joueur est déjà mute, soyez gentil !");
			} else {
				
				String reason = ChatColor.replaceColor(StringUtils.join(args, " ", 2));
				long   time   = Time.MILLIS_SECOND.matchTime(args[1]);
				
				if(time > Time.DAY.convert(14, Time.MILLIS_SECOND) && !sender.hasPermission("ladder.punish.bypass")){
					sender.sendMessage(ChatColor.RED + "Vous ne pouvez pas bannir plus de 14 jours !");
				} else if(time == 0){
					sender.sendMessage(ChatColor.RED + "Veuillez entrer un temps valide !");
				} else {
					player.getAsPunished().setMute(true);
					player.getAsPunished().setMuteEnd(System.currentTimeMillis() + time);
					player.getAsPunished().setMuter(sender.getName());
					player.getAsPunished().setMuteReason(reason);
					player.savePunishions();
					
					player.getIpAsPunished().setMute(true);
					player.getIpAsPunished().setMuteEnd(System.currentTimeMillis() + time);
					player.getIpAsPunished().setMuter(sender.getName());
					player.getIpAsPunished().setMuteReason(reason);
					player.getIpData().savePunishions();
					
					player.saveData();
					player.getIpData().saveData();

					String msg = ChatColor.RED + player.getName() + 
							ChatColor.YELLOW + " mute " + ChatColor.YELLOW + 
							Time.MILLIS_SECOND.toFrench(time, Time.MINUTE, Time.DAY) + ChatColor.YELLOW + " par " + 
							ChatColor.RED + sender.getName() + " " + ChatColor.YELLOW + "pour " + 
							ChatColor.BOLD + reason + ".";
					
					if(player instanceof LadderPlayer){
						LadderPlayer connected = (LadderPlayer) player;
						if(connected.getBukkitServer() != null){
							connected.getBukkitServer().broadcast(msg);
						}
						
						connected.sendToBungee("punish");
						player.getIpData().sendToServers();
					}
					
					sender.sendMessage(msg);
				}
			}
		}
	}
}
