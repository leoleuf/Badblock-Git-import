package fr.badblock.bungee;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import fr.badblock.bungee.commands.linked.SendToAllCommand;
import fr.badblock.bungee.commands.unlinked.MotdCommand;
import fr.badblock.bungee.data.players.BadPlayer;
import fr.badblock.bungee.listeners.DisconnectListener;
import fr.badblock.bungee.listeners.LoginListener;
import fr.badblock.bungee.listeners.ProxyPingListener;
import fr.badblock.bungee.rabbit.listeners.RabbitBungeeExecuteCommandListener;
import fr.badblock.bungee.rabbit.listeners.RabbitBungeeHelloWorldListener;
import fr.badblock.bungee.rabbit.listeners.RabbitBungeeKeepAliveListener;
import fr.badblock.bungee.utils.BungeeUtils;
import fr.badblock.bungee.utils.Motd;
import fr.badblock.commons.technologies.mongodb.MongoConnector;
import fr.badblock.commons.technologies.mongodb.MongoService;
import fr.badblock.commons.technologies.rabbitmq.RabbitConnector;
import fr.badblock.commons.technologies.rabbitmq.RabbitPacketType;
import fr.badblock.commons.technologies.rabbitmq.RabbitService;
import fr.badblock.commons.technologies.redis.RedisConnector;
import fr.badblock.commons.technologies.redis.RedisService;
import fr.badblock.commons.utils.Callback;
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
	public static final Type bungeeType = new TypeToken<Bungee>() {}.getType();
	
	private String		  bungeeName;
	private Configuration config;
	private ConfigurationProvider cp = ConfigurationProvider.getProvider(YamlConfiguration.class);
	private RabbitService rabbitService;
	private MongoService  mongoService;
	private RedisService  redisService;
	private Gson		  gson;
	private Motd		  motd;
	private Motd		  fullMotd;
	private Timer		  timer;
	private int			  onlineCount;
	
	@Override
	public void onEnable() {
		setInstance(this);
		// Load configuration
		loadConfig();
		this.gson = new Gson();
		System.out.println("[BadBungee] Waiting for response..");
		new RabbitBungeeHelloWorldListener();
		new RabbitBungeeKeepAliveListener();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.getRabbitService().sendSyncPacket("bungee.worker.helloWorld", "a", Encodage.UTF8, RabbitPacketType.PUBLISHER, 5000, false);
		while (!RabbitBungeeKeepAliveListener.done) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("[BadBungee] Waiting for proxies...");
		try {
			Thread.sleep(10_000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("[BadBungee] Loading...");
		new RabbitBungeeExecuteCommandListener();
		this.setTimer(new Timer());
		this.getTimer().schedule(new TimerTask() {
			@Override
			public void run() {
				keepAlive();
			}
		}, 5000L, 5000L);
		this.getTimer().schedule(new TimerTask() {
			@Override
			public void run() {
				getRabbitService().sendPacket("bungee.worker.playersupdate", Integer.toString(getOnlinePlayers().size()), Encodage.UTF8, RabbitPacketType.PUBLISHER, 5000, false);
			}
		}, 500L, 500L);
		// At the end, we load the listeners
		PluginManager pluginManager = this.getProxy().getPluginManager();
		pluginManager.registerListener(this, new LoginListener());
		pluginManager.registerListener(this, new ProxyPingListener());
		pluginManager.registerListener(this, new DisconnectListener());
		// And the commands :D
		pluginManager.registerCommand(this, new MotdCommand());
		pluginManager.registerCommand(this, new SendToAllCommand());
		System.out.println("[BadBungee] Loaded!");
	}
	
	@Override
	public void onDisable() {
		
	}
	
	public void keepAlive() {
		this.getRabbitService().sendPacket("bungee.worker.keepAlive", gson.toJson(new Bungee(this.getBungeeName(), BadPlayer.players.values())), Encodage.UTF8, RabbitPacketType.PUBLISHER, 10000, false);
	}
	
	public void reloadMotd() {
		System.out.println("A");
		redisService.getAsyncObject("motd.default", Motd.class, new Callback<Motd>() {

			@Override
			public void done(Motd result, Throwable error) {
				System.out.println("B " + redisService.getCredentials().getDatabase());
				if (result == null) return;
				System.out.println("C");
				motd = result;
			}
			
		});
		redisService.getAsyncObject("motd.full", Motd.class, new Callback<Motd>() {

			@Override
			public void done(Motd result, Throwable error) {
				if (result == null) return;
				fullMotd = result;
			}
			
		});
	}
	
	void loadConfig() {
		try {
			getDataFolder().mkdirs();
			File f = new File(getDataFolder(), "config.yml");
			if(!f.exists())
				f.createNewFile();
			config = cp.load(f);
			// Bungee
			bungeeName = config.getString("bungeeName");
			// RabbitMQ
			loadRabbitMQ();
			// MongoDB
			loadMongoDB();
			// Load Redis
			loadRedis();
		} catch(Exception e){
			e.printStackTrace();
			return;
		}
	}

	void loadRabbitMQ() {
		String rabbitHostname = config.getString("rabbit.hostname");
		int rabbitPort = config.getInt("rabbit.port");
		String rabbitUsername = config.getString("rabbit.username");
		String rabbitPassword = config.getString("rabbit.password");
		String rabbitVirtualHost = config.getString("rabbit.virtualHost");
		rabbitService = RabbitConnector.getInstance().newService("default", rabbitHostname, rabbitPort, rabbitUsername, rabbitPassword, rabbitVirtualHost);
	}
	
	void loadMongoDB() {
		List<String> mongoHostnames = config.getStringList("mongo.hostnames");
		int mongoPort = config.getInt("mongo.port");
		String mongoUsername = config.getString("mongo.username");
		String mongoPassword = config.getString("mongo.password");
		String mongoDatabase = config.getString("mongo.database");
		String[] mongoHostnamesArray = new String[mongoHostnames.size()];
		mongoHostnamesArray = mongoHostnames.toArray(mongoHostnamesArray);
		mongoService = MongoConnector.getInstance().newService("default", mongoPort, mongoUsername, mongoPassword, mongoDatabase, mongoHostnamesArray);
	}
	
	void loadRedis() {
		String redisHostname = config.getString("redis.hostname");
		int redisPort = config.getInt("redis.port");
		int redisDatabase = config.getInt("redis.database");
		String redisPassword = config.getString("redis.password");
		redisService = RedisConnector.getInstance().newService("default", redisHostname, redisPort, redisPassword, redisDatabase);
		reloadMotd();
	}
	
	public List<BadPlayer> getOnlinePlayers() {
		List<BadPlayer> array = new ArrayList<>();
		if (BungeeUtils.getAvailableBungees() == null) return array;
		for (Bungee bungee : BungeeUtils.getAvailableBungees())
			array.addAll(bungee.getPlayers());
		return array;
	}
	
}
