package fr.badblock.bungeecord.plugins.others.commands.guardian;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import fr.badblock.bungeecord.plugins.others.BadBlockBungeeOthers;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class GReportsCommand extends Command {
	
	public static List<UUID> exclusions = new ArrayList<>();
	
	public GReportsCommand() {
		super("greports", "guardian.modo", "greport", "guardianreport", "guardians", "guardianreports");
	}
	
	@SuppressWarnings({ "deprecation", "unused" })
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (!(sender instanceof ProxiedPlayer)) {
			sender.sendMessage("§bVous devez être un joueur pour pouvoir exécuter cette commande.");
			return;
		}
		ProxiedPlayer player = (ProxiedPlayer) sender;
		if (player != null) {
			if (!exclusions.contains(player.getUniqueId())) {
				player.sendMessage("§8(§b§lGuardian§8) Reports §bdésactivés§8.");
				exclusions.add(player.getUniqueId());
				return;
			}else{
				player.sendMessage("§8(§b§lGuardian§8) Reports §bactivés§8.");
				exclusions.remove(player.getUniqueId());
				return;
			}
		}
		BadBlockBungeeOthers.getInstance().getProxy().getConsole().sendMessage("[Guardian] " + player.getName() + " issued server command: /greports");
	}
	
}
