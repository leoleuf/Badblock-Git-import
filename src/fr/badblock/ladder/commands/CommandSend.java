package fr.badblock.ladder.commands;

import java.util.UUID;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.chat.ChatColor;
import fr.badblock.ladder.api.commands.Command;
import fr.badblock.ladder.api.entities.Bukkit;
import fr.badblock.ladder.api.entities.CommandSender;
import fr.badblock.ladder.api.entities.Player;

public class CommandSend extends Command {
	public CommandSend() {
		super("send", "ladder.command.send");
	}

	@Override
	public void executeCommand(CommandSender sender, String[] args) {
		if(args.length < 2){
			sender.sendMessage(ChatColor.RED + "Utilisation : /send <player> <sender>"); return;
		}

		Player player = Ladder.getInstance().getPlayer(args[0]);
		Bukkit bukkit = Ladder.getInstance().getBukkitServer(args[1]);

		if(player == null && !args[0].equalsIgnoreCase("current")){
			sender.sendMessage(ChatColor.RED + "Le joueur n'est pas connecté !");
		} else if(bukkit == null){
			sender.sendMessage(ChatColor.RED + "Le serveur n'existe pas !");
		} else if(args[0].equalsIgnoreCase("current") && sender instanceof Player){
			Player pSender = (Player) sender;
			
			for(UUID uniqueId : pSender.getBukkitServer().getPlayers()){
				Player toSend = Ladder.getInstance().getPlayer(uniqueId);

				if(toSend != null)
					send(sender, player, bukkit);
			}
			sender.sendMessage(ChatColor.GREEN + "Téléporté !");
		} else {
			send(sender, player, bukkit);
			sender.sendMessage(ChatColor.GREEN + "Téléporté !");
		}
	}

	private void send(CommandSender sender, Player player, Bukkit server){
		player.connect(server);
		player.sendMessage(ChatColor.GREEN + "Vous avez été déplacé par " + sender.getName());
	}
}