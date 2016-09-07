package fr.badblock.ladder.plugins.utils.commands;

import fr.badblock.ladder.api.chat.ChatColor;
import fr.badblock.ladder.api.commands.Command;
import fr.badblock.ladder.api.entities.CommandSender;

public class CommandVersion extends Command {
	private int last = 0;
	
	public final String[] MESSAGES = new String[]{
		ChatColor.GREEN + "La version la plus r�cente du syst�me d'exploitation de Microsoft est Windows 10.",
		ChatColor.GREEN + "BadBlock est actuellement dans la derni�re version qui existe ! Si, si, je vous jure !",
		ChatColor.GREEN + "BadBlock est en version 1.03.828 (b�ta). Informatif, n'est-il point ?",
		ChatColor.GREEN + "Le r�chauffement climatique est (en partie) d� � ceux qui produisent du CO2 dans l'unique espoir de trouver une version.",
		ChatColor.GREEN + "BadBlock utilise la version (bzzzz) de MineCraft. En vous remerciant de votre appel.",
		ChatColor.GREEN + "Bravo ! Vous avez r�ussi � infiltrer les profondeurs de BadBlock en tapant cette commande ! Ou pas.",
		ChatColor.GREEN + "Aux �mes bien n�es, la valeur n'attend point le nombre des versions.",
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
