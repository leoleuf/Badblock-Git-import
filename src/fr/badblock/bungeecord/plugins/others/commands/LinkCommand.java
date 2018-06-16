package fr.badblock.bungeecord.plugins.others.commands;

import java.sql.ResultSet;

import fr.badblock.bungeecord.plugins.others.database.Request;
import fr.badblock.bungeecord.plugins.others.database.Request.RequestType;
import fr.badblock.bungeecord.plugins.others.database.WebDatabase;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class LinkCommand extends Command {

	public LinkCommand() {
		super("link");
	}

	public void execute(CommandSender sender, String[] args) {
		if (args.length != 1) {
			WebDatabase.getInstance()
					.addRequest(new Request(
							"SELECT linked FROM joueurs WHERE pseudo = '"
									+ WebDatabase.getInstance().mysql_real_escape_string(sender.getName()) + "'",
							RequestType.GETTER) {
						@SuppressWarnings("deprecation")
						@Override
						public void done(ResultSet resultSet) {
							try {
								if (resultSet.next()) {
									boolean linked = resultSet.getBoolean("linked");
									if (!linked) {
										sender.sendMessage(
												"§9Votre compte n'est pas relié à votre compte sur le site web. Afin de le relier, nous vous invitons à faire la commande suivante :");
										sender.sendMessage("  §e/link <email>");
										sender.sendMessage("§9(utilisez l'adresse email inscrite avec votre pseudo "
												+ sender.getName() + " sur le site)");
										sender.sendMessage(
												"§c/!\\ Une fois votre compte relié, il ne pourra plus être délié /!\\");
										sender.sendMessage(
												"§3Relier votre compte permet de faire des achats, gérer votre mot de passe, vos connexions... depuis le site!");
									} else {
										sender.sendMessage("§cVotre compte est déjà relié sur le site web.");
									}
								} else {
									sender.sendMessage(
											"§cAucun compte n'existe avec votre pseudonyme sur le site https://badblock.fr.");
								}
							} catch (Exception error) {
								error.printStackTrace();
							}
						}
					});
		} else {
			String email = args[0];
			WebDatabase.getInstance()
					.addRequest(new Request(
							"SELECT linked, mail FROM joueurs WHERE pseudo = '"
									+ WebDatabase.getInstance().mysql_real_escape_string(sender.getName()) + "'",
							RequestType.GETTER) {
						@SuppressWarnings("deprecation")
						@Override
						public void done(ResultSet resultSet) {
							try {
								if (resultSet.next()) {
									boolean linked = resultSet.getBoolean("linked");
									if (!linked) {
										String mail = resultSet.getString("mail");
										if (!email.equalsIgnoreCase(mail)) {
											sender.sendMessage(
													"§cL'email entré n'est pas le même que celui avec lequel vous vous êtes inscrit sur le site web.");
										} else {
											WebDatabase.getInstance()
													.addSyncRequest(new Request(
															"UPDATE joueurs SET linked = 'true' WHERE pseudo = '"
																	+ WebDatabase.getInstance()
																			.mysql_real_escape_string(sender.getName())
																	+ "'",
															RequestType.SETTER));
											sender.sendMessage(
													"§aVotre compte est désormais relié sur le site web! Vous avez désormais le plein pouvoir de votre compte en jeu depuis le site.");
										}
									} else {
										sender.sendMessage("§cVotre compte est déjà relié sur le site web.");
									}
								} else {
									sender.sendMessage(
											"§cAucun compte n'existe avec votre pseudonyme sur le site https://badblock.fr.");
								}
							} catch (Exception error) {
								error.printStackTrace();
							}
						}
					});
		}
	}

}