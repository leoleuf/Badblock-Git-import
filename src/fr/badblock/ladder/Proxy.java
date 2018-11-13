package fr.badblock.ladder;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.chat.ActionBar;
import fr.badblock.ladder.api.chat.Motd;
import fr.badblock.ladder.api.chat.RawMessage;
import fr.badblock.ladder.api.chat.Title;
import fr.badblock.ladder.api.config.Configuration;
import fr.badblock.ladder.api.config.ConfigurationProvider;
import fr.badblock.ladder.api.config.YamlConfiguration;
import fr.badblock.ladder.api.entities.Bukkit;
import fr.badblock.ladder.api.entities.BungeeCord;
import fr.badblock.ladder.api.entities.OfflinePlayer;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.ladder.api.entities.PlayerIp;
import fr.badblock.ladder.api.utils.FileUtils;
import fr.badblock.ladder.api.utils.StringUtils;
import fr.badblock.ladder.chat.LadderActionBar;
import fr.badblock.ladder.chat.LadderRawMessage;
import fr.badblock.ladder.chat.LadderTitle;
import fr.badblock.ladder.commands.CommandAlert;
import fr.badblock.ladder.commands.CommandEnd;
import fr.badblock.ladder.commands.CommandFind;
import fr.badblock.ladder.commands.CommandGiveKey;
import fr.badblock.ladder.commands.CommandList;
import fr.badblock.ladder.commands.CommandPermissions;
import fr.badblock.ladder.commands.CommandPlayer;
import fr.badblock.ladder.commands.CommandPluginsManager;
import fr.badblock.ladder.commands.CommandReload;
import fr.badblock.ladder.commands.CommandSend;
import fr.badblock.ladder.commands.punish.CommandAdmin;
import fr.badblock.ladder.commands.punish.CommandBan;
import fr.badblock.ladder.commands.punish.CommandBanip;
import fr.badblock.ladder.commands.punish.CommandKick;
import fr.badblock.ladder.commands.punish.CommandMute;
import fr.badblock.ladder.commands.punish.CommandSanction;
import fr.badblock.ladder.commands.punish.CommandTempban;
import fr.badblock.ladder.commands.punish.CommandTempbanip;
import fr.badblock.ladder.commands.punish.CommandUnban;
import fr.badblock.ladder.commands.punish.CommandUnbanip;
import fr.badblock.ladder.commands.punish.CommandUnmute;
import fr.badblock.ladder.connection.LadderSocketHost;
import fr.badblock.ladder.data.DataSavers;
import fr.badblock.ladder.data.LadderDataHandler;
import fr.badblock.ladder.data.LadderIpDataHandler;
import fr.badblock.ladder.entities.ConsoleCommandSender;
import fr.badblock.ladder.entities.LadderBukkit;
import fr.badblock.ladder.entities.LadderBungee;
import fr.badblock.ladder.entities.LadderOfflinePlayer;
import fr.badblock.ladder.entities.LadderPermissionManager;
import fr.badblock.ladder.http.LadderHttpHandler;
import fr.badblock.ladder.log.LadderLogger;
import fr.badblock.ladder.log.LoggerOutputStream;
import fr.badblock.ladder.sql.BadblockDatabase;
import fr.badblock.protocol.packets.Packet;
import fr.badblock.protocol.packets.PacketHelloworld;
import fr.badblock.protocol.packets.PacketLadderStop;
import fr.badblock.protocol.packets.PacketPlayerChat;
import fr.badblock.protocol.packets.PacketPlayerChat.ChatAction;
import fr.badblock.protocol.packets.PacketPlayerData;
import fr.badblock.protocol.packets.PacketPlayerData.DataAction;
import fr.badblock.protocol.packets.PacketPlayerData.DataType;
import fr.badblock.protocol.socket.SocketHost;
import fr.badblock.rabbitconnector.RabbitListener;
import fr.badblock.rabbitconnector.RabbitListenerType;
import fr.badblock.rabbitconnector.RabbitPacketType;
import fr.badblock.rabbitconnector.RabbitService;
import fr.badblock.utils.Encodage;
import fr.toenga.common.tech.mongodb.MongoConnector;
import fr.toenga.common.tech.mongodb.MongoService;
import fr.toenga.common.tech.mongodb.setting.MongoSettings;
import jline.console.ConsoleReader;
import lombok.Getter;
import lombok.Setter;

public class Proxy extends Ladder {

	@Getter protected static Proxy instance;

	public static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();
	public static final String LADDER_VERSION = "Ladder BêtaV0.1";

	public final static File DATA_FOLDER		  = new File("data");
	public final static File PLAYER_FOLDER        = new File(DATA_FOLDER, "players");
	public final static File IPS_FOLDER       	  = new File(DATA_FOLDER, "ips");
	public final static File PLUGINS_FOLDER       = new File("plugins");
	public final static File SERVERS_FOLDER       = new File("servers");
	public final static File LOG_FOLDER	          = new File("logs");

	public final static File PERMISSIONS_FILE     = new File("permissions.json");
	public final static File MOTD_FILE		      = new File("motd.json");
	public final static File MONGO_FILE		      = new File("mongo.json");
	public final static File SERVERS_BUNGEE_FILE  = new File("bungeecords.yml");
	public final static File CONFIG_FILE	      = new File("ladder.yml");

	@Getter
	private Map<String, Bukkit>		reconnectionInvitations = Maps.newConcurrentMap();

	@Getter
	private LadderPermissionManager permissions;
	@Getter private Motd	  		motd;

	@Getter private String	  		ip;
	@Getter private int		  		port;

	@Getter private String	  		alertPrefix;

	@Getter
	private final SocketHost  		host;

	private final Map<InetAddress, LadderIpDataHandler> ipData;

	@Getter@Setter
	private transient RabbitService  	rabbitServiced;

	@Getter@Setter
	private MongoSettings			    mongo;
	
	@Getter@Setter
	private MongoService				mongoService;


	public Proxy(ConsoleReader reader) throws IOException {
		super(LADDER_VERSION, 
				new LadderLogger(reader, LOG_FOLDER), 
				new ConsoleCommandSender(),
				ConfigurationProvider.getProvider(YamlConfiguration.class));

		instance = this;

		ipData   = new HashMap<>();

		DATA_FOLDER.mkdir();
		PLAYER_FOLDER.mkdir();
		IPS_FOLDER.mkdir();

		host = new LadderSocketHost(InetAddress.getByName(ip), port);
		logger.log(Level.INFO, "Listening on " + host.getServer().getInetAddress() + ":" + host.getServer().getLocalPort() + "!");

		broadcastPacket(new PacketHelloworld());
		broadcastPacket(new PacketPlayerData(DataType.PLAYERS, DataAction.REQUEST, "*", "*"));
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				if (Proxy.getInstance().getRabbitServiced() != null) {// en cas de désynchro
					Proxy.getInstance().getRabbitServiced().sendAsyncPacket("ladder.playersupdate", Integer.toString(getLadderOnlineCount()), Encodage.UTF8, RabbitPacketType.PUBLISHER, 10000, false);
				}
			}
		}, 100, 100);
		new Thread() {
			@Override
			public void run() {
				while (Proxy.getInstance().getRabbitServiced() == null) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}					
				new RabbitListener(Proxy.getInstance().getRabbitServiced(), "ladder.playersupdater", false, RabbitListenerType.SUBSCRIBER) {

					@Override
					public void onPacketReceiving(String body) {
						setBungeeOnlineCount(Integer.parseInt(body));
					}

				};
			}
		}.start();
	}

	public void removeReconnectionInvitation(OfflinePlayer player, boolean punish){
		String name = player.getName().toLowerCase();

		if(reconnectionInvitations.containsKey(name)){
			reconnectionInvitations.remove(name);
			this.getRabbitServiced().sendAyncPacket("removeReconnectionInvitations", player.getName().toLowerCase(), Encodage.UTF8, RabbitPacketType.PUBLISHER);

			if(punish){
				//TODO LeaverBuster :o
			}
		}
	}

	public Bukkit getReconnectionInvitation(OfflinePlayer player){
		return reconnectionInvitations.get( player.getName().toLowerCase() );
	}

	public void invite(OfflinePlayer player, Bukkit bukkit){
		reconnectionInvitations.put(player.getName().toLowerCase(), bukkit);
		this.getRabbitServiced().sendAyncPacket("reconnectionInvitations", player.getName().toLowerCase() + ";" + bukkit.getName(), Encodage.UTF8, RabbitPacketType.PUBLISHER);
	}

	@Override
	public int getLadderOnlineCount() {
		return this.getOnlinePlayers().size();
	}

	@Override
	public OfflinePlayer getOfflinePlayer(String name) {
		OfflinePlayer result = getPlayer(name);

		if(result == null)
			result = new LadderOfflinePlayer(name, null);

		return result;
	}

	@Override
	public void stopLadder(){
		logger.log(Level.INFO, "Closing Ladder ....");
		broadcastPacket(new PacketHelloworld());
		broadcastPacket(new PacketLadderStop());
		host.end();
		logger.log(Level.INFO, "Not listening anymore ... saving players");

		DataSavers.allow = false;

		while(!DataSavers.toSave.isEmpty()){
			try {
				Thread.sleep(50L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		for(Player player : getOnlinePlayers()){
			((LadderDataHandler) player).saveSync(player.getData(), false);
			((LadderDataHandler) player.getIpData()).saveSync(player.getData(), false);
		}

		logger.log(Level.INFO, "Players saved! Good bye!");
		System.exit(0);
	}

	@Override
	protected void readConfiguration() throws IOException {
		SERVERS_FOLDER.mkdir();

		System.setErr(new PrintStream(new LoggerOutputStream(logger, Level.SEVERE), true));
		System.setOut(new PrintStream(new LoggerOutputStream(logger, Level.INFO), true));

		logger.log(Level.INFO, "Reading configuration ...");

		loadPermissions(false);
		loadMotd(false);
		loadMongo();

		Configuration configuration = getConfigurationProvider().load(CONFIG_FILE);
		if(!configuration.contains("ip"))
			configuration.set("ip", "127.0.0.1");
		if(!configuration.contains("port"))
			configuration.set("port", 26850);
		if(!configuration.contains("alert"))
			configuration.set("alert", "&4[&cAlert&4] &r");
		if(!configuration.contains("maxplayers"))
			configuration.set("maxplayers", -1);
		if(!configuration.contains("portHttp"))
			configuration.set("portHttp", 8080);
		if(!configuration.contains("dbHostname"))
			configuration.set("dbHostname", "127.0.0.1");
		if(!configuration.contains("dbPort"))
			configuration.set("dbPort", 3306);
		if(!configuration.contains("dbUsername"))
			configuration.set("dbUsername", "root");
		if(!configuration.contains("dbPassword"))
			configuration.set("dbPassword", "azerty123");
		if(!configuration.contains("dbDatabase"))
			configuration.set("dbDatabase", "root");
		BadblockDatabase.getInstance().connect(configuration.getString("dbHostname"), configuration.getInt("dbPort"), configuration.getString("dbUsername"), configuration.getString("dbPassword"), configuration.getString("dbDatabase"));

		ip   		= configuration.getString("ip");
		port 		= configuration.getInt("port");
		alertPrefix = configuration.getString("alert");
		maxPlayers  = configuration.getInt("maxplayers");
		new LadderHttpHandler(configuration.getInt("portHttp"));

		getConfigurationProvider().save(configuration, CONFIG_FILE);

		Configuration bungeeCords = getConfigurationProvider().load(SERVERS_BUNGEE_FILE);
		if(!bungeeCords.contains("servers")){
			bungeeCords.set("servers.proxy1.host", "127.0.0.1");
			bungeeCords.set("servers.proxy1.port", 27050);
		}

		for(String key : bungeeCords.getSection("servers").getKeys()){
			InetSocketAddress address = new InetSocketAddress(bungeeCords.getString("servers." + key + ".host"),
					bungeeCords.getInt("servers." + key + ".port"));
			super.bungeeCords.put(key.toLowerCase(), new LadderBungee(key, address));
			super.bungeesAddress.put(address, key.toLowerCase());
		}

		getConfigurationProvider().save(bungeeCords, SERVERS_BUNGEE_FILE);
		loadServers();
	}

	private void loadServers(){
		ConfigurationProvider cp = ConfigurationProvider.getProvider(YamlConfiguration.class);

		for(File f : getYamlFiles(SERVERS_FOLDER)){
			try {
				Configuration config = cp.load(f);
				List<Bukkit> servers = getServerInfos(cp.load(f));

				for(Bukkit server : servers){
					if(!bukkits.containsKey(server.getName().toLowerCase()) && !bukkitsAddress.containsKey(server.getAddress())){
						bukkits.put(server.getName().toLowerCase(), server);
						bukkitsAddress.put(server.getAddress(), server.getName().toLowerCase());
					}
				}
				cp.save(config, f);
			} catch (IOException e) {
				System.out.println("Impossible de charger la configuration de serveurs " + f.getAbsolutePath() + " :");
				e.printStackTrace();
			}
		}
	}

	private List<Bukkit> getServerInfos(Configuration config){
		List<Bukkit> ret = new ArrayList<Bukkit>();

		try {
			for(String key : config.getSection("servers").getKeys()){
				Bukkit info = new LadderBukkit(StringUtils.getAddress(config.getString("servers." + key)), key);
				ret.add(info);
			}
		} catch(Exception e){
			config.set("servers.lobby", "localhost:25565");
		}

		return ret;
	}

	private List<File> getYamlFiles(File directory){
		List<File> result = new ArrayList<File>();

		if(!directory.isDirectory()) return result;

		for(File f : directory.listFiles()){
			if(!f.isDirectory() && f.getName().endsWith(".yml")){
				result.add(f);
			} else if(f.isDirectory()){
				result.addAll(getYamlFiles(f));
			}
		}

		return result;
	}

	@Override
	public Gson getGson(){
		return gson;
	}

	public void loadPermissions(boolean send){
		System.out.println("Loaded permissions.json");
		this.permissions     = new LadderPermissionManager(new File("permissions.json"));
		this.permissions.save();
		if(!send) return;

		for(BungeeCord bungee : getServers()){
			bungee.sendPermissions();
		}

		for(Bukkit bukkit : getBukkitServers()){
			bukkit.sendPermissions();
		}
	}

	public void loadMongo()
	{	
		MongoSettings mongoSettings = new MongoSettings();
		if (MONGO_FILE.exists())
		{
			mongo = FileUtils.load(MONGO_FILE, MongoSettings.class);
			if(mongo == null)
			{
				mongo = mongoSettings;
			}
		}
		else
		{
			mongo = mongoSettings;
		}

		FileUtils.save(MONGO_FILE, mongo, true);

		mongoService = MongoConnector.getInstance().registerService(new MongoService("default", mongo));
	}

	public void loadMotd(boolean send){
		Motd def = new Motd(new String[]{"&cA Ladder server", "With a cool MOTD"}, getVersion(), new String[]{""}, 1000);
		if(MOTD_FILE.exists()){
			motd = FileUtils.load(MOTD_FILE, Motd.class);
			if(motd == null)
				motd = def;
		} else {
			motd = def;
		}

		FileUtils.save(MOTD_FILE, motd, true);

		if(!send) return;
		for(BungeeCord bungee : getServers()){
			bungee.sendMotd(motd);
		}
	}

	@Override
	protected void enablePlugins() {
		PLUGINS_FOLDER.mkdir();
		logger.log(Level.INFO, "Loading plugins ...");

		pluginsManager.detectPlugins(PLUGINS_FOLDER);

		pluginsManager.registerCommand(null, new CommandAlert());
		pluginsManager.registerCommand(null, new CommandAdmin());
		pluginsManager.registerCommand(null, new CommandEnd());
		pluginsManager.registerCommand(null, new CommandFind());
		pluginsManager.registerCommand(null, new CommandSend());
		pluginsManager.registerCommand(null, new CommandList());
		pluginsManager.registerCommand(null, new CommandReload());
		pluginsManager.registerCommand(null, new CommandPermissions());
		pluginsManager.registerCommand(null, new CommandBan());
		pluginsManager.registerCommand(null, new CommandBanip());
		pluginsManager.registerCommand(null, new CommandTempban());
		pluginsManager.registerCommand(null, new CommandTempbanip());
		pluginsManager.registerCommand(null, new CommandKick());
		pluginsManager.registerCommand(null, new CommandMute());
		pluginsManager.registerCommand(null, new CommandUnban());
		pluginsManager.registerCommand(null, new CommandUnbanip());
		pluginsManager.registerCommand(null, new CommandUnmute());
		pluginsManager.registerCommand(null, new CommandSanction());
		pluginsManager.registerCommand(null, new CommandPlayer());
		pluginsManager.registerCommand(null, new CommandGiveKey());
		pluginsManager.registerCommand(null, new CommandPluginsManager());

		pluginsManager.loadPlugins();
		pluginsManager.enablePlugins();

		logger.log(Level.INFO, "Plugins loaded!");
	}

	@Override
	public boolean addBukkitServer(InetSocketAddress address, String name){
		if(bukkits.containsKey(name.toLowerCase()) || bukkitsAddress.containsKey(address))
			return false;

		Bukkit bukkit = new LadderBukkit(address, name);
		name = name.toLowerCase();

		bukkits.put(name, bukkit);
		bukkitsAddress.put(address, name);

		for(BungeeCord bungee : getServers()){
			bungee.addBukkitServer(bukkit);
		}

		return true;
	}

	@Override
	public void broadcastPacket(Packet packet) {
		for(BungeeCord bungee : getServers()){
			bungee.sendPacket(packet);
		}
	}

	@Override
	public void broadcast(String... messages) {
		broadcastPacket(new PacketPlayerChat(null, ChatAction.MESSAGE_FLAT, messages));
		for(String message : messages)
			getLogger().log(Level.INFO, message);
	}

	@Override
	public Title createTitle(String title, String subtitle) {
		return new LadderTitle(title, subtitle);
	}

	@Override
	public ActionBar createActionBar(String base) {
		return new LadderActionBar(base);
	}

	@Override
	public RawMessage createRawMessage(String base) {
		return new LadderRawMessage(base);
	}

	public void playerConnect(Player player) {
		if(!players.containsKey(player.getUniqueId())) {
			players.put(player.getUniqueId(), player);
			if (playersCache.size() < this.getBungeeOnlineCount()) {
				playersCache.add(player.getName());
			}
			names.put(player.getName().toLowerCase(), player.getUniqueId());
		}
	}

	public void playerDisconnect(Player player) {
		if(players.containsKey(player.getUniqueId())){
			players.remove(player.getUniqueId());
			if (playersCache.size() > this.getBungeeOnlineCount()) {
				playersCache.remove(player.getName());
			}
			names.remove(player.getName().toLowerCase());
		}
	}

	@Override
	public PlayerIp getIpData(InetAddress address) {
		if(address == null) return null;

		if(ipData.containsKey(address))
			return ipData.get(address);

		LadderIpDataHandler player = new LadderIpDataHandler(address);
		ipData.put(address, player);

		return player;
	}
}