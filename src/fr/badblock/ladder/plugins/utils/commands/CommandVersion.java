package fr.badblock.ladder.plugins.utils.commands;

import fr.badblock.ladder.api.chat.ChatColor;
import fr.badblock.ladder.api.commands.Command;
import fr.badblock.ladder.api.entities.CommandSender;

public class CommandVersion extends Command {
	private int last = 0;
	
	public final String[] MESSAGES = new String[]{
		ChatColor.GREEN + "La version la plus récente du système d'exploitation de Microsoft est Windows 10.",
		ChatColor.GREEN + "BadBlock est actuellement dans la dernière version qui existe ! Si, si, je vous jure !",
		ChatColor.GREEN + "BadBlock est en version 1.03.828 (bêta). Informatif, n'est-il point ?",
		ChatColor.GREEN + "Le réchauffement climatique est (en partie) dû à ceux qui produisent du CO2 dans l'unique espoir de trouver une version.",
		ChatColor.GREEN + "BadBlock utilise la version (bzzzz) de MineCraft. En vous remerciant de votre appel.",
		ChatColor.GREEN + "Bravo ! Vous avez réussi à infiltrer les profondeurs de BadBlock en tapant cette commande ! Ou pas.",
		ChatColor.GREEN + "Aux âmes bien nées, la valeur n'attend point le nombre des versions.",
		ChatColor.GREEN + "Ma version ? Et toi je t'en pose des questions ?"
	};
	
	public CommandVersion() {
		super("version", null, "ver", "about", "bukkit:about", "bukkit:ver", "bukkit:version");
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
