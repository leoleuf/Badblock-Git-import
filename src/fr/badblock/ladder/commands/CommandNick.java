package fr.badblock.ladder.commands;

import fr.badblock.ladder.Proxy;
import fr.badblock.ladder.api.chat.ChatColor;
import fr.badblock.ladder.api.commands.Command;
import fr.badblock.ladder.api.entities.CommandSender;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.protocol.packets.PacketPlayerNickSet;
import fr.badblock.utils.CommonFilter;

public class CommandNick extends Command {

	public CommandNick() {
		super("nick", "ladder.command.nick");
	}

	@Override
	public void executeCommand(CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Vous devez être un joueur pour pouvoir exécuter cette commande.");
			return;
		}
		Player player = (Player) sender;
		if (args.length == 0) {
			sender.sendMessage(ChatColor.RED + "Veuillez préciser le surnom !");
			return;
		}
		if (player.getNickName().equals(args[0])) {
			sender.sendMessage(ChatColor.RED + "Vous avez déjà ce surnom !");
			return;
		}
		if (player.getName().equals(args[0])) {
			Proxy.getInstance().getOfflineCachePlayers().remove(player.getNickName());
			player.setNickName(player.getName());
			player.sendToBungee("nickName");
			player.saveData();
			player.getBungeeServer().sendPacket(new PacketPlayerNickSet(player.getName(), CommonFilter.filterNames(player.getNickName())));
			sender.sendMessage(ChatColor.GREEN + "Vous avez supprimé votre surnom !");
			sender.sendMessage(ChatColor.GREEN + "Changez de serveur afin de voir un changement.");
			return;
		}
		if (Proxy.getInstance().getOfflinePlayer(args[0]).hasPlayed()) {
			sender.sendMessage(ChatColor.RED + "Ce surnom existe comme vrai joueur !");
			return;
		}
		if (Proxy.getInstance().getOfflineCachePlayers().containsKey(args[0])) {
			sender.sendMessage(ChatColor.RED + "Ce surnom est déjà utilisé !");
			return;
		}
		Proxy.getInstance().getOfflineCachePlayers().put(player.getNickName(), player);
		player.setNickName(args[0]);
		player.sendToBungee("nickName");
		player.saveData();
		player.getBungeeServer().sendPacket(new PacketPlayerNickSet(player.getName(), CommonFilter.filterNames(player.getNickName())));
		sender.sendMessage(ChatColor.GREEN + "Vous avez changé votre surnom en " + ChatColor.YELLOW + args[0] + ChatColor.GREEN + " !");
		sender.sendMessage(ChatColor.GREEN + "Changez de serveur afin de voir un changement.");
	}
}