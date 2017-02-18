package fr.badblock.bungee;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import com.cloudflare.api.CloudflareAccess;
import com.cloudflare.api.constants.RecordType;
import com.cloudflare.api.requests.dns.DNSAddRecord;
import com.cloudflare.api.utils.TimeUnit;
import com.cloudflare.api.utils.TimeUnit.UnitType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fr.badblock.bungee.bobjects.Bungee;
import fr.badblock.bungee.bobjects.Motd;
import fr.badblock.bungee.commands.linked.SendToAllCommand;
import fr.badblock.bungee.commands.unlinked.MotdCommand;
import fr.badblock.bungee.data.ip.BadIpData;
import fr.badblock.bungee.data.players.BadPlayer;
import fr.badblock.bungee.listeners.DisconnectListener;
import fr.badblock.bungee.listeners.LoginListener;
import fr.badblock.bungee.listeners.ProxyPingListener;
import fr.badblock.bungee.listeners.ServerConnectListener;
import fr.badblock.bungee.rabbit.listeners.RabbitBungeeExecuteCommandListener;
import fr.badblock.bungee.sync.SyncBungee;
import fr.badblock.bungee.utils.BungeeUtils;
import fr.badblock.commons.technologies.mongodb.MongoConnector;
import fr.badblock.commons.technologies.rabbitmq.RabbitConnector;
import fr.badblock.commons.technologies.rabbitmq.RabbitPacketType;
import fr.badblock.commons.technologies.redis.RedisConnector;
import fr.badblock.commons.utils.Callback;
import fr.badblock.commons.utils.Encodage;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.PluginManager;

 public class BadBungee extends SyncBungee {


	@Override
	public void onEnable() {

		// Load configuration
		loadConfig();
		this.gson = new Gson();
		this.exposeGson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		System.out.println("[BadBungee] Waiting for proxies...");
		try {
			Thread.sleep(1_000L);
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
				//System.out.println(getOnlinePlayers().size());
				getRabbitService().sendPacket("bungee.worker.playersupdate", Integer.toString(getOnlinePlayers().size()), Encodage.UTF8, RabbitPacketType.PUBLISHER, 5000, false);
			}
		}, 500L, 500L);
		// At the end, we load the listeners
		PluginManager pluginManager = this.getProxy().getPluginManager();
		pluginManager.registerListener(this, new DisconnectListener());
		pluginManager.registerListener(this, new LoginListener());
		pluginManager.registerListener(this, new ProxyPingListener());
		pluginManager.registerListener(this, new ServerConnectListener());
		// And the commands :D
		pluginManager.registerCommand(this, new MotdCommand());
		pluginManager.registerCommand(this, new SendToAllCommand());
		// Add DNS
		CloudflareAccess access = new CloudflareAccess(config.getString("cloudflare.email"), config.getString("cloudflare.key"));
		System.out.println(ProxyServer.getInstance().getConfig().getListeners().iterator().next().getHost().getHostString());
		
		DNSAddRecord dns = new DNSAddRecord(access, "badblock.fr", RecordType.Text, "_minecraft._tcp.badblock.fr", "SRV 0 100 25565 " + ProxyServer.getInstance().getConfig().getListeners().iterator().next().getHost().getHostString() + ".", new TimeUnit(UnitType.MINUTES, 2));
		try {
			System.out.print(dns.executeBasic().getJSONObject("rec").getJSONObject("obj").getLong("rec_id"));
			dns.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("[BadBungee] Loaded!");
	}

	@Override
	public void onDisable() {
		getRabbitService().sendSyncPacket("bungee.worker.stop", this.getBungeeName(), Encodage.UTF8, RabbitPacketType.PUBLISHER, 5000, false);
	}

	public void keepAlive() {
		String data = this.getExposeGson().toJson(new Bungee(this.getBungeeName(), BadPlayer.players.values(), BadIpData.ips.values()));
		this.getRabbitService().sendPacket("bungee.worker.keepAlive", data, Encodage.UTF8, RabbitPacketType.PUBLISHER, 10000, false);
	}

	public void reloadMotd() {
		redisDataService.getSyncObject("motd.default", Motd.class, new Callback<Motd>() {

			@Override
			public void done(Motd result, Throwable error) {
				if (result == null) {
					System.out.println("[BadBungee] No MOTD set.");
					return;
				}
				motd = result;
			}

		}, false);
		redisDataService.getSyncObject("motd.full", Motd.class, new Callback<Motd>() {

			@Override
			public void done(Motd result, Throwable error) {
				if (result == null) {
					System.out.println("[BadBungee] No Full-MOTD set.");
					return;
				}
				fullMotd = result;
			}

		}, false);
	}

	void loadConfig() {
		try {
			getDataFolder().mkdirs();
			File f = new File(getDataFolder(), "config.yml");
			if(!f.exists())
				f.createNewFile();
			config = cp.load(f);
			// Bungee
			String bungeeName = config.getString("bungeeName");
			this.bungeeName = bungeeName;
			System.out.println(this.bungeeName);
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
		int redisPlayerDatabase = config.getInt("redis.playerDatabase");
		int redisDatabase = config.getInt("redis.database");
		String redisPassword = config.getString("redis.password");
		redisDataService = RedisConnector.getInstance().newService("default", redisHostname, redisPort, redisPassword, redisDatabase);
		redisPlayerDataService = RedisConnector.getInstance().newService("default", redisHostname, redisPort, redisPassword, redisPlayerDatabase);
		reloadMotd();
	}

	public List<BadPlayer> getOnlinePlayers() {
		List<BadPlayer> array = new ArrayList<>();
		if (BungeeUtils.getAvailableBungees() == null) return array;
		for (Bungee bungee : BungeeUtils.getAvailableBungees())
			array.addAll(bungee.getPlayers());
		return array;
	}

	public List<BadIpData> getOnlineIps() {
		List<BadIpData> array = new ArrayList<>();
		if (BungeeUtils.getAvailableBungees() == null) return array;
		for (Bungee bungee : BungeeUtils.getAvailableBungees())
			array.addAll(bungee.getIps());
		return array;
	}

	public BadPlayer get(String playerName) {
		Optional<BadPlayer> optionalBadPlayer = getOnlinePlayers().parallelStream().filter(player -> player.getName().toLowerCase().equals(playerName.toLowerCase())).findAny();
		if (!optionalBadPlayer.isPresent()) return null;
		return optionalBadPlayer.get();
	}

	public BadIpData getIp(String ip) {
		Optional<BadIpData> optionalBadIpData = getOnlineIps().parallelStream().filter(ipData -> ipData.getIp().toLowerCase().equals(ip.toLowerCase())).findAny();
		if (!optionalBadIpData.isPresent()) return null;
		return optionalBadIpData.get();
	}

	public BadPlayer get(ProxiedPlayer proxiedPlayer) {
		return get(proxiedPlayer.getName());
	}

}
