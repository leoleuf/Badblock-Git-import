package fr.badblock.bungee.data.ip.threading;

import java.lang.Thread.State;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.data.ip.BadIpData;
import fr.badblock.commons.technologies.mongodb.MongoService;

public class IpDataWorker {

	private static List<IpDataThread> threads = new ArrayList<>();
	// TODO Set this in config
	private static int			threadsMax = 32;

	static {
		for (int id = 0; id < threadsMax; id++) {
			IpDataThread thread = new IpDataThread(id);
			threads.add(thread);
		}
	}

	public static void save(BadIpData badIpData) {
		if (badIpData == null) return;
		// Priority to available threads ;P
		List<IpDataThread> availableThreads = threads.stream().filter(thread -> thread.getState().equals(State.WAITING)).collect(Collectors.toList());
		IpDataThread dataThread = null;
		if (availableThreads == null || availableThreads.isEmpty()) dataThread = threads.get(new SecureRandom().nextInt(threads.size()));
		else dataThread = availableThreads.get(new SecureRandom().nextInt(availableThreads.size()));
		if (dataThread == null) System.out.println("No available thread to save " + badIpData.getIp() + " :o");
		else dataThread.addIpData(badIpData);
	}

	public static void populate(BadIpData badIpData) {
		if (badIpData == null) return;
		BadBungee bungee = BadBungee.getInstance();
		MongoService mongoService = bungee.getMongoService();
		DBCollection table = mongoService.db().getCollection("ips");
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("ip", badIpData.getIp());
		DBCursor cursor = table.find(searchQuery);
		if (cursor.hasNext()) {
			badIpData.setData((BasicDBObject) cursor.next());
		}else{
			badIpData.setData(new BasicDBObject());
		}
	}

}
