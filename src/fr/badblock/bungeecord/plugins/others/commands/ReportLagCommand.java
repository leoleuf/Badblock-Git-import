package fr.badblock.bungeecord.plugins.others.commands;

import java.util.Date;

import fr.badblock.bungeecord.plugins.others.BadBlockBungeeOthers;
import fr.badblock.bungeecord.plugins.others.database.BadblockDatabase;
import fr.badblock.bungeecord.plugins.others.database.Request;
import fr.badblock.bungeecord.plugins.others.database.Request.RequestType;
import fr.badblock.utils.Flags;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ReportLagCommand extends Command {

	public ReportLagCommand() {
		super("reportlag", "", "lagreport", "lagr", "rlag");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length != 0) {
			sender.sendMessage("§cUtilisation: /lagreport");
			return;
		}
		if (!(sender instanceof ProxiedPlayer))
		{
			sender.sendMessage("§cVous devez être un joueur pour pouvoir exécuter cette commande.");
			return;
		}
		ProxiedPlayer proxiedPlayer = (ProxiedPlayer) sender;
		String playerName = sender.getName();
		String flagName = "lagreport_" + playerName;
		if (!Flags.has(flagName))
		{
			Flags.set(flagName, 300_000);
			String serverName = proxiedPlayer.getServer() != null && proxiedPlayer.getServer().getInfo() != null ? proxiedPlayer.getServer().getInfo().getName() : "unknown";
			String date = BadBlockBungeeOthers.getInstance().simpleDateFormat.format(new Date());
			BadblockDatabase.getInstance().addRequest(new Request(
					"INSERT INTO lagReports(playerName, server, date, timestamp) VALUES('" + playerName
					+ "', '" + serverName + "', '" + date 
					+ "', '" + System.currentTimeMillis() + "')", RequestType.SETTER));
			sender.sendMessage("§6[INFO] §aVous avez signalé un lag.");
			sender.sendMessage("§6[INFO] §aNous vous remercions de l'intérêt que vous portez à BadBlock !");
		}
		else
		{
			sender.sendMessage("§6[INFO] §cVous avez déjà signalé un lag récemment.");
		}
	}

}