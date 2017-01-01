package fr.badblock.bungee.data.players.threading;

import java.lang.Thread.State;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.data.players.BadOfflinePlayer;
import fr.badblock.commons.technologies.mongodb.MongoService;

public class PlayerDataWorker {

	private static List<PlayerDataThread> threads = new ArrayList<>();
	// TODO Set this in config
	private static int			threadsMax = 32;

	static {
		for (int id = 0; id < threadsMax; id++) {
			PlayerDataThread thread = new PlayerDataThread(id);
			threads.add(thread);
		}
	}

	public static void save(BadOfflinePlayer player) {
		if (player == null) return;
		// Priority to available threads ;P
		List<PlayerDataThread> availableThreads = threads.stream().filter(thread -> thread.getState().equals(State.WAITING)).collect(Collectors.toList());
		PlayerDataThread dataThread = null;
		if (availableThreads == null || availableThreads.isEmpty()) dataThread = threads.get(new SecureRandom().nextInt(threads.size()));
		else dataThread = availableThreads.get(new SecureRandom().nextInt(availableThreads.size()));
		if (dataThread == null) System.out.println("No available thread to save " + player.getName() + " :o");
		else dataThread.addPlayer(player);
	}

	public static void populate(BadOfflinePlayer badOfflinePlayer) {
		BadBungee bungee = BadBungee.getInstance();
		MongoService mongoService = bungee.getMongoService();
		DBCollection table = mongoService.db().getCollection("players");
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("name", badOfflinePlayer.getName());
		DBCursor cursor = table.find(searchQuery);
		if (cursor.hasNext()) {
			badOfflinePlayer.setData((BasicDBObject) cursor.next());
		}else{
			badOfflinePlayer.setData(new BasicDBObject());
		}
	}

}
