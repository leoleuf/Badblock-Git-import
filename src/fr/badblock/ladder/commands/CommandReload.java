package fr.badblock.ladder.commands;

import fr.badblock.ladder.Proxy;
import fr.badblock.ladder.api.chat.ChatColor;
import fr.badblock.ladder.api.commands.Command;
import fr.badblock.ladder.api.entities.CommandSender;

public class CommandReload extends Command {
	public CommandReload() {
		super("lreload", "ladder.command.reload");
	}
		
	@Override
	public void executeCommand(CommandSender sender, String[] args) {
		if(args.length == 0){
			sender.sendMessage(ChatColor.RED + "Utilisation : /lreload permissions|motd|all");
		} else if(args[0].equalsIgnoreCase("permissions")) {
			Proxy.getInstance().loadPermissions(true);
			sender.sendMessage(ChatColor.GREEN + "Les permissions ont �t� recharg�s ! Attention, l'envoi de donn�es aux serveurs est volimineux : n'utilisez cette commande qu'en cas d'urgence.");
		} else if(args[0].equalsIgnoreCase("motd")){
			Proxy.getInstance().loadMotd(true);
			sender.sendMessage(ChatColor.GREEN + "Le motd a �t� recharg� !");
		} else if(args[0].equalsIgnoreCase("all")){
			Proxy.getInstance().loadPermissions(true);
			Proxy.getInstance().loadMotd(true);
			
			sender.sendMessage(ChatColor.GREEN + "Les permissions & motd ont �t� recharg�s !");
		} else {
			sender.sendMessage(ChatColor.RED + "Utilisation : /lreload permissions|motd|all");
		}
	}
}
