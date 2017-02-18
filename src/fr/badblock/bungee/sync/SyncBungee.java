package fr.badblock.bungee.sync;

import java.util.Timer;

import com.google.gson.Gson;

import fr.badblock.bungee.bobjects.Motd;
import fr.badblock.bungee.loaders.ConfigLoader;
import fr.badblock.bungee.loaders.technologies.MongoDBLoader;
import fr.badblock.bungee.loaders.technologies.RabbitMQLoader;
import fr.badblock.bungee.loaders.technologies.RedisLoader;
import fr.badblock.bungee.rabbit.listeners.RabbitBungeeHelloWorldListener;
import fr.badblock.bungee.rabbit.listeners.RabbitBungeeKeepAliveListener;
import fr.badblock.bungee.rabbit.listeners.RabbitBungeePlayersUpdaterListener;
import fr.badblock.bungee.rabbit.listeners.RabbitBungeeStopListener;
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
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;

@Data@EqualsAndHashCode(callSuper=false) public class SyncBungee extends Plugin {
	
	@Getter@Setter public static SyncBungee SyncInstance;

	private Thread		  mainThread;
	private String		  bungeeName;
	private Configuration config;
	private RabbitService rabbitService;
	private MongoService  mongoService;
	private RedisService  redisDataService;
	private RedisService  redisPlayerDataService;
	private Gson		  gson;
	private Gson		  exposeGson;
	private Motd		  motd;
	private Motd		  fullMotd;
	private Timer		  timer;
	private int			  onlineCount;

	public SyncBungee() {
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
