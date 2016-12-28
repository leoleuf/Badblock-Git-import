package fr.badblock.bungee.data.players.utils;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.data.players.BadOfflinePlayer;
import fr.badblock.commons.technologies.mongodb.MongoService;

public class PlayerDataUtils {

	public static void saveData(BadOfflinePlayer player) {
		BadBungee bungee = BadBungee.getInstance();
		MongoService mongoService = bungee.getMongoService();
		DBCollection table = mongoService.db().getCollection("players");
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("name", player.getName());
		table.findAndRemove(searchQuery);
		BasicDBObject dbObject = new BasicDBObject();
		dbObject.putAll(player.getData());
		table.insert(dbObject);
	}
	
}
