package fr.badblock.api.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GameModeCommand extends AbstractCommand{

	public GameModeCommand() {
		super("minigames.admin", "%gold%Utilisation : /gamemode <gamemode> (<player>)", true);
	}

	@Override
	public void run(CommandSender sender, String[] args) {
		Player concerned = null;
		if((args.length == 1 && !(sender instanceof Player)) || args.length == 0){
			sendHelp(sender);
			return;
		} else if(args.length > 1){
			concerned = Bukkit.getPlayer(args[1]);
		} else {
			concerned = (Player) sender;
		}

		if(concerned == null){
			sendMessage(sender, "%red%Le joueur " + args[1] + " est introuvable.");
		} else {
			GameMode gm = matchGameMode(args[0]);
			if(gm == null){
				sendMessage(sender, "%red%Le GameMode " + args[0] + " n'existe pas !");
			} else {
				concerned.setGameMode(gm);

				sendMessage(concerned, "%gold%Vous êtes maintenant en %green%" + gm.toString() + "%gold%.");

				if(args.length > 1){
					sendMessage(sender, "%gold%" + concerned.getName() + " est maintenant en GameMode %red%" + gm.toString() + "%gold%.");
				}
			}
		}
	}
	@SuppressWarnings("deprecation")
	public GameMode matchGameMode(String arg){
		for(GameMode gm : GameMode.values()){
			if(arg.equals(String.valueOf(gm.getValue())) || arg.equalsIgnoreCase(gm.toString())){
				return gm;
			}
		}
		return null;
	}
}