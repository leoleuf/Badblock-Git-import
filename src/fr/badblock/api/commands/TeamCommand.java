package fr.badblock.api.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.badblock.api.game.BPlayer;
import fr.badblock.api.game.BPlayersManager;
import fr.badblock.api.listeners.ChatListener;

public class TeamCommand extends AbstractCommand{
	public TeamCommand() {
		super("minigames.player", "%gold%Utilisation : /t <message>", true);
	}

	@Override
	public void run(CommandSender sender, String[] args) {
		BPlayer player = null;
		if(args.length == 0 || !(sender instanceof Player)){
			sendHelp(sender);
			return;
		} 

		player = BPlayersManager.getInstance().getPlayer((Player) sender);
		
		if(player == null || player.isSpectator()){
			sendMessage(sender, "%red%Vous n'êtes pas dans la partie !");
		} else if(player.getTeam() == null || player.getTeam().getTeam() == null){
			sendMessage(sender, "%red%Vous ne faites partie d'aucune team !");
		} else {
			String message = ""; boolean isFirst = true;
			for(String part : args){
				if(!isFirst) message += " "; else isFirst = false;
				message += part;
			}
			String formatted = ChatListener.format(message,(Player)sender);
			if(formatted == null) return;
			
			player.getTeam().getTeam().sendMessage("%dgray%[Chat Equipe (/t)] " + formatted);
		}
	}
}
