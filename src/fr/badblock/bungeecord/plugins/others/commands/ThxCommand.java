package fr.badblock.bungeecord.plugins.others.commands;

import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.ConcurrentMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import fr.badblock.bungeecord.api.CommandSender;
import fr.badblock.bungeecord.api.plugin.Command;
import fr.badblock.bungeecord.plugins.others.BadBlockBungeeOthers;
import fr.badblock.bungeecord.plugins.others.database.BadblockDatabase;
import fr.badblock.bungeecord.plugins.others.database.Request;
import fr.badblock.bungeecord.plugins.others.database.Request.RequestType;
import fr.badblock.bungeecord.plugins.others.tmputils.PlayerBooster;
import fr.badblock.common.commons.technologies.rabbitmq.RabbitPacketType;
import fr.badblock.common.commons.utils.Encodage;

public class ThxCommand extends Command {

	private static final Type type = new TypeToken<ConcurrentMap<String, PlayerBooster>>() {}.getType();
	private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private static final Map<String, Long> lastExecute = new HashMap<>();

	public ThxCommand() {
		super("thx");
	}

	@SuppressWarnings("deprecation")
	public void execute(CommandSender sender, String[] args) {
		if (lastExecute.containsKey(sender.getName().toLowerCase())) {
			if (lastExecute.get(sender.getName().toLowerCase()) > System.currentTimeMillis()) {
				sender.sendMessage("§c➤ Veuillez patienter 15 secondes entre chaque exécution de cette commande.");
				return;
			}
		}
		lastExecute.put(sender.getName().toLowerCase(), System.currentTimeMillis() + 15_000L);
		BadblockDatabase.getInstance().addRequest(new Request("SELECT value FROM keyValues WHERE `key` = 'booster'", RequestType.GETTER) {

			@Override
			public void done(ResultSet result) {
				try {
					result.next();
					String value = result.getString("value");
					Map<String, PlayerBooster> updatedMap = gson.fromJson(value, type);
					sender.sendMessage("§8------------------------------------------------");
					StringBuilder o = new StringBuilder();
					for (Entry<String, PlayerBooster> entry : updatedMap.entrySet()) {
						if (!entry.getValue().isEnabled() || !entry.getValue().isValid()) continue;
						BadblockDatabase.getInstance().addSyncRequest(new Request("SELECT playerName FROM boosterThanks WHERE expire = '" + entry.getValue().getExpire() + "' AND playerName = '" + sender.getName().toLowerCase() + "'", RequestType.GETTER) {
							@Override
							public void done(ResultSet result) {
								try {
									if (result.next()) {
										sender.sendMessage("§6➤ [" + entry.getValue().getGameName().toUpperCase() + "] §f➠ §e" + entry.getValue().getUsername() + " §f: §cRemerciement déjà apporté");
									}else{
										double randomBadcoins = new Random().nextInt((int) (entry.getValue().getBooster().getMaxRandomBadcoins() - entry.getValue().getBooster().getMinRandomBadcoins())) + entry.getValue().getBooster().getMinRandomBadcoins();
										double randomXP = new Random().nextInt((int) (entry.getValue().getBooster().getMaxRandomXp() - entry.getValue().getBooster().getMinRandomXp())) + entry.getValue().getBooster().getMinRandomXp();
										if (entry.getValue().getAddedBadcoins() >= entry.getValue().getBooster().getMaxBadcoins()) randomBadcoins = 0;
										if (entry.getValue().getAddedXp() >= entry.getValue().getBooster().getMaxXP()) randomXP = 0;
										updatedMap.get(entry.getKey()).setAddedBadcoins((int) (entry.getValue().getAddedBadcoins() + randomBadcoins));
										updatedMap.get(entry.getKey()).setAddedXp((int) (entry.getValue().getAddedXp() + randomXP));
										if (entry.getValue().getAddedBadcoins() > entry.getValue().getBooster().getMaxBadcoins()) updatedMap.get(entry.getKey()).setAddedBadcoins((int) entry.getValue().getBooster().getMaxBadcoins());
										if (entry.getValue().getAddedXp() > entry.getValue().getBooster().getMaxXP()) updatedMap.get(entry.getKey()).setAddedXp((int) entry.getValue().getBooster().getMaxXP());
										if (randomXP != 0 || randomBadcoins != 0) {
											BadblockDatabase.getInstance().addSyncRequest(new Request("INSERT INTO boosterThanks(playerName, expire) VALUES('" + sender.getName().toLowerCase() + "', '" + entry.getValue().getExpire() + "')", RequestType.SETTER));
											BadblockDatabase.getInstance().addSyncRequest(new Request("INSERT INTO debts(playerName, xp, badcoins) VALUES('" + entry.getValue().getUsername().toLowerCase() + "', '" + randomXP + "', '" + randomBadcoins + "')", RequestType.SETTER));
											sender.sendMessage("§6➤ [" + entry.getValue().getGameName().toUpperCase() + "] §f➠ §e" + entry.getValue().getUsername() + " §f: §6" + (int) entry.getValue().getAddedBadcoins() + " BadCoin" + (randomBadcoins > 1 ? "s" : "") + " §7& §3" + (int) entry.getValue().getAddedXp() + " XP");
											double randomBadcoinsPlayer = new Random().nextInt(3) + 2;
											double randomXPPlayer = new Random().nextInt(2) + 2;
											if (o.length() == 0) {
												BadblockDatabase.getInstance().addSyncRequest(new Request("INSERT INTO debts(playerName, xp, badcoins) VALUES('" + sender.getName().toLowerCase() + "', '" + randomXPPlayer + "', '" + randomBadcoinsPlayer + "')", RequestType.SETTER));
												o.append("§a➤ Vous avez gagné §6+" + (int) randomBadcoinsPlayer + " BadCoin" + (randomBadcoinsPlayer > 1 ? "s" : "") + " §7& §3+" + (int) randomXPPlayer + " XP §asuite à votre reconnaissance envers les personnes ayant des Boosters activés (ils vont apparaître sur votre compte dans quelques minutes).");
											}
											if (!entry.getValue().getUsername().toLowerCase().equalsIgnoreCase(sender.getName().toLowerCase())) {
												BadBlockBungeeOthers.getInstance().getRabbitService().sendPacket("playerMessage", entry.getValue().getUsername() + ";§f[§dBooster§f] §e" + sender.getName() + " §7vous remercie ! §6+" + (int) randomBadcoins + " BadCoin" + (randomBadcoins > 1 ? "s" : "") + " §7& §3+" + (int) randomXP + " XP", Encodage.UTF8, RabbitPacketType.PUBLISHER, 100000, false);
											}else{
												BadBlockBungeeOthers.getInstance().getRabbitService().sendPacket("playerMessage", entry.getValue().getUsername() + ";§f[§dBooster§f] §7Vous vous êtes remercié :O §6+" + (int) randomBadcoins + " BadCoin" + (randomBadcoins > 1 ? "s" : "") + " §7& §3+" + (int) randomXP + " XP", Encodage.UTF8, RabbitPacketType.PUBLISHER, 100000, false);
											}
										}else{
											sender.sendMessage("§6➤ [" + entry.getValue().getGameName().toUpperCase() + "] §f➠ §e" + entry.getValue().getUsername() + " §f: §cMaximum atteint §6" + (int) entry.getValue().getBooster().getMaxBadcoins() + " BadCoin" + (entry.getValue().getBooster().getMaxBadcoins() > 1 ? "s" : "") + " §c& §3" + (int) entry.getValue().getBooster().getMaxXP() + " XP");
										}
									}
								} catch (SQLException e) {
									e.printStackTrace();
								}
							}
						});
					}
					BadblockDatabase.getInstance().addSyncRequest(new Request("UPDATE keyValues SET `value` = '" + gson.toJson(updatedMap) + "' WHERE `key` = 'booster'", RequestType.SETTER));
					sender.sendMessage("§8------------------------------------------------");
					if (o.length() > 0) {
						sender.sendMessage(o.toString());
						sender.sendMessage("§8------------------------------------------------");
					}
					result.close(); // don't forget to close it
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}

		});
	}

}