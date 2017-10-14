package fr.badblock.bungeecord.plugins.others.commands;

import java.sql.ResultSet;
import java.util.Date;

import fr.badblock.bungeecord.plugins.others.BadBlockBungeeOthers;
import fr.badblock.bungeecord.plugins.others.database.BadblockDatabase;
import fr.badblock.bungeecord.plugins.others.database.Request;
import fr.badblock.bungeecord.plugins.others.database.Request.RequestType;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class GNickCommand extends Command {

	public GNickCommand() {
		super("gnick", "others.gnick");
	}

	@SuppressWarnings("deprecation")
	public void execute(CommandSender sender, String[] args) {
		if (args.length != 1) {
			sender.sendMessage("§cUtilisation: /gnick <nom/off>");
			return;
		}
		if (!(sender instanceof ProxiedPlayer)) {
			sender.sendMessage("§cVous devez être sur le serveur pour pouvoir utiliser cette commande.");
			return;
		}
		ProxiedPlayer proxiedPlayer = (ProxiedPlayer) sender;
		String name = args[0];
		String playerName = sender.getName().toLowerCase();
		BadblockDatabase.getInstance().addRequest(new Request("SELECT * FROM nick WHERE playerName = '" + BadblockDatabase.getInstance().mysql_real_escape_string(playerName) + "'", RequestType.GETTER)
		{
			@Override
			public void done(ResultSet resultSet)
			{
				try
				{
					if (name.equalsIgnoreCase("off"))
					{
						if (resultSet.next())
						{
							BadblockDatabase.getInstance().addRequest(new Request("DELETE FROM nick WHERE playerName = '" + BadblockDatabase.getInstance().mysql_real_escape_string(playerName) + "'", RequestType.SETTER));
							BadblockDatabase.getInstance().addSyncRequest(new Request("INSERT INTO nickLogs(`date`, `timestamp`, `ip`, `playerName`, `nick`, `type`) VALUES('" + 
									BadblockDatabase.getInstance().mysql_real_escape_string(BadBlockBungeeOthers.getInstance().simpleDateFormat.format(new Date())) + "', '" + 
									System.currentTimeMillis() + "', '" + 
									BadblockDatabase.getInstance().mysql_real_escape_string(proxiedPlayer.getAddress().getAddress().getHostAddress()) + "', '" + 
									BadblockDatabase.getInstance().mysql_real_escape_string(proxiedPlayer.getName()) + "', '" + 
									BadblockDatabase.getInstance().mysql_real_escape_string(resultSet.getString("nick")) + "', 'REMOVE')", RequestType.SETTER));
							System.out.println("INSERT INTO nickLogs(`date`, `timestamp`, `ip`, `playerName`, `nick`, `type`) VALUES('" + 
									BadblockDatabase.getInstance().mysql_real_escape_string(BadBlockBungeeOthers.getInstance().simpleDateFormat.format(new Date())) + "', '" + 
									System.currentTimeMillis() + "', '" + 
									BadblockDatabase.getInstance().mysql_real_escape_string(proxiedPlayer.getAddress().getAddress().getHostAddress()) + "', '" + 
									BadblockDatabase.getInstance().mysql_real_escape_string(proxiedPlayer.getName()) + "', '" + 
									BadblockDatabase.getInstance().mysql_real_escape_string(resultSet.getString("nick")) + "', 'REMOVE')");
							sender.sendMessage("§aVotre surnom a été supprimé.");
							sender.sendMessage("§bChangez de serveur pour que l'effet soit immédiat.");
						}
						else
						{
							sender.sendMessage("§cVous n'avez aucun surnom.");
							sender.sendMessage("§bSi vous avez retiré un surnom récemment, changez de serveur pour que l'effet soit immédiat.");
						}
					}
					else
					{
						if (resultSet.next())
						{
							if (name.equalsIgnoreCase(resultSet.getString("nick")))
							{
								sender.sendMessage("§cVous avez déjà ce surnom.");
								return;
							}
						}
						BadblockDatabase.getInstance().addRequest(new Request("SELECT COUNT(pseudo) AS count FROM friends WHERE pseudo = '" + BadblockDatabase.getInstance().mysql_real_escape_string(name) + "'", RequestType.GETTER)
						{
							@Override
							public void done(ResultSet resultSet)
							{
								try
								{
									if (resultSet.next())
									{
										int count = resultSet.getInt("count");
										if (count == 0)
										{
											BadblockDatabase.getInstance().addRequest(new Request("SELECT COUNT(nick) AS count FROM nick WHERE nick = '" + BadblockDatabase.getInstance().mysql_real_escape_string(name) + "'", RequestType.GETTER)
											{
												@Override
												public void done(ResultSet resultSet)
												{
													try
													{
														if (resultSet.next())
														{
															int count = resultSet.getInt("count");
															if (count == 0)
															{
																sender.sendMessage("§3Changement de surnom...");
																BadblockDatabase.getInstance().addSyncRequest(new Request("DELETE FROM nick WHERE playerName = '" + BadblockDatabase.getInstance().mysql_real_escape_string(playerName) + "'", RequestType.SETTER));
																BadblockDatabase.getInstance().addSyncRequest(new Request("INSERT INTO nick(playerName, nick) VALUES('" + BadblockDatabase.getInstance().mysql_real_escape_string(playerName) + "', '" + BadblockDatabase.getInstance().mysql_real_escape_string(name) + "')", RequestType.SETTER));
																BadblockDatabase.getInstance().addSyncRequest(new Request("INSERT INTO nickLogs(`date`, `timestamp`, `ip`, `playerName`, `nick`, `type`) VALUES('" + 
																		BadblockDatabase.getInstance().mysql_real_escape_string(BadBlockBungeeOthers.getInstance().simpleDateFormat.format(new Date())) + "', '" + 
																		System.currentTimeMillis() + "', '" + 
																		BadblockDatabase.getInstance().mysql_real_escape_string(proxiedPlayer.getAddress().getAddress().getHostAddress()) + "', '" + 
																		BadblockDatabase.getInstance().mysql_real_escape_string(proxiedPlayer.getName()) + "', '" + 
																		BadblockDatabase.getInstance().mysql_real_escape_string(name) + "', 'EDIT')", RequestType.SETTER));
																System.out.println("INSERT INTO nickLogs(`date`, `timestamp`, `ip`, `playerName`, `nick`, `type`) VALUES('" + 
																		BadblockDatabase.getInstance().mysql_real_escape_string(BadBlockBungeeOthers.getInstance().simpleDateFormat.format(new Date())) + "', '" + 
																		System.currentTimeMillis() + "', '" + 
																		BadblockDatabase.getInstance().mysql_real_escape_string(proxiedPlayer.getAddress().getAddress().getHostAddress()) + "', '" + 
																		BadblockDatabase.getInstance().mysql_real_escape_string(proxiedPlayer.getName()) + "', '" + 
																		BadblockDatabase.getInstance().mysql_real_escape_string(name) + "', 'EDIT')");
																sender.sendMessage("§aVotre surnom a été changé en §d" + name + "§a.");
																sender.sendMessage("§bChangez de serveur pour que l'effet soit immédiat.");
															}
															else
															{
																sender.sendMessage("§cQuelqu'un s'est déjà renommé en §d" + name + "§a.");
															}
														}
														else
														{
															sender.sendMessage("§cUne erreur est survenue lors du changement de nick. Erreur 4.");
														}
													}
													catch (Exception error)
													{
														error.printStackTrace();
														sender.sendMessage("§cUne erreur est survenue lors du changement de nick. Erreur 2.");
													}
												}
											});
										}
										else
										{
											sender.sendMessage("§cLe pseudo §d" + name + " §cexiste déjà.");
										}
									}
									else
									{
										sender.sendMessage("§cUne erreur est survenue lors du changement de nick. Erreur 1.");
									}
								}
								catch (Exception error)
								{
									error.printStackTrace();
									sender.sendMessage("§cUne erreur est survenue lors du changement de nick. Erreur 2.");
								}
							}
						});
					}
				}
				catch (Exception error)
				{
					error.printStackTrace();
					sender.sendMessage("§cUne erreur est survenue lors du changement de nick. Erreur 3.");
				}
			}
		});
	}

}