package fr.badblock.ladder.commands;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.chat.ChatColor;
import fr.badblock.ladder.api.commands.Command;
import fr.badblock.ladder.api.entities.CommandSender;
import fr.badblock.ladder.api.entities.Player;

public class CommandFind extends Command {

	public CommandFind() {
		super("find", "ladder.command.find");
	}

	@Override
	public void executeCommand(CommandSender sender, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(ChatColor.RED + "Veuillez préciser le joueur !");
			return;
		}
		Player player = Ladder.getInstance().getPlayer(args[0]);
		if (player == null) {
			sender.sendMessage(ChatColor.RED + "Le joueur '" + args[0] +"' est introuvable !");
			return;
		}
		if(player.getBukkitServer() != null)
			sender.sendMessage("§9" + player.getName() + " §7> Serveur: §b" + player.getBukkitServer().getName() + "§7.");
		sender.sendMessage("§9" + player.getName() + " §7> BungeeCord: §b" + player.getBungeeServer().getName() + "§7.");
	}
}