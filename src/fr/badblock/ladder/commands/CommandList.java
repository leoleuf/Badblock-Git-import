package fr.badblock.ladder.commands;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.commands.Command;
import fr.badblock.ladder.api.entities.CommandSender;
import fr.badblock.ladder.api.entities.Player;

public class CommandList extends Command {
	
	public CommandList() {
		super("llist", "ladder.command.list", "glist");
	}

	@Override
	public void executeCommand(CommandSender sender, String[] args) {
		sender.sendMessage("§7Ladder > §b" + Ladder.getInstance().getBungeeOnlineCount() + " §9joueurs");
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if (player.getBukkitServer() != null)
				sender.sendMessage("§7Serveur > §b" + player.getBukkitServer().getPlayers().size() + " §9joueurs");
			else sender.sendMessage("§aVous n'êtes sur aucun serveur Bukkit.");
		}
	}
	
}
