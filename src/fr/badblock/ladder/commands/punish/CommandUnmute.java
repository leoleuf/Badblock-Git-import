package fr.badblock.ladder.commands.punish;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.chat.ChatColor;
import fr.badblock.ladder.api.commands.Command;
import fr.badblock.ladder.api.entities.CommandSender;
import fr.badblock.ladder.entities.LadderOfflinePlayer;
import fr.badblock.ladder.entities.LadderPlayer;

public class CommandUnmute extends Command {
	public CommandUnmute() {
		super("unmute", "ladder.command.unmute");
	}

	@Override
	public void executeCommand(CommandSender sender, String[] args) {
		if(args.length == 0){
			sender.sendMessage(ChatColor.RED + "Utilisation : /unmute <player>");
		} else {
			LadderOfflinePlayer player = (LadderOfflinePlayer) Ladder.getInstance().getOfflinePlayer(args[0]);
			if(!player.hasPlayed()){
				sender.sendMessage(ChatColor.RED + "Le joueur '" + args[0] + "' ne s'est jamais connecté sur BadBlock !");
			} else {
				player.getPunished().setMute(false);
				player.getIpAsPunished().setMute(false);

				player.getIpData().savePunishions();
				player.getIpData().saveData();
				player.saveData();

				if(player instanceof LadderPlayer){
					LadderPlayer connected = (LadderPlayer) player;
					connected.sendToBungee("punish");
				}

				sender.sendMessage(ChatColor.GREEN + "Appliqué !");
			}
		}
	}
}