package fr.badblock.bungee;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import fr.badblock.bungee.data.players.BadPlayer;
import fr.badblock.bungee.listeners.LoginListener;
import fr.badblock.bungee.rabbit.listeners.RabbitBungeeKeepAliveListener;
import fr.badblock.bungee.utils.BungeeUtils;
import fr.badblock.commons.technologies.mongodb.MongoConnector;
import fr.badblock.commons.technologies.mongodb.MongoService;
import fr.badblock.commons.technologies.rabbitmq.RabbitConnector;
import fr.badblock.commons.technologies.rabbitmq.RabbitPacketType;
import fr.badblock.commons.technologies.rabbitmq.RabbitService;
import fr.badblock.commons.technologies.redis.RedisConnector;
import fr.badblock.commons.technologies.redis.RedisService;
import fr.badblock.commons.utils.Encodage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

@Data@EqualsAndHashCode(callSuper=false) public class BadBungee extends Plugin {

	@Getter@Setter public static BadBungee instance;
	
	private String		  bungeeName;
	private Configuration config;
	private ConfigurationProvider cp = ConfigurationProvider.getProvider(YamlConfiguration.class);
	private RabbitService rabbitService;
	private MongoService  mongoService;
	private RedisService  redisService;
	private Gson		  gson;
	
	@Override
	public void onEnable() {
		setInstance(this);
		// Load configuration
		loadConfig();
		this.gson = new Gson();
		System.out.println("Waiting for response..");
		this.getRabbitService().sendPacket("bungee.worker.helloWorld", "", Encodage.UTF8, RabbitPacketType.PUBLISHER, 5000, false);
		while (!RabbitBungeeKeepAliveListener.done);
		
		// At the end, we load the listeners
		PluginManager pluginManager = this.getProxy().getPluginManager();
		pluginManager.registerListener(this, new LoginListener());
	}
	
	@Override
	public void onDisable() {
		
	}
	
	void keepAlive() {
		
	}
	
	void loadConfig() {
		try {
			getDataFolder().mkdirs();
			File f = new File(getDataFolder(), "config.yml");
			if(!f.exists())
				f.createNewFile();
			config = cp.load(f);
			// RabbitMQ
			String rabbitHostname = config.getString("rabbit.hostname");
			int rabbitPort = config.getInt("rabbit.port");
			String rabbitUsername = config.getString("rabbit.username");
			String rabbitPassword = config.getString("rabbit.password");
			String rabbitVirtualHost = config.getString("rabbit.virtualHost");
			rabbitService = RabbitConnector.getInstance().newService("default", rabbitHostname, rabbitPort, rabbitUsername, rabbitPassword, rabbitVirtualHost);
			// MongoDB
			List<String> mongoHostnames = config.getStringList("mongo.hostnames");
			int mongoPort = config.getInt("mongo.port");
			String mongoUsername = config.getString("mongo.username");
			String mongoPassword = config.getString("mongo.password");
			String mongoDatabase = config.getString("mongo.database");
			String[] mongoHostnamesArray = new String[mongoHostnames.size()];
			mongoHostnamesArray = mongoHostnames.toArray(mongoHostnamesArray);
			mongoService = MongoConnector.getInstance().newService("default", mongoPort, mongoUsername, mongoPassword, mongoDatabase, mongoHostnamesArray);
			bungeeName = config.getString("bungeeName");
			redisService = RedisConnector.getInstance().newService("default", redisHostname, redisPort, redisPassword);
		} catch(Exception e){
			e.printStackTrace();
			return;
		}
	}

	public List<BadPlayer> getOnlinePlayers() {
		List<BadPlayer> array = new ArrayList<>();
		if (BungeeUtils.getAvailableBungees() == null) return array;
		for (Bungee bungee : BungeeUtils.getAvailableBungees())
			array.addAll(bungee.getPlayers());
		return array;
	}
	
}
