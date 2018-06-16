package fr.badblock.bungeecord.plugins.others.commands;

import java.sql.ResultSet;

import fr.badblock.bungeecord.plugins.others.database.BadblockDatabase;
import fr.badblock.bungeecord.plugins.others.database.Request;
import fr.badblock.bungeecord.plugins.others.database.Request.RequestType;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class CoupleCommand extends Command {

	public CoupleCommand() {
		super("couple", "", "amour");
	}

	@SuppressWarnings("deprecation")
	public void execute(CommandSender sender, String[] args) {
		if (args.length != 2) {
			sender.sendMessage("§cUtilisation: /couple request/accept/remove <nom>");
			return;
		}
		if (!(sender instanceof ProxiedPlayer)) {
			sender.sendMessage("§cVous devez être sur le serveur pour pouvoir utiliser cette commande.");
			return;
		}
		ProxiedPlayer proxiedPlayer = (ProxiedPlayer) sender;
		String type = BadblockDatabase.getInstance().mysql_real_escape_string(args[0]);
		String name = BadblockDatabase.getInstance().mysql_real_escape_string(args[1]);
		String playerName = sender.getName().toLowerCase();
		BadblockDatabase.getInstance().addRequest(new Request(
				"SELECT COUNT(pseudo) AS count FROM friends WHERE pseudo = '" + name + "'", RequestType.GETTER) {
			@Override
			public void done(ResultSet resultSet) {
				try {
					if (resultSet.next()) {
						int count = resultSet.getInt("count");
						if (count == 0) {
							sender.sendMessage("§cLe joueur '" + name + "' ne s'est jamais connecté.");
							return;
						}
						if (type.equalsIgnoreCase("request")) {
							request(proxiedPlayer, name);
							return;
						}
						if (type.equalsIgnoreCase("accept")) {
							accept(proxiedPlayer, name);
							return;
						}
					}
				} catch (Exception error) {
					error.printStackTrace();
				}
			}
		});
	}

	private void request(ProxiedPlayer player, String name) {
		BadblockDatabase.getInstance()
				.addRequest(new Request("SELECT COUNT(pseudo) AS count FROM couples WHERE pseudo = '" + name
						+ "' AND (status = 'WAITING' OR status = 'ACCEPTED')", RequestType.GETTER) {
					@SuppressWarnings("deprecation")
					@Override
					public void done(ResultSet resultSet) {
						try {
							if (resultSet.next()) {
								int count = resultSet.getInt("count");
								if (count != 0) {
									player.sendMessage(
											"§cCe couple est déjà en attente d'acceptation ou est déjà créé.");
									return;
								}
								BadblockDatabase.getInstance().addRequest(
										new Request("INSERT INTO couples(pseudo, fromPlayer, status, timestamp) SET('"
												+ name + "', '" + player.getName() + "', 'WAITING', '"
												+ System.currentTimeMillis() + "')", RequestType.SETTER));
								player.sendMessage("§aVotre demande de couple a été envoyée à " + name + ".");
								player.sendMessage("§eDîtes à votre potentiel amour de vous accepter.");
							}
						} catch (Exception error) {
							error.printStackTrace();
						}
					}
				});
	}

	private void accept(ProxiedPlayer player, String name) {
		BadblockDatabase.getInstance().addRequest(new Request(
				"SELECT COUNT(pseudo) AS count FROM couples WHERE fromPlayer = '" + name + "' AND status = 'WAITING'",
				RequestType.GETTER) {
			@SuppressWarnings("deprecation")
			@Override
			public void done(ResultSet resultSet) {
				try {
					if (resultSet.next()) {
						int count = resultSet.getInt("count");
						if (count == 0) {
							player.sendMessage("§cAucune demande en cours concernant ce couple.");
							return;
						}
						BadblockDatabase.getInstance()
								.addRequest(new Request("UPDATE couples SET status = 'ACCEPTED' WHERE fromPlayer = '"
										+ name + "' AND status = 'WAITING'", RequestType.SETTER));
						player.sendMessage("§aVous avez accepté(e) la demande de " + name + ".");
						player.sendMessage("§bJouez ensemble pour gagner + !");
					}
				} catch (Exception error) {
					error.printStackTrace();
				}
			}
		});
	}

}