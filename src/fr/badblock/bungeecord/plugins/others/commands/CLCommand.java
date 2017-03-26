package fr.badblock.bungeecord.plugins.others.commands;

import java.security.SecureRandom;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import fr.badblock.bungeecord.plugins.others.database.BadblockDatabase;
import fr.badblock.bungeecord.plugins.others.database.Request;
import fr.badblock.bungeecord.plugins.others.database.Request.RequestType;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class CLCommand extends Command {

	private static SecureRandom randomThreadId = new SecureRandom();

	public CLCommand() {
		super("cl", "others.mod.track");
	}

	@SuppressWarnings("deprecation")
	public void execute(CommandSender sender, String[] args) {
		if (args.length > 1) {
			sender.sendMessage("§cUtilisation: /cl <page>");
			return;
		}
		int page = 0;
		if (args.length == 1) {
			try {
				page = Integer.parseInt(args[0]);
			}catch(Exception error) {
				sender.sendMessage("§cLe numéro de page doit être un nombre.");
				return;
			}
		}
		if (page < 1) page = 1;
		final int finalCurrentPage = page;
		double nbPerPage = 10;
		new Thread("cl_" + randomThreadId.nextInt(Integer.MAX_VALUE)) {
			@Override
			public void run() {
				Map<String, Long> bestCheaters = new HashMap<>();
				HashSet<String> players = new HashSet<>();
				BadblockDatabase.getInstance().addSyncRequest(new Request("SELECT * FROM cheatReports WHERE timestamp > '" + System.currentTimeMillis() + "' AND lastLogin > '" + System.currentTimeMillis() + "'", RequestType.GETTER) {
					@Override
					public void done(ResultSet resultSet) {
						try {
							while (resultSet.next()) {
								players.add(resultSet.getString("pseudo"));
							}
						}catch(Exception error2) {
							error2.printStackTrace();
						}
					}
				});
				double total = players.size();
				int maxPages = (int) Math.ceil(total / nbPerPage);
				int currentPage = finalCurrentPage;
				if (currentPage > maxPages) currentPage = maxPages;
				int firstEntry = (int) ((currentPage - 1) * nbPerPage);
				BadblockDatabase.getInstance().addSyncRequest(new Request("SELECT * FROM cheatReports WHERE timestamp > '" + System.currentTimeMillis() + "' AND lastLogin > '" + System.currentTimeMillis() + "' ORDER BY id DESC;", RequestType.GETTER) {
					@Override
					public void done(ResultSet resultSet) {
						try {
							while (resultSet.next()) {
								String pseudo = resultSet.getString("pseudo");
								long nb = !bestCheaters.containsKey(pseudo) ? 1 : bestCheaters.get(pseudo) + 1;
								bestCheaters.put(pseudo, nb);
							}
						}catch(Exception error2) {
							error2.printStackTrace();
						}
					}
				});
				List<Entry<String, Long>> entries = entriesSortedByValues(bestCheaters);
				int id = 0;
				sender.sendMessage("§e------- Top report cheat décroissant (Page n°" + currentPage + ") -------");
				for (Entry<String, Long> entry : entries) {
					id++;
					if (id >= firstEntry && id <= firstEntry + nbPerPage)
						sender.sendMessage("§b" + id + ". §7" + entry.getKey());
				}
				sender.sendMessage("§e-------------------------------------------");
			}
		}.start();
	}

	static <K,V extends Comparable<? super V>> List<Entry<K, V>> entriesSortedByValues(Map<K,V> map) {
		List<Entry<K,V>> sortedEntries = new ArrayList<Entry<K,V>>(map.entrySet());
		Collections.sort(sortedEntries,  new Comparator<Entry<K,V>>() {
			@Override
			public int compare(Entry<K,V> e1, Entry<K,V> e2) {
				return e2.getValue().compareTo(e1.getValue());
			}
		});
		return sortedEntries;
	}

}