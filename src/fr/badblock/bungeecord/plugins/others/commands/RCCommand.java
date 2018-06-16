package fr.badblock.bungeecord.plugins.others.commands;

import java.security.SecureRandom;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import fr.badblock.bungeecord.plugins.ladder.LadderBungee;
import fr.badblock.bungeecord.plugins.others.database.BadblockDatabase;
import fr.badblock.bungeecord.plugins.others.database.Request;
import fr.badblock.bungeecord.plugins.others.database.Request.RequestType;
import fr.badblock.common.protocol.packets.Packet;
import fr.badblock.common.protocol.packets.PacketPlayerChat;
import fr.badblock.common.protocol.packets.PacketPlayerChat.ChatAction;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class RCCommand extends Command {

	private static SecureRandom randomThreadId = new SecureRandom();

	public RCCommand() {
		super("rc", "others.mod.track");
	}

	@SuppressWarnings("deprecation")
	public void execute(CommandSender sender, String[] args) {
		if (args.length > 1) {
			sender.sendMessage("§cUtilisation: /rc <topId>");
			return;
		}
		int topId = 0;
		if (args.length == 1) {
			try {
				topId = Integer.parseInt(args[0]);
			} catch (Exception error) {
				sender.sendMessage("§cLe topId doit être un nombre.");
				return;
			}
		}
		if (topId < 1)
			topId = 1;
		final int finalTopId = topId;
		if (!(sender instanceof ProxiedPlayer)) {
			sender.sendMessage("§cVous devez être sur le serveur pour pouvoir utiliser cette commande.");
			return;
		}
		ProxiedPlayer player = (ProxiedPlayer) sender;
		player.setDisplayName("t_" + player.getName());
		new Thread("rc_" + randomThreadId.nextInt(Integer.MAX_VALUE)) {
			@Override
			public void run() {
				sender.sendMessage("§eRecherche d'un potentiel tricheur...");
				Map<String, Long> bestCheaters = new HashMap<>();
				BadblockDatabase.getInstance()
						.addSyncRequest(new Request(
								"SELECT * FROM cheatReports WHERE timestamp > '" + System.currentTimeMillis()
										+ "' AND lastLogin > '" + System.currentTimeMillis() + "' ORDER BY id DESC;",
								RequestType.GETTER) {
							@Override
							public void done(ResultSet resultSet) {
								try {
									while (resultSet.next()) {
										String pseudo = resultSet.getString("pseudo");
										long nb = !bestCheaters.containsKey(pseudo) ? 1 : bestCheaters.get(pseudo) + 1;
										bestCheaters.put(pseudo, nb);
									}
								} catch (Exception error2) {
									error2.printStackTrace();
								}
							}
						});
				List<Entry<String, Long>> entries = entriesSortedByValues(bestCheaters);
				int id = 0;
				for (Entry<String, Long> entry : entries) {
					id++;
					if (finalTopId == id) {
						sender.sendMessage("§aJoueur trouvé (" + entry.getKey() + " avec " + entry.getValue()
								+ " signalement" + (entry.getValue() > 1 ? "s" : "") + ")");
						sender.sendMessage("§aTéléportation en cours en GhostConnect..");
						Packet packet = new PacketPlayerChat(player.getName(), ChatAction.LADDER_COMMAND,
								"gconnect " + entry.getKey());
						LadderBungee.getInstance().getClient().sendPacket(packet);
						return;
					}
				}
				if (finalTopId > id) {
					sender.sendMessage("§cL'ID " + finalTopId
							+ " est en dehors de la liste des tricheurs, tapez /cl pour la voir.");
				}
			}
		}.start();
	}

	static <K, V extends Comparable<? super V>> List<Entry<K, V>> entriesSortedByValues(Map<K, V> map) {
		List<Entry<K, V>> sortedEntries = new ArrayList<Entry<K, V>>(map.entrySet());
		Collections.sort(sortedEntries, new Comparator<Entry<K, V>>() {
			@Override
			public int compare(Entry<K, V> e1, Entry<K, V> e2) {
				return e2.getValue().compareTo(e1.getValue());
			}
		});
		return sortedEntries;
	}

}