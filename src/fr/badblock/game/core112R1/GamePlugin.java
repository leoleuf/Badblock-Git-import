package fr.badblock.game.core112R1;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapView;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import fr.badblock.api.common.minecraft.party.PartySyncManager;
import fr.badblock.api.common.tech.mongodb.MongoConnector;
import fr.badblock.api.common.tech.mongodb.setting.MongoSettings;
import fr.badblock.api.common.tech.rabbitmq.RabbitService;
import fr.badblock.api.common.tech.rabbitmq.setting.RabbitSettings;
import fr.badblock.api.common.utils.permissions.Permissible;
import fr.badblock.api.common.utils.permissions.PermissionsManager;
import fr.badblock.game.core112R1.configuration.GameConfiguration;
import fr.badblock.game.core112R1.gameserver.GameServer;
import fr.badblock.game.core112R1.gameserver.GameServerManager;
import fr.badblock.game.core112R1.gameserver.threading.ServerForceKillListener;
import fr.badblock.game.core112R1.i18n.GameI18n;
import fr.badblock.game.core112R1.itemstack.GameCustomInventory;
import fr.badblock.game.core112R1.itemstack.GameItemExtra;
import fr.badblock.game.core112R1.itemstack.GameItemStackFactory;
import fr.badblock.game.core112R1.itemstack.ItemStackExtras;
import fr.badblock.game.core112R1.itemstack.QrCodeMap;
import fr.badblock.game.core112R1.jsonconfiguration.data.FTPConfig;
import fr.badblock.game.core112R1.jsonconfiguration.data.GameServerConfig;
import fr.badblock.game.core112R1.jsonconfiguration.data.LadderConfig;
import fr.badblock.game.core112R1.jsonconfiguration.data.RankedConfig;
import fr.badblock.game.core112R1.jsonconfiguration.data.SQLConfig;
import fr.badblock.game.core112R1.jsonconfiguration.data.ServerConfig;
import fr.badblock.game.core112R1.listeners.ChatListener;
import fr.badblock.game.core112R1.listeners.CrashBookFix;
import fr.badblock.game.core112R1.listeners.PortalListener;
import fr.badblock.game.core112R1.listeners.mapprotector.DefaultMapProtector;
import fr.badblock.game.core112R1.players.GameBadblockPlayer;
import fr.badblock.game.core112R1.players.GameCustomObjective;
import fr.badblock.game.core112R1.players.GameTeam;
import fr.badblock.game.core112R1.players.data.GameKit;
import fr.badblock.game.core112R1.players.listeners.GameJoinItems;
import fr.badblock.game.core112R1.players.listeners.GameScoreboard;
import fr.badblock.game.core112R1.players.utils.Meteor;
import fr.badblock.game.core112R1.sql.FakeSQLDatabase;
import fr.badblock.game.core112R1.sql.GameSQLDatabase;
import fr.badblock.game.core112R1.tasks.AntiAFKTask;
import fr.badblock.game.core112R1.tasks.GameStatisticsTask;
import fr.badblock.game.core112R1.technologies.RabbitSpeaker;
import fr.badblock.game.core112R1.technologies.rabbitlisteners.PlayerBoosterRefreshListener;
import fr.badblock.game.core112R1.technologies.rabbitlisteners.PlayerDataReceiver;
import fr.badblock.game.core112R1.technologies.rabbitlisteners.PlayerPingListener;
import fr.badblock.game.core112R1.technologies.rabbitlisteners.SoundReceiver;
import fr.badblock.game.core112R1.technologies.rabbitlisteners.VanishTeleportListener;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.configuration.BadConfiguration;
import fr.badblock.gameapi.databases.LadderSpeaker;
import fr.badblock.gameapi.databases.SQLDatabase;
import fr.badblock.gameapi.events.api.PlayerJoinTeamEvent.JoinReason;
import fr.badblock.gameapi.players.BadblockOfflinePlayer;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.BadblockMode;
import fr.badblock.gameapi.players.BadblockTeam;
import fr.badblock.gameapi.players.bossbars.BossBarColor;
import fr.badblock.gameapi.players.bossbars.BossBarStyle;
import fr.badblock.gameapi.players.data.boosters.PlayerBooster;
import fr.badblock.gameapi.players.kits.DefaultKitContentManager;
import fr.badblock.gameapi.players.kits.PlayerKit;
import fr.badblock.gameapi.players.kits.PlayerKitContentManager;
import fr.badblock.gameapi.players.scoreboard.CustomObjective;
import fr.badblock.gameapi.portal.Portal;
import fr.badblock.gameapi.run.RunType;
import fr.badblock.gameapi.servers.ChestGenerator;
import fr.badblock.gameapi.servers.JoinItems;
import fr.badblock.gameapi.servers.MapProtector;
import fr.badblock.gameapi.utils.BukkitUtils;
import fr.badblock.gameapi.utils.ServerProperties;
import fr.badblock.gameapi.utils.general.JsonUtils;
import fr.badblock.gameapi.utils.general.MathsUtils;
import fr.badblock.gameapi.utils.itemstack.CustomInventory;
import fr.badblock.gameapi.utils.itemstack.ItemStackExtra;
import fr.badblock.gameapi.utils.itemstack.ItemStackFactory;
import fr.badblock.gameapi.utils.reflection.Reflector;
import fr.badblock.gameapi.utils.selections.CuboidSelection;
import fr.badblock.gameapi.utils.threading.TaskManager;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.MinecraftServer;

public class GamePlugin extends GameAPI {

	public static final String
	FOLDER_CONFIG 	  = "config",
	FOLDER_KITS		  = "kits",
	CONFIG_DATABASES  = "databases.json",
	WHITELIST		  = "whitelist.yml";
	public static Thread thread;

	public static final Type type = new TypeToken<Map<String, PlayerBooster>>() {}.getType();
	@Getter private static GamePlugin   instance;

	@Getter 
	private GameI18n 				    i18n;
	private Map<String, BadblockTeam>   teams				= Maps.newConcurrentMap();

	@Getter
	private GameServer					gameServer;

	@Getter@Setter@NonNull
	private MapProtector				mapProtector		= new DefaultMapProtector();
	@Getter
	private boolean						antiSpawnKill		= false;
	@Getter
	private long						antiBowSpam			= 0;
	@Getter@Setter@NonNull
	private PlayerKitContentManager     kitContentManager	= new DefaultKitContentManager(true);
	@Getter
	private JoinItems					joinItems;
	@Getter@Setter
	private boolean						leaverBusterEnabled = false;

	@Getter
	private GameScoreboard				badblockScoreboard;
	@Getter
	private SQLDatabase					sqlDatabase;
	@Getter
	private LadderSpeaker				ladderDatabase;
	@Getter
	private GameServerManager			gameServerManager;
	@Getter
	private RabbitSpeaker				rabbitSpeaker;

	@Getter
	private List<String>				whitelist;
	@Setter
	private boolean						whitelistStatus = false;


	@Getter
	private Map<String, Portal>			portals			= Maps.newConcurrentMap();
	private File						portalFolder	= null;

	@Getter
	private RunType						runType;
	@Getter@Setter
	private boolean						compassSelectNearestTarget;

	@Getter
	private double						serverBadcoinsBonus;
	@Getter
	private double						serverXpBonus;
	@Getter
	private List<BadblockPlayer> 		onlinePlayers;
	@Getter
	private String						i18nFolder 		= "/home/dev01/i18n";
	@Getter
	public String						gamePrefix;
	@Getter
	public PlayerBooster				booster;
	@Getter@Setter
	public boolean						finished;
	@Getter
	public String						ladderIp;
	@Getter
	public int							ladderPort;
	@Getter
	public String						reverseDNS;
	@Getter @Setter
	public boolean						antiAfk;
	@Getter
	private Meteor						meteor;
	@Getter
	private String							permissionPlace;
	@Getter
	private String							cluster;

	// TODO remove : c'est pour le nouveau backend
	@Getter
	private boolean					newbackend;
	
	@Getter@Setter
	private RabbitService			rabbitService;
	
	@Getter@Setter
	private PartySyncManager	partyManager;
	
	@Getter@Setter
	private ServerConfig				serverConfig;

	@Override
	public void onEnable() {
		thread		= Thread.currentThread();
		instance 	= this;
		GameAPI.API = this;

		i18n  		= new GameI18n(); // Cr�ation de l'I18N pour permettre la couleur

		if(!getDataFolder().exists()) getDataFolder().mkdirs();

		this.onlinePlayers = Collections.unmodifiableList(Lists.transform(MinecraftServer.getServer().getPlayerList().players, new Function<EntityPlayer, GameBadblockPlayer>() {
			@Override
			public GameBadblockPlayer apply(EntityPlayer player) {
				return (GameBadblockPlayer) player.getBukkitEntity();
			}
		}));

		try {
			/**
			 * Chargement de la configuration
			 */

			GameAPI.logColor("&b[GameAPI] &aLoading API...");
			long nano = System.nanoTime();

			File configFile  = new File(getDataFolder(), CONFIG_DATABASES);

			if(!configFile.exists())
				configFile.createNewFile();
			File configFolder = new File(getDataFolder(), FOLDER_CONFIG);
			if (!configFolder.exists()) configFolder.mkdirs();

			FTPConfig ftpConfig = JsonUtils.load(new File(configFolder, "ftp.json"), FTPConfig.class);
			GameServerConfig gameServerConfig = JsonUtils.load(new File(configFolder, "gameServer.json"), GameServerConfig.class);
			RankedConfig rankedConfig = JsonUtils.load(new File(configFolder, "ranked.json"), RankedConfig.class);
			LadderConfig ladderConfig = JsonUtils.load(new File(configFolder, "ladder.json"), LadderConfig.class);
			RabbitSettings rabbitSettings = JsonUtils.load(new File(configFolder, "rabbitmq.json"), RabbitSettings.class);
			ServerConfig serverConfig = JsonUtils.load(new File(configFolder, "server.json"), ServerConfig.class);
			SQLConfig sqlConfig = JsonUtils.load(new File(configFolder, "sql.json"), SQLConfig.class);
			SQLConfig webConfig = JsonUtils.load(new File(configFolder, "web.json"), SQLConfig.class);
			MongoSettings mongoSettings = JsonUtils.load(new File(configFolder, "mongosettings.json"), MongoSettings.class);
			setMongoService(MongoConnector.getInstance().createService("default", mongoSettings));

			setServerConfig(serverConfig);
			this.cluster = serverConfig.getCluster();
			
			GameAPI.logColor("§b[GameAPI] §aFetching permission data from MongoDB...");
			this.permissionPlace = serverConfig.getPermissionPlace();
			GameAPI.logColor("§b[GameAPI] §aPermission place: §e" + serverConfig.getPermissionPlace());

			partyManager = new PartySyncManager(getMongoService());
			
			// Get the database object
			DB db = getMongoService().getDb();
			// Get the permissions collection
			DBCollection collection = db.getCollection("permissions");
			// Create an empty query
			BasicDBObject query = new BasicDBObject();
			// Fetch everything with the empty query
			DBCursor cursor = collection.find(query);
			// Create a new permission map
			Map<String, Permissible> groups = new HashMap<>();
			// For each data
			Gson gs = new Gson();
			while (cursor.hasNext()) {
				// Get the data
				DBObject dbObject = cursor.next();
				// Deserialize the data object
				String json = gs.toJson(dbObject);
				// Serialize the data as a Permissible object
				Permissible permissible = gs.fromJson(json, Permissible.class);
				// Put the object in the groups map
				groups.put(dbObject.get("name").toString(), permissible);
			}
			// Create a permission manager, with the server place
			PermissionsManager.createPermissionManager(groups, serverConfig.getPermissionPlace());

			if(!GameAPI.TEST_MODE) {
				if(runType != RunType.DEV)
					GameAPI.logColor("&b[GameAPI] &a=> SQL : " + sqlConfig.sqlIp + ":" + sqlConfig.sqlPort);
				GameAPI.logColor("&b[GameAPI] &aConnecting to SQL...");

				sqlDatabase = new GameSQLDatabase(sqlConfig.sqlIp, Integer.toString(sqlConfig.sqlPort), sqlConfig.sqlUser, sqlConfig.sqlPassword, sqlConfig.sqlDatabase);
				((GameSQLDatabase) sqlDatabase).openConnection();

				rabbitSpeaker = new RabbitSpeaker(rabbitSettings);
				setRabbitService(rabbitSpeaker.getRabbitService());
				new ServerForceKillListener();
			} else {
				sqlDatabase = new FakeSQLDatabase();
			}
			
			i18nFolder = serverConfig.getI18nPath();
			if (i18nFolder == null || i18nFolder.isEmpty()) i18nFolder = getDataFolder().getAbsolutePath() + "/i18n/";
			loadI18n();

			teams 		 	 = Maps.newConcurrentMap();

			GameAPI.logColor("&b[GameAPI] &aLoading databases configuration...");

			runType = gameServerConfig.runType;
			
			GameAPI.logColor("&b[GameAPI] &aRegistering listeners and commands...");
			/**
			 * Chargement des Listeners
			 */
			BukkitUtils.instanciateListenersAndCommandsFrom(this, "fr.badblock.game.core112R1.listeners", "fr.badblock.game.core112R1.commands");

			joinItems = new GameJoinItems();    // Items donn� � l'arriv�e du joueur

			meteor = new Meteor(new Location(Bukkit.getWorld("world"), 0, 100, 0));
			
			new ItemStackExtras();

			GameAPI.logColor("&b[GameAPI] &aRegistering packets listeners ...");

			new VanishTeleportListener();
			new PlayerPingListener();
			new SoundReceiver();
			new PlayerDataReceiver();
			new PlayerBoosterRefreshListener();
			CrashBookFix.addListener(this);

			//AntiCheat.load();
			GamePlugin gamePlugin = this;
			getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");;
			getServer().getMessenger().registerIncomingPluginChannel(this, "SkinsRestorer", new PluginMessageListener()
			{
				public void onPluginMessageReceived(String channel, final Player player, final byte[] message)
				{
					if (!channel.equals("SkinsRestorer")) {
						return;
					}
					Bukkit.getScheduler().runTaskAsynchronously(gamePlugin, new Runnable()
					{
						public void run()
						{
							DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
							try
							{
								String subchannel = in.readUTF();
								if (subchannel.equalsIgnoreCase("SkinUpdate"))
								{
									in.readUTF();
									((BadblockPlayer)player).setTextureProperty(in.readUTF(), in.readUTF());
									/*try
									{
										SkinFactory.applySkin(player, new Property(in.readUTF(), in.readUTF(), in.readUTF()));
									}
									catch (Exception localException1) {}
									SkinFactory.updateSkin(player);*/
								}
							}
							catch (Exception e)
							{
								e.printStackTrace();
							}
						}
					});
				}
			});
			GameAPI.logColor("&b[GameAPI] &aCreating scoreboard...");

			badblockScoreboard = new GameScoreboard(); // Permet de g�rer le scoreboard
			/**
			 * Chargement des commandes par d�faut
			 */
			GameAPI.logColor("&b[GameAPI] &aRegistering commands...");

			File whitelistFile 			= new File(getDataFolder(), WHITELIST);
			FileConfiguration whitelist = YamlConfiguration.loadConfiguration(whitelistFile);

			if(!whitelist.contains("whitelist")){
				whitelist.set("whitelist", Arrays.asList("lelann"));
			}

			this.whitelist = new ArrayList<>();

			for(String player : whitelist.getStringList("whitelist"))
				this.whitelist.add(player.toLowerCase());

			whitelist.save(whitelistFile);
			enableWhiteList();

			// Set server bonus
			serverXpBonus = serverConfig.getBonusXp();
			serverBadcoinsBonus = serverConfig.getBonusCoins();

			// Loading GameServer
			GameAPI.logColor("&b[GameAPI] &aGameServer loading...");
			// GameServer apr�s tout
			this.gameServer 	   = new GameServer();
			this.gameServerManager = new GameServerManager(gameServerConfig, ftpConfig);
			this.gameServerManager.setRankedConfig(rankedConfig);
			this.setLeaverBusterEnabled(gameServerConfig.isLeaverBusterEnabled());
			this.getGameServerManager().start();

			nano = System.nanoTime() - nano;

			double ms = (double) nano / 1_000_000d;
			ms = MathsUtils.round(ms, 3);

			//Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "whitelist off");

			GameAPI.logColor("&b[GameAPI] &aAPI loaded! (" + ms + "ms)");

			File plugins = new File("plugins" + File.separator + "apiPlugins");
			plugins.mkdirs();
			Arrays.stream(Bukkit.getPluginManager().loadPlugins(plugins)).forEach(plugin -> Bukkit.getPluginManager().enablePlugin(plugin));

			if (this.getRunType().equals(RunType.GAME)) {
				setAntiAfk(true);
				TaskManager.scheduleAsyncRepeatingTask("boosterr", new Runnable() {
					@Override
					public void run() {
						if (booster != null && !booster.isExpired() && booster.isEnabled()) {
							long o = booster.getExpire();
							double remainingTime = o - System.currentTimeMillis();
							remainingTime /= 1000;
							double totalTime = booster.getBooster().getLength() / 1000;
							float a = (float) (remainingTime / totalTime);
							getOnlinePlayers().forEach(player -> player.addBossBar("boosters", player.getTranslatedMessage("boosters.bossbar", (int) ((booster.getBooster().getXpMultiplier() - 1) * 100), (int) ((booster.getBooster().getCoinsMultiplier() - 1) * 100), booster.getUsername())[0], a, BossBarColor.GREEN, BossBarStyle.SOLID));
						}else{
							getOnlinePlayers().forEach(player -> player.removeBossBar("boosters"));
						}
						Calendar calendar = Calendar.getInstance();
						calendar.setTimeInMillis(System.currentTimeMillis());
						int hours = calendar.get(Calendar.HOUR_OF_DAY);
						boolean t = false;
						if (hours >= 21 && hours < 10) {
							GamePlugin.getInstance().serverBadcoinsBonus = 6;
							GamePlugin.getInstance().serverXpBonus = 11;	
							t = true;
						}
						if (GamePlugin.getInstance().serverBadcoinsBonus > 1 || GamePlugin.getInstance().serverXpBonus > 1) {
							float a = 1.0f;
							if (t) {
								calendar = Calendar.getInstance();
								calendar.setTimeInMillis(System.currentTimeMillis());
								calendar.set(Calendar.HOUR_OF_DAY, 10);
								double remainingTime = calendar.getTimeInMillis() - System.currentTimeMillis();
								double totalTime = ((13 * 60) * 60) * 1000;
								a = (float) (remainingTime / totalTime);
							}
							float o = a;
							boolean p = t;
							getOnlinePlayers().forEach(player -> player.addBossBar("serverbooster",
									player.getTranslatedMessage("boosters.bossbarserver", 
											(int) ((GamePlugin.getInstance().serverXpBonus - 1) * 100),
											(int) ((GamePlugin.getInstance().serverBadcoinsBonus - 1) * 100),
											p ? player.getTranslatedMessage("boosters.night")[0] : "")[0], o, BossBarColor.GREEN, BossBarStyle.SOLID));
						}
						else 
							getOnlinePlayers().forEach(player -> player.removeBossBar("serverbooster"));
					}
				}, 20, 20);
				new GameStatisticsTask();
				new AntiAFKTask(this);
			}
		} catch (Throwable t){
			t.printStackTrace();

			GameAPI.logColor("&c[GameAPI] Error occurred while loading API. See the stack trace. Restarting...");
			Bukkit.shutdown();
		}
	}

