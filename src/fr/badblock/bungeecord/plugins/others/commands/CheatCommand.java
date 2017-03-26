package fr.badblock.bungeecord.plugins.others.commands;

import java.sql.ResultSet;
import java.util.UUID;

import com.google.gson.Gson;

import fr.badblock.bungeecord.api.CommandSender;
import fr.badblock.bungeecord.api.plugin.Command;
import fr.badblock.bungeecord.plugins.others.BadBlockBungeeOthers;
import fr.badblock.bungeecord.plugins.others.database.BadblockDatabase;
import fr.badblock.bungeecord.plugins.others.database.Request;
import fr.badblock.bungeecord.plugins.others.database.Request.RequestType;
import fr.badblock.bungeecord.plugins.others.guardian.objects.GuardianReport;
import fr.badblock.common.commons.technologies.rabbitmq.RabbitPacketType;
import fr.badblock.common.commons.utils.Encodage;

public class CheatCommand extends Command {

	public CheatCommand() {
		super("cheat");
	}

	@SuppressWarnings("deprecation")
	public void execute(CommandSender sender, String[] args) {
		if (args.length != 1) {
			sender.sendMessage("§cUtilisation: /cheat <pseudo>");
			return;
		}
		String playerName = args[0];
		if (playerName.equalsIgnoreCase(sender.getName())) {
			sender.sendMessage("§cLe trouble de la personnalité masochiste est référencée dans le DSM-IV. Il est important de consulter avant de vouloir signaler soi-même ses messages à des autorités compétentes.");
			return;
		}
		BadblockDatabase.getInstance().addSyncRequest(new Request("SELECT * FROM friends WHERE pseudo = '" + BadblockDatabase.getInstance().mysql_real_escape_string(playerName) + "' AND uuid IS NOT NULL AND uuid != 'NULL' AND uuid != '';", RequestType.GETTER) {
			@Override
			public void done(ResultSet resultSeta) {
				try {
					if (resultSeta.next()) {
						BadblockDatabase.getInstance().addSyncRequest(new Request("SELECT COUNT(*) AS count FROM cheatReports WHERE pseudo = '" + BadblockDatabase.getInstance().mysql_real_escape_string(playerName) + "' AND byPlayer = '" + BadblockDatabase.getInstance().mysql_real_escape_string(sender.getName()) + "' AND timestamp > '" + System.currentTimeMillis() + "'", RequestType.GETTER) {
							@Override
							public void done(ResultSet resultSet) {
								try {
									if (resultSet.next()) {
										int count = resultSet.getInt("count");
										String broadcastMessage = "§f(§7Guardian§a3.0§f) §4[REPORT] §6[" + count + " signalement" + (count > 1 ? "s" : "") + "] §e" + playerName + " §c[[SERVER]]";
										GuardianReport guardianReport = new GuardianReport(UUID.fromString(resultSeta.getString("uuid")), broadcastMessage);
										BadBlockBungeeOthers.getInstance().getRabbitService().sendPacket("guardian.report", new Gson().toJson(guardianReport), Encodage.UTF8, RabbitPacketType.MESSAGE_BROKER, 5000, false);
										if (count == 0) {
											// Il peut report pour cheat
											BadblockDatabase.getInstance().addSyncRequest(new Request("INSERT INTO cheatReports(pseudo, byPlayer, timestamp) VALUES('" + BadblockDatabase.getInstance().mysql_real_escape_string(playerName) + "', '" + BadblockDatabase.getInstance().mysql_real_escape_string(sender.getName()) + "', '" + (System.currentTimeMillis() + 86400000) + "')", RequestType.SETTER));
											sender.sendMessage("§aVous avez signalé ce joueur. Les modérateurs vérifieront votre signalement prochainement.");
										}else{
											sender.sendMessage("§cVous avez déjà signalé ce joueur ces dernières 24 heures.");
										}
									}else{
										sender.sendMessage("§cErreur.");
									}
								}catch(Exception error2) {
									error2.printStackTrace();
								}
							}
						});
					}else{
						sender.sendMessage("§cCe joueur n'existe pas.");
					}
				}catch(Exception error) {
					error.printStackTrace();
				}
			}
		});
	}

}