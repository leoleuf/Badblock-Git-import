package fr.badblock.api.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand extends AbstractCommand{
	public FlyCommand() {
		super("minigames.moderator", "%gold%Utilisation : /fly (<player>)", true);
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
			if(concerned.getAllowFlight()){
				concerned.setAllowFlight(false);
				concerned.setFlying(false);

				sendMessage(concerned, "%gold%Vous ne pouvez plus %red%voler%gold%.");
				if(args.length > 0){
					sendMessage(sender, "%gold%" + concerned.getName() + " ne peut plus %red%voler%gold%.");
				}
			} else if(!concerned.getAllowFlight()){
				concerned.setAllowFlight(true);
				concerned.setFlying(true);

				sendMessage(concerned, "%gold%Vous pouvez maintenant %green%voler%gold%.");
				if(args.length > 0){
					sendMessage(sender, "%gold%" + concerned.getName() + " peut maintenant %green%voler%gold%.");
				}
			}
		}
	}
}