	@Override
	public void onDisable(){
		try {
			if(!TEST_MODE)
				((GameSQLDatabase) sqlDatabase).closeConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if(i18n != null)
			i18n.save();

		gameServer.cancelReconnectionInvitations();

		if(getGameServerManager() != null)
			this.getGameServerManager().stop();

	}

	private void enableWhiteList()
	{
		String whitelist = System.getProperty("badblockwhitelist");

		if(whitelist == null){
			whitelist = ServerProperties.getProperties().getProperty("badblockwhitelist", "false");
		}

		boolean hasWhiteList = false;

		try {
			hasWhiteList = Boolean.parseBoolean(whitelist);
		} catch(Exception e){
			hasWhiteList = false;
		} finally {
			this.setWhitelistStatus(hasWhiteList);
		}
	}

	public void loadI18n(){
		File file = new File(this.getI18nFolder());
		i18n.load(file);
	}

	@Override
	public List<BadblockPlayer> getRealOnlinePlayers(){
		return onlinePlayers.stream().filter(player -> {
			return player.getBadblockMode() != BadblockMode.SPECTATOR;
		}).collect(Collectors.toList());
	}

	@Override
	public ItemStackFactory createItemStackFactory() {
		return new GameItemStackFactory();
	}

	@Override
	public ItemStackFactory createItemStackFactory(ItemStack item) {
		return new GameItemStackFactory(item);
	}

	@Override
	public ItemStackExtra createItemStackExtra(ItemStack itemStack) {
		return new GameItemExtra(itemStack);
	}

	@Override
	public CustomInventory createCustomInventory(int lines, String displayName) {
		return new GameCustomInventory(displayName, lines);
	}

	@Override
	public Map<String, PlayerKit> loadKits(String game) {
		File kitFolder = new File(new File(getDataFolder(), FOLDER_KITS), game.toLowerCase());

		Map<String, PlayerKit> kits = Maps.newConcurrentMap();

		if(!kitFolder.exists()) kitFolder.mkdirs();
		if(!kitFolder.isDirectory()) return kits;

		for(File file : kitFolder.listFiles()){
			PlayerKit kit = JsonUtils.load(file, GameKit.class);

			kits.put(kit.getKitName().toLowerCase(), kit);
		}

		return kits;
	}

	@Override
	public void registerTeams(int maxPlayers, ConfigurationSection configuration) {
		teams.clear();

		for(String key : configuration.getKeys(false)){
			key = key.toLowerCase();
			BadblockTeam team = new GameTeam(configuration.getConfigurationSection(key), maxPlayers);

			teams.put(key, team);
		}
	}

	@Override
	public Collection<BadblockTeam> getTeams() {
		return Collections.unmodifiableCollection(teams.values());
	}

	@Override
	public Collection<String> getTeamsKey() {
		return Collections.unmodifiableCollection(teams.keySet());
	}

	@Override
	public BadblockTeam getTeam(@NonNull String key) {
		return teams.get(key.toLowerCase());
	}

	@Override
	public void unregisterTeam(@NonNull BadblockTeam team) {
		teams.values().remove(team);

		getOnlinePlayers().stream().filter(player -> player.getCustomObjective() != null).forEach(player -> player.getCustomObjective().generate());
	}

	@Override
	public void balanceTeams(boolean sameSize) {
		if(teams.size() == 0)
			return;

		getOnlinePlayers().stream().filter(player -> { return player.getTeam() == null; }).forEach(player -> addPlayerInTeam(player, sameSize));

		int playersByTeam = (int) Math.ceil((double) getOnlinePlayers().size() / (double)getTeams().size());

		if(sameSize) {

			// New
			List<BadblockTeam> tooManyPlayers = getTeams().stream().filter(team -> { return team.getOnlinePlayers().size() > playersByTeam; }).collect(Collectors.toList());

			while (tooManyPlayers.size() >= 1)
			{
				BadblockTeam mostFilledTeam = null;
				for (BadblockTeam team : tooManyPlayers)
				{
					if (mostFilledTeam == null || (mostFilledTeam != null && mostFilledTeam.getOnlinePlayers().size() < team.getOnlinePlayers().size()))
					{
						mostFilledTeam = team;
					}
				}

				if (mostFilledTeam != null)
				{
					BadblockTeam smallestFilledTeam = null;
					for (BadblockTeam team : getTeams())
					{
						if (smallestFilledTeam == null || (smallestFilledTeam != null && smallestFilledTeam.getOnlinePlayers().size() > team.getOnlinePlayers().size()))
						{
							smallestFilledTeam = team;
						}
					}

					if (smallestFilledTeam != null)
					{
						Iterator<BadblockPlayer> iterator = mostFilledTeam.getOnlinePlayers().iterator();
						if (iterator.hasNext())
						{
							BadblockPlayer player = iterator.next();
							mostFilledTeam.leaveTeam(player);
							smallestFilledTeam.joinTeam(player, JoinReason.REBALANCING);
						}
					}

				}

				tooManyPlayers = getTeams().stream().filter(team -> { return team.getOnlinePlayers().size() > playersByTeam; }).collect(Collectors.toList());
			}

			Set<BadblockTeam> toRemove = getTeams().stream().filter(team -> { return team.getOnlinePlayers().size() == 0; }).collect(Collectors.toSet());
			toRemove.forEach(this::unregisterTeam);
		}
	}

	private void addPlayerInTeam(BadblockPlayer player, boolean sameSize){
		BadblockTeam team = getTeam(!sameSize);

		if(team == null){
			System.out.println("All teams are filled !");
			player.sendPlayer("lobby");
		} else {
			team.joinTeam(player, JoinReason.REBALANCING);
		}

	}

	private BadblockTeam getTeam(boolean max){
		BadblockTeam choosed = null;
		int          count	 = 0;

		for(BadblockTeam team : getTeams()){
			int cur = team.getOnlinePlayers().size();

			if(cur >= team.getMaxPlayers())
				continue;

			if(choosed == null || (count < cur && max) || (count > cur && !max)){
				choosed = team;
				count   = cur;
			}

		}

		return choosed;
	}

	@Override
	public void formatChat(boolean enabled, boolean team){
		formatChat(enabled, team, null);
	}

	@Override
	public void formatChat(boolean enabled, boolean team, String custom){
		ChatListener.enabled = enabled;
		ChatListener.team    = team;
		ChatListener.custom  = custom;
	}

	@Override
	public BadblockOfflinePlayer getOfflinePlayer(@NonNull String name) {
		return gameServer.getPlayers().get(name.toLowerCase());
	}

	@Override
	public CustomObjective buildCustomObjective(@NonNull String name) {
		return new GameCustomObjective(name);
	}

	@Override
	public void enableAntiSpawnKill() {
		antiSpawnKill = true;
	}

	@Override
	public void enableAntiBowSpam(long milliseconds) {
		antiBowSpam = milliseconds;
	}

	@Override
	public BadConfiguration loadConfiguration(File file) {
		return loadConfiguration(JsonUtils.loadObject(file));
	}

	@Override
	public BadConfiguration loadConfiguration(JsonObject object) {
		return new GameConfiguration(object);
	}

	@Override
	public void setDefaultKitContentManager(boolean allowDrop) {
		this.kitContentManager = new DefaultKitContentManager(allowDrop);
	}

	@Override
	public void managePortals(File folder) {
		if(portalFolder != null)
			throw new IllegalStateException("Portals are already loaded");

		if(!folder.exists())
			folder.mkdirs();

		this.portalFolder = folder;

		// On charge commandes/listeners qu'on avait pas load pour �conomiser sur les serveurs ou inutiles
		new PortalListener();

		for(File file : folder.listFiles()){
			Portal portal = JsonUtils.load(file, Portal.class);
			String name   = file.getName().split("\\.")[0];

			portal.setFile(file);

			portals.put(name.toLowerCase(), portal);
		}
	}

	public void addPortal(String name, Portal portal){
		File file = new File(portalFolder, name + ".json");
		portal.setFile(file);

		portals.put(name, portal);
	}

	public void savePortal(Portal portal){
		JsonUtils.save(portal.getFile(), portal, true);
	}

	public void removePortal(String name){
		Portal portal = portals.get(name);

		if(portal != null){
			portal.getFile().delete();
			portals.remove(name);
		}
	}

	@Override
	public Portal getPortal(@NonNull String name) {
		return portals.get(name.toLowerCase());
	}

	@Override
	public Portal getPortal(@NonNull Location location) {
		for(Portal portal : portals.values()){
			if(portal.getPortal().isInSelection(location))
				return portal;
		}

		return null;
	}

	@Override
	public Collection<Portal> getLoadedPortals() {
		return Collections.unmodifiableCollection(portals.values());
	}

	@Override
	public Collection<String> getWhitelistedPlayers() {
		return Collections.unmodifiableCollection(whitelist);
	}

	@Override
	public void whitelistPlayer(String player) {
		if(!whitelist.contains(player.toLowerCase())){
			whitelist.add(player.toLowerCase());
		}
	}

	@Override
	public void unwhitelistPlayer(String player) {
		if(whitelist.contains(player.toLowerCase())){
			whitelist.remove(player.toLowerCase());
		}
	}

	@Override
	public boolean isWhitelisted(String player){
		return whitelist.contains(player.toLowerCase());
	}

	@Override
	public boolean getWhitelistStatus() {
		return whitelistStatus;
	}

	@Override
	public void setEmptyChunks(CuboidSelection selection, boolean exclusion) {
		setPredicate(selection, exclusion, "empty");
	}

	@Override
	public void setLightChunks(CuboidSelection selection, boolean exclusion) {
		setPredicate(selection, exclusion, "light");
	}

	private void setPredicate(CuboidSelection selection, boolean exclusion, String fieldName){
		BiPredicate<Integer, Integer> predicate;

		if(selection == null){
			predicate = (i, j) -> false;
		} else {
			int minChunkX = ((int) selection.getMinX()) >> 4;
			int minChunkZ = ((int) selection.getMinZ()) >> 4;

			int maxChunkX = ((int) selection.getMaxX()) >> 4;
			int maxChunkZ = ((int) selection.getMaxZ()) >> 4;

			predicate = (i, j) -> (i >= minChunkX && i <= maxChunkX && j >= minChunkZ && j <= maxChunkZ) ? !exclusion : exclusion;
		}

		try {
			Class<?> clazz = Class.forName("fr.badblock.minecraftserver.BadblockEmptyChunk");
			Reflector reflec = new Reflector(null, clazz);

			reflec.setStaticFieldValue(fieldName, predicate);
		} catch(Exception e){}
	}

	@Override
	public void balancePlayers(BadblockPlayer leader, List<UUID> slaves) {
		if (!runType.equals(RunType.GAME)) return;
		TaskManager.runTaskLater(new Runnable() {
			@Override
			public void run() {
				if (leader.getTeam() != null) return;
				// Aucune team, alors aucun balance
				if (getTeams().isEmpty()) return;
				int playersByTeam = Bukkit.getMaxPlayers() / getTeams().size();
				// Récupération dans l'ordre des teams où il y a le moins de joueurs dedans
				List<BadblockTeam> teams = getTeams().stream().filter(team -> { return team.getOnlinePlayers().size() < playersByTeam; }).collect(Collectors.toList());
				Collections.sort(teams, new Comparator<BadblockTeam>() {
					public int compare(BadblockTeam badblockTeam1, BadblockTeam badblockTeam2) {
						return ((Integer) badblockTeam1.getOnlinePlayers().size()).compareTo(badblockTeam2.playersCurrentlyOnline());
					}
				});
				int playersInTeam = 0;
				Set<UUID> players = new HashSet<>(slaves);
				players.add(leader.getUniqueId());
				int id = 0;
				BadblockTeam team = teams.get(id);
				if (team == null) return;
				Set<String> done = new HashSet<>();
				Set<BadblockPlayer> plo = new HashSet<>();
				for (UUID uuid : players) {
					BadblockPlayer player = BukkitUtils.getPlayer(uuid);
					if (player != null)
						plo.add(player);
				}
				for (BadblockPlayer player : plo) {
					if (player.getTeam() != null) continue;
					if (!done.contains(player.getName().toLowerCase())) {
						done.add(player.getName().toLowerCase());
						if (playersInTeam >= playersByTeam) {
							playersInTeam = 0;
							id++;
							team = teams.get(id);
							if (team == null) return;
						}
						player.sendTranslatedMessage("teams.joinTeamWithHisParty", team.getChatName());
						team.joinTeam(player, JoinReason.REBALANCING);
						playersInTeam++;
					}
				}
			}
		}, 1);
	}

	@SuppressWarnings("deprecation")
	@Override
	public ItemStack generateQrCode(org.bukkit.World world, String content)
	{
		MapView view = getServer().createMap(world);

		view.getRenderers().clear();
		view.addRenderer(new QrCodeMap(content));

		return new ItemStack(Material.MAP, 1, view.getId());
	}

	@Override
	public ItemStack generateGoogleAuthQrCode(BadblockPlayer player, String googleAuthKey, String image)
	{
		return generateQrCode(player.getWorld(), "otpauth://totp/BadBlock%20(" + player.getName() + ")?secret=" + googleAuthKey + "&image=" + image);
	}

	@Override
	public ChestGenerator getChestGenerator() {
		// TODO Auto-generated method stub
		return null;
	}

}