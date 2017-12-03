package fr.badblock.ladder.plugins.others.commands.mod.punish;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.entities.CommandSender;
import fr.badblock.ladder.api.entities.OfflinePlayer;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.ladder.api.utils.Time;
import fr.badblock.ladder.plugins.others.database.BadblockDatabase;
import fr.badblock.ladder.plugins.others.database.Request;
import fr.badblock.ladder.plugins.others.database.Request.RequestType;

public class CommandSanction extends SanctionCommand {

	public static CommandSanction instance;

	public CommandSanction() {
		super("sanction", "ladder.command.sanction", "case", "casier", "sn");
		instance = this;
	}

	@Override
	public void run(CommandSender sender, String[] args) {
		if (args.length == 0) {
			sender.sendMessage("§cUsage: /sanction <pseudo>");
			return;
		}
		if (args[0].equals("")) {
			sender.sendMessage("§cLe pseudo ne doit pas être vide.");
			return;
		}
		Ladder ladder = Ladder.getInstance();
		OfflinePlayer offlinePlayer = ladder.getOfflinePlayer(args[0]);
		// Unknown player?
		if (!offlinePlayer.hasPlayed()) {
			sender.sendMessage("§c➤ " + args[0] + " ne s'est jamais connecté sur le serveur.");
			return;
		}

		// If it's a player (not a console, etc.)
		if (sender instanceof Player) {
			Player banner = (Player) sender;
			int bannerLevel = ModUtils.getLevel(banner);
			int bannedLevel = ModUtils.getLevel(offlinePlayer);
			// Rank level
			if (bannerLevel != 100 && bannerLevel <= bannedLevel
					&& !banner.getName().equalsIgnoreCase(offlinePlayer.getName())) {
				sender.sendMessage(
						"§c➤ Ce joueur est plus haut gradé/au même grade que vous, impossible de voir ses sanctions !");
				return;
			}
		}

		new Thread() {
			@Override
			public void run() {
				/*
				 * sender.sendMessage("§7➤ Sanctions §aactives §7de §b" +
				 * offlinePlayer.getName() + " §7:"); try { Thread.sleep(50); }
				 * catch (InterruptedException e) { e.printStackTrace(); } for
				 * (String sanction : sanctions) { sender.sendMessage(sanction);
				 * try { Thread.sleep(50); } catch (InterruptedException e) {
				 * e.printStackTrace(); } }
				 */
				BadblockDatabase.getInstance()
				.addRequest(new Request(
						"SELECT * FROM sanctions WHERE pseudo = '" + BadblockDatabase.getInstance()
						.mysql_real_escape_string(offlinePlayer.getName()) + "' ORDER BY id ASC;",
						RequestType.GETTER) {
					@Override
					public void done(ResultSet resultSet) {
						try {
							List<String> activeTypes = new ArrayList<>();
							boolean first = true;
							while (resultSet.next()) {
								if (first) {
									first = false;
									sender.sendMessage(
											"§7➤ §bCasier §7de §b" + offlinePlayer.getName() + " §7:");
									Thread.sleep(100);
								}
								String active = "§cINACTIVE";
								String sanction = "?";
								String type = resultSet.getString("type");
								long timestamp = resultSet.getLong("timestamp");
								long expire = resultSet.getLong("expire");
								String banner = resultSet.getString("banner");
								String reason = resultSet.getString("reason");
								String date = resultSet.getString("date");
								String time = "";
								if (type.equals("mute")) {
									sanction = "Bâillon";
									if ((expire > System.currentTimeMillis() || expire == -1)
											&& reason.equals(offlinePlayer.getAsPunished().getMuteReason())
											&& banner.equals(offlinePlayer.getAsPunished().getMuter())
											&& offlinePlayer.getAsPunished().isMute()
											&& offlinePlayer.getAsPunished().getMuteEnd() == expire)
										active = "§aON";
								}
								if (type.equals("warn")) {
									sanction = "Avertissement";
									active = "§7DONE";
								}
								if (type.equals("muteip")) {
									sanction = "BâillonIP";
									if ((expire > System.currentTimeMillis() || expire == -1)
											&& offlinePlayer.getIpAsPunished().isMute()
											&& reason.equals(offlinePlayer.getIpAsPunished().getMuteReason())
											&& banner.equals(offlinePlayer.getIpAsPunished().getMuter())
											&& offlinePlayer.getIpAsPunished().getMuteEnd() == expire)
										active = "§aON";
								}
								if (type.equals("ban") || type.equals("tempban")) {
									sanction = "Bannissement";
									if ((expire > System.currentTimeMillis() || expire == -1)
											&& offlinePlayer.getAsPunished().isBan()
											&& reason.equals(offlinePlayer.getAsPunished().getBanReason())
											&& banner.equals(offlinePlayer.getAsPunished().getBanner())
											&& offlinePlayer.getAsPunished().getBanEnd() == expire)
										active = "§aON";
								}
								if (type.equals("banip") || type.equals("tempbanip")) {
									sanction = "BannissementIP";
									if ((expire > System.currentTimeMillis() || expire == -1)
											&& offlinePlayer.getIpAsPunished().isBan()
											&& reason.equals(offlinePlayer.getIpAsPunished().getBanReason())
											&& banner.equals(offlinePlayer.getIpAsPunished().getBanner())
											&& offlinePlayer.getIpAsPunished().getBanEnd() == expire)
										active = "§aON";
								}
								if (type.equals("§aON")) {
									if (activeTypes.contains(sanction))
										continue;
									activeTypes.add(sanction);
								}
								if (type.equals("kick")) {
									active = "§7DONE";
									sanction = "Kick";
								}
								if (type.equals("unban")) {
									sanction = "Débannissement";
									active = "§7DONE";
								}
								if (type.equals("unbanip")) {
									sanction = "DébannissementIP";
									active = "§7DONE";
								}
								if (type.equals("unmute")) {
									sanction = "Débâillonnement";
									active = "§7DONE";
								}
								if (active.equals("§aON") && type.equals("mute"))
									time = offlinePlayer.getAsPunished().buildMuteTime().replace("à vie",
											"Permanent");
								else if (active.equals("§aON") && type.equals("muteip"))
									time = offlinePlayer.getIpAsPunished().buildMuteTime().replace("à vie",
											"Permanent");
								else if (active.equals("§aON") && type.equals("tempban"))
									time = offlinePlayer.getAsPunished().buildBanTime().replace("à vie",
											"Permanent");
								else if (active.equals("§aON") && type.equals("tempbanip"))
									time = offlinePlayer.getIpAsPunished().buildBanTime().replace("à vie",
											"Permanent");
								else if (active.equals("§aON"))
									time = "?";
								else if (active.equals("§cINACTIVE") && expire != -1)
									time = buildTime(expire, timestamp);
								else
									time = "?";
								sender.sendMessage("§b➤ §8[" + active + "§8] §7" + sanction + " — §b"
										+ offlinePlayer.getName()
										+ (!reason.isEmpty() ? " §7pour §b" + reason : "") + "§7 par §b"
										+ banner + (!time.equals("?") ? " §7(§b" + time + "§7)" : "") + " [§b"
										+ date + "§7]");
								Thread.sleep(100);
							}
							if (first) {
								sender.sendMessage("§c➤ " + offlinePlayer.getName() + " a un casier blanc comme neige.");
							}
						} catch (Exception error) {
							error.printStackTrace();
						}
					}
				});
			}
		}.start();
	}

	public String buildTime(long expire, long time) {
		if (time != -1L)
			return Time.MILLIS_SECOND.toFrench(Math.abs(expire - time), Time.MINUTE, Time.DAY);
		return "à vie";
	}

}