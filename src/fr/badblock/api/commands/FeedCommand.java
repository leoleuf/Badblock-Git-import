package fr.badblock.api.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FeedCommand extends AbstractCommand{
	public FeedCommand() {
		super("minigames.admin", "%gold%Utilisation : /feed (<player>)", true);
	}

	@Override
	public void run(CommandSender sender, String[] args) {
		Player concerned = null;
		if(args.length == 0 && !(sender instanceof Player)){
			sendHelp(sender);
		} else if(args.length > 0){
			concerned = Bukkit.getPlayer(args[0]);
		} else {
			concerned = (Player) sender;
		}

		if(concerned == null){
			sendMessage(sender, "%red%Le joueur " + args[0] + " est introuvable.");
		} else {
			feed(concerned);

			sendMessage(concerned, "%gold%Vous avez été nourri !");
			if(args.length > 0){
				sendMessage(sender, "%gold%" + concerned.getName() + " a été nourri.");
			}
		}
	}
	public static void feed(Player concerned){
		concerned.setFoodLevel(20);
	}
}
