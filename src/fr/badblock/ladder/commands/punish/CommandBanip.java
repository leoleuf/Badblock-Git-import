package fr.badblock.ladder.commands.punish;

import java.util.UUID;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.chat.ChatColor;
import fr.badblock.ladder.api.commands.Command;
import fr.badblock.ladder.api.entities.CommandSender;
import fr.badblock.ladder.api.entities.OfflinePlayer;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.ladder.api.utils.StringUtils;
import fr.badblock.ladder.entities.LadderPlayer;

public class CommandBanip extends Command {
	public CommandBanip() {
		super("banip", "ladder.command.banip");
	}

	@Override
	public void executeCommand(CommandSender sender, String[] args) {
		if(args.length == 0){
			sender.sendMessage(ChatColor.RED + "Utilisation : /banip <player> <raison>");
		} else if(args.length == 1){
			sender.sendMessage(ChatColor.RED + "Veuillez préciser la raison !");
		} else {
			OfflinePlayer player = Ladder.getInstance().getOfflinePlayer(args[0]);
			if(!player.hasPlayed()){
				sender.sendMessage(ChatColor.RED + "Le joueur '" + args[0] + "' ne s'est jamais connecté sur BadBlock !");
			} else if((sender instanceof Player) && player.hasPermission("ladder.command.banip")){
				sender.sendMessage(ChatColor.RED + "Vous ne pouvez pas ban ce joueur !");
			} else {
				String ip	  = player.getData().get("lastIp").getAsString();
				String reason = ChatColor.replaceColor(StringUtils.join(args, " ", 1));

				player.getIpAsPunished().setBan(true);
				player.getIpAsPunished().setBanEnd(-1);
				player.getIpAsPunished().setBanner(sender.getName());
				player.getIpAsPunished().setBanReason(reason);
				player.getIpData().savePunishions();
				
				if(player instanceof LadderPlayer){
					LadderPlayer connected = (LadderPlayer) player;
					if(connected.getBukkitServer() != null){
						connected.getBukkitServer().broadcast(ChatColor.RED + hideIp(ip) + 
								ChatColor.YELLOW + " ban par " + 
								ChatColor.RED + sender.getName() + " " + ChatColor.YELLOW + "pour " + 
								ChatColor.BOLD + reason + ".");
					}
				}

				for(UUID uniqueId : player.getIpData().getCurrentPlayers()){
					Player connected = Ladder.getInstance().getPlayer(uniqueId);
					if(connected != null){
						//connected.disconnect(player.getIpAsPunished().buildBanReason());
					}
				}
				
				sender.sendMessage(ChatColor.GREEN + "Appliqué !");
			}
		}
	}

	private String hideIp(String ip){
		String[] part = ip.split("\\.");
		return part[0] + "." + part[1] + ".*.*";
	}
}