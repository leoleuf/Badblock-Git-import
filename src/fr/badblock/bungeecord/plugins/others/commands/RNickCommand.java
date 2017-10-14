package fr.badblock.bungeecord.plugins.others.commands;

import java.sql.ResultSet;

import fr.badblock.bungeecord.plugins.others.database.BadblockDatabase;
import fr.badblock.bungeecord.plugins.others.database.Request;
import fr.badblock.bungeecord.plugins.others.database.Request.RequestType;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class RNickCommand extends Command {

	public RNickCommand() {
		super("rnick", "others.rnick");
	}

	@SuppressWarnings("deprecation")
	public void execute(CommandSender sender, String[] args) {
		if (args.length != 1) {
			sender.sendMessage("§cUtilisation: /rnick <surnom>");
			return;
		}
		String name = args[0];
		BadblockDatabase.getInstance().addRequest(new Request("SELECT playerName FROM nick WHERE nick = '" + BadblockDatabase.getInstance().mysql_real_escape_string(name) + "'", RequestType.GETTER)
		{
			@Override
			public void done(ResultSet resultSet)
			{
				try
				{
					if (resultSet.next())
					{
						sender.sendMessage("§bLe vrai pseudo de §e" + name + " §best §d" + resultSet.getString("playerName") + ".");
					}
					else
					{
						sender.sendMessage("§cAucune personne ne porte actuellement le surnom suivant : §d" + name + "§c.");
					}
				}
				catch (Exception error)
				{
					error.printStackTrace();
					sender.sendMessage("§cUne erreur est survenue lors de la révélation de surnom. Erreur 9.");
				}
			}
		});
	}

}