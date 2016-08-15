package fr.badblock.api.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;

public class HealCommand extends AbstractCommand{
	public HealCommand() {
		super("minigames.admin", "%gold%Utilisation : /heal (<player>)", true);
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
			heal(concerned);

			sendMessage(concerned, "%gold%Vous êtes de nouveau en pleine forme !");
			if(args.length > 0){
				sendMessage(sender, "%gold%" + concerned.getName() + " a retrouvé sa vie.");
			}
		}
	}
	public static void heal(Player concerned){
		concerned.setHealth(((Damageable)concerned).getMaxHealth());
		concerned.setFireTicks(0);
		FeedCommand.feed(concerned);
	}
}
