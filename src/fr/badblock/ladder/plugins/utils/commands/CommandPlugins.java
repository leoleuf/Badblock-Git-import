package fr.badblock.ladder.plugins.utils.commands;

import fr.badblock.ladder.api.chat.ChatColor;
import fr.badblock.ladder.api.commands.Command;
import fr.badblock.ladder.api.entities.CommandSender;

public class CommandPlugins extends Command {
	private int last = 0;
	
	public final String[] MESSAGES = new String[]{
		ChatColor.WHITE + "Plugins (beaucoup): " + ChatColor.GREEN + "Plein de plugins que tu ne verras pas ! :p",
		ChatColor.WHITE + "Plugins (beaucoup): " + ChatColor.GREEN + "Des plugins qui peuvent te bannir, %name% ! :)",
		ChatColor.WHITE + "Plugins (3): " + ChatColor.GREEN + "Essentials, Vault, PermissionsEX, ... Tu y as cru, hein ?",
		ChatColor.WHITE + "Plugins (beaucoup): " + ChatColor.GREEN + "Des plugins d'amour ... #pluginophilie",
		ChatColor.WHITE + "Plugins (beaucoup): " + ChatColor.GREEN + "Bad(bzz). La communication à malheureusement été interrompue.",
		ChatColor.WHITE + "Plugins (beaucoup): " + ChatColor.GREEN + "Félicitations ! Vous êtes la 10.000ème personne à avoir voulu voir nos plugins aujourd'hui ! Vous avez gagné un iPhone 6S Gold ! Ah non, en fait.",
		ChatColor.WHITE + "Plugins (beaucoup): " + ChatColor.GREEN + "Quand t'as pas d'ami, prend un curly.",
		ChatColor.WHITE + "Plugins (beaucoup): " + ChatColor.GREEN + "A chaque fois qu'un joueur fait /pl, un hérisson meurt. Arrêtons le génocide.",
		ChatColor.WHITE + "Plugins (beaucoup): " + ChatColor.GREEN + "BadBlock est en version 1 ... Ah non, je me suis trompé de commande."
	};
	
	public CommandPlugins() {
		super("plugins", null, "pl", "bukkit:plugins", "bukkit:pl");
	}

	@Override
	public void executeCommand(CommandSender sender, String[] args) {
		if(last == MESSAGES.length)
			last = 0;
		
		String message = MESSAGES[last].replace("%name%", sender.getName());
		sender.sendMessage(message);
		
		last++;
	}
	
	@Override
	public boolean isBypassable(){
		return true;
	}
}
