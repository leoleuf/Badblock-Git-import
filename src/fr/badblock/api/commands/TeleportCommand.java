package fr.badblock.api.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportCommand extends AbstractCommand{
	public TeleportCommand() {
		super("minigames.admin", "%gold%Utilisation : /tp <player> (<player>)", true);
	}

	@Override
	public void run(CommandSender sender, String[] args) {
		Player from = null, to = null;
		if(args.length == 0){
			sendHelp(sender);
			return;
		}
		if(args.length == 1 && !(sender instanceof Player)){
			sendHelp(sender);
			return;
		} else if(args.length > 1){
			to = Bukkit.getPlayer(args[1]);
			from = Bukkit.getPlayer(args[0]);
		} else {
			from = (Player) sender;
			to = Bukkit.getPlayer(args[0]);
		}

		if(from == null){
			sendMessage(sender, "%red%Le joueur " + args[0] + " est introuvable.");
		} else if(to == null) {
			sendMessage(sender, "%red%Le joueur " + (args.length > 1 ? args[0] : args[1]) + " est introuvable.");
		} else {
			sendMessage(sender, "%green%Téléporté !");
			from.teleport(to);
			if(args.length > 1)
				sendMessage(sender, "%green%Joueur téléporté !");
		}
	}
}
