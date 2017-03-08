package fr.badblock.bungee.loaders.technologies;

import java.util.List;

import fr.badblock.bungee.loaders.BungeeLoader;
import fr.badblock.bungee.utils.ConfigDefaultUtil;
import fr.badblock.commons.technologies.mongodb.MongoConnector;
import fr.badblock.commons.technologies.mongodb.MongoService;
import net.md_5.bungee.config.Configuration;

public class RabbitMQLoader extends BungeeLoader {

private MongoService mongoService;
	
	public RabbitMQLoader(Configuration configuration) {
		String rabbitMqPrefix = buildFullPrefix(ConfigDefaultUtil.CONFIG_MONGO_PREFIX);
		try {
			List<String> mongoHostnames = configuration.getStringList(mongoPrefix + "hostnames");
			int mongoPort = configuration.getInt(mongoPrefix + "port");
			String mongoUsername = configuration.getString(mongoPrefix + "username");
			String mongoPassword = configuration.getString(mongoPrefix + "password");
			String mongoDatabase = configuration.getString(mongoPrefix + "database");
			String[] mongoHostnamesArray = new String[mongoHostnames.size()];
			mongoHostnamesArray = mongoHostnames.toArray(mongoHostnamesArray);
			mongoService = MongoConnector.getInstance().newService("default", mongoPort, mongoUsername, mongoPassword, mongoDatabase, mongoHostnamesArray);
			done();
		} catch(Exception e){
			e.printStackTrace();
			return;
		}
	}

	public MongoService done() {
		return mongoService;
	}

	private String buildFullPrefix(String prefix) {
		return prefix + ConfigDefaultUtil.PREFIX_SEPARATOR;
	}
	
}
