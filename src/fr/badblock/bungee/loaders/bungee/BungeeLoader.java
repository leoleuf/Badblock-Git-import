package fr.badblock.bungee.loaders.bungee;

import java.util.Timer;

import com.google.gson.Gson;

import fr.badblock.bungee.bobjects.Motd;
import fr.badblock.bungee.loaders.Loader;
import fr.badblock.bungee.loaders.technologies.ConfigLoader;
import fr.badblock.bungee.loaders.technologies.MongoDBLoader;
import fr.badblock.bungee.loaders.technologies.RabbitMQLoader;
import fr.badblock.bungee.loaders.technologies.RedisLoader;
import fr.badblock.bungee.sync.rabbitmq.listeners.RabbitBungeeHelloWorldListener;
import fr.badblock.bungee.sync.rabbitmq.listeners.RabbitBungeeKeepAliveListener;
import fr.badblock.bungee.sync.rabbitmq.listeners.RabbitBungeePlayersUpdaterListener;
import fr.badblock.bungee.sync.rabbitmq.listeners.RabbitBungeeStopListener;
import fr.badblock.bungee.utils.ConfigDefaultUtil;
import fr.badblock.commons.technologies.mongodb.MongoService;
import fr.badblock.commons.technologies.rabbitmq.RabbitPacketType;
import fr.badblock.commons.technologies.rabbitmq.RabbitService;
import fr.badblock.commons.technologies.redis.RedisService;
import fr.badblock.commons.utils.Encodage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.config.Configuration;

/**
 * Main object of a synchronized Bungee, it doesn't manage the synchronization but it feels like a synchronized object
 * @author xMalware
 */
@Data@EqualsAndHashCode(callSuper=false) public class BungeeLoader implements Loader {
	
	@Getter@Setter public static BungeeLoader SyncInstance;

	// Main objects
	protected Thread		  mainThread;
	protected String		  bungeeName;
	// Configuration
	protected Configuration   config;
	// Communication services
	protected RabbitService   rabbitService;
	protected MongoService    mongoService;
	protected RedisService    redisDataService;
	protected RedisService    redisPlayerDataService;
	// Gson and formatting
	protected Gson		  	  gson;
	protected Gson		  	  exposeGson;
	protected Motd		  	  motd;
	protected Motd		  	  fullMotd;
	// Threading
	protected Timer		  	  timer;

	public BungeeLoader() {
		// Set main objects
		setSyncInstance(this);
		this.mainThread = Thread.currentThread();
		// Loading configuration
		new ConfigLoader(this.getDataFolder(), ConfigDefaultUtil.CONFIG_FILE_NAME) {
			@Override
			public void done(Configuration configuration) {
				setMongoService(new MongoDBLoader(configuration).done());
				setRedisService(new RedisLoader(configuration).done());
				setRabbitService(new RabbitMQLoader(configuration).done());
			}
		};
		// Loading rabbit listeners
		loadRabbitListeners();
		log("Waiting for response..");
		waitOnServerThread(1000);
		this.getRabbitService().sendSyncPacket("bungee.worker.helloWorld", "", Encodage.UTF8, RabbitPacketType.PUBLISHER, 5000, false);
		while (!RabbitBungeeKeepAliveListener.done) waitOnServerThread(100);
	}
	
	public void loadRabbitListeners() {
		new RabbitBungeeHelloWorldListener();
		new RabbitBungeeKeepAliveListener();
		new RabbitBungeePlayersUpdaterListener();
		new RabbitBungeeStopListener();
	}

	@SuppressWarnings("deprecation")
	public void log(String message) {
		BungeeCord.getInstance().getConsole().sendMessage("[BadBungee] " +message);
	}

	public void waitOnServerThread(long time) {
		synchronized (this.mainThread) {
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
