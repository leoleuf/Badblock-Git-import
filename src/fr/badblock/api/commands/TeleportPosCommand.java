package fr.badblock.api.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportPosCommand extends AbstractCommand{
	public TeleportPosCommand() {
		super("minigames.admin", "%gold%Utilisation : /tppos (<player>) <x> <y> <z>", true);
	}

	@Override
	public void run(CommandSender sender, String[] args) {
		double x = 0, y = 0, z = 0;
		Player concerned = null;
		if((args.length == 3 && !(sender instanceof Player)) || args.length < 3){
			sendHelp(sender);
			return;
		} else if(args.length > 3){
			concerned = Bukkit.getPlayer(args[0]);
			try {
				x = Double.parseDouble(args[1]);
				y = Double.parseDouble(args[2]);
				z = Double.parseDouble(args[3]);
			} catch(Exception e){
				sendMessage(sender, "%red%Coordonnées invalides !"); return;
			}
		} else {
			concerned = (Player) sender;
			try {
				x = Double.parseDouble(args[0]);
				y = Double.parseDouble(args[1]);
				z = Double.parseDouble(args[2]);
			} catch(Exception e){
				sendMessage(sender, "%red%Coordonnées invalides !"); return;
			}
		}

		if(concerned == null){
			sendMessage(sender, "%red%Le joueur " + args[0] + " est introuvable.");
		} else {
			Location to = new Location(concerned.getLocation().getWorld(), x, y, z);
			concerned.teleport(to);
			sendMessage(concerned, "%green%Téléporté !");
			
			if(args.length != 3)
				sendMessage(sender, "%green%Le joueur a été téléporté !");
		}
	}
}
