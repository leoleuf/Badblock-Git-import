4package fr.badblock.ladder.commands.punish;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.chat.ChatColor;
import fr.badblock.ladder.api.commands.Command;
import fr.badblock.ladder.api.entities.CommandSender;
import fr.badblock.ladder.api.entities.OfflinePlayer;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.ladder.api.utils.StringUtils;
import fr.badblock.ladder.api.utils.Time;

public class CommandTempbanip extends Command {
	public CommandTempbanip() {
		super("tempbanip", "ladder.command.tempbanip");
	}

	@Override
	public void executeCommand(CommandSender sender, String[] args) {
		if(args.length == 0){
			sender.sendMessage(ChatColor.RED + "Utilisation : /tempbanip <player> <time> <raison>");
		} else if(args.length == 1){
			sender.sendMessage(ChatColor.RED + "Veuillez préciser le temps et la raison !");
		} else if(args.length == 2){
			sender.sendMessage(ChatColor.RED + "Veuillez préciser la raison !");
		} else {
			OfflinePlayer player = Ladder.getInstance().getOfflinePlayer(args[0]);
			if(!player.hasPlayed()){
				sender.sendMessage(ChatColor.RED + "Le joueur '" + args[0] + "' ne s'est jamais connecté sur BadBlock !");
			} else if((sender instanceof Player) && player.hasPermission("ladder.command.tempbanip")){
				sender.sendMessage(ChatColor.RED + "Vous ne pouvez pas ban ce joueur !");
			} else {
				String ip	  = player.getLastAddress().getHostAddress();
				String reason = ChatColor.replaceColor(StringUtils.join(args, " ", 2));
				long   time   = Time.MILLIS_SECOND.matchTime(args[1]);
				
				if(time > Time.DAY.convert(7, Time.MILLIS_SECOND) && !sender.hasPermission("ladder.punish.bypass")){
					sender.sendMessage(ChatColor.RED + "Vous ne pouvez pas bannir plus de sept jours !");
				} else if(time == 0){
					sender.sendMessage(ChatColor.RED + "Veuillez entrer un temps valide !");
				} else {
					player.getIpAsPunished().setBan(true);
					player.getIpAsPunished().setBanEnd(System.currentTimeMillis() + time);
					player.getIpAsPunished().setBanner(sender.getName());
					player.getIpAsPunished().setBanReason(reason);
					
					player.getIpData().savePunishions();;
					player.getIpData().saveData();
					
					String msg = ChatColor.RED + hideIp(ip) + 
							ChatColor.YELLOW + " ban " + ChatColor.YELLOW + 
							Time.MILLIS_SECOND.toFrench(time, Time.MINUTE, Time.DAY) + ChatColor.YELLOW + " par " + 
							ChatColor.RED + sender.getName() + " " + ChatColor.YELLOW + "pour " + 
							ChatColor.BOLD + reason + ".";
					
					if(player instanceof Player){
						Player connected = (Player) player;
						if(connected.getBukkitServer() != null){
							connected.getBukkitServer().broadcast(msg);
						}
						
						//connected.disconnect(player.getIpAsPunished().buildBanReason());
					}
					
					sender.sendMessage(msg);
				}
				
				player.saveData();
			}
		}
	}
	
	private String hideIp(String ip){
		String[] part = ip.split("\\.");
		return part[0] + "." + part[1] + ".*.*";
	}
}
