package fr.badblock.bungee.data.ip.utils;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.data.ip.BadIpData;
import fr.badblock.commons.technologies.mongodb.MongoService;

public class IpDataUtils {

	public static void saveData(BadIpData ipData) {
		BadBungee bungee = BadBungee.getInstance();
		MongoService mongoService = bungee.getMongoService();
		DBCollection table = mongoService.db().getCollection("ips");
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("ip", ipData.getIp());
		table.findAndRemove(searchQuery);
		BasicDBObject dbObject = new BasicDBObject();
		dbObject.putAll(ipData.getData());
		table.insert(dbObject);
	}
	
}
