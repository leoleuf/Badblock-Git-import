package fr.badblock.bungeecord.plugins.others;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import fr.badblock.bungeecord.plugins.ladder.LadderBungee;
import fr.badblock.bungeecord.plugins.ladder.listeners.BungeePlayersUpdateListener;
import fr.badblock.bungeecord.plugins.ladder.listeners.PlayersUpdateListener;
import fr.badblock.bungeecord.plugins.others.commands.BListCommand;
import fr.badblock.bungeecord.plugins.others.commands.BOAddInsultCommand;
import fr.badblock.bungeecord.plugins.others.commands.BOReloadCommand;
import fr.badblock.bungeecord.plugins.others.commands.BORemoveInsultCommand;
import fr.badblock.bungeecord.plugins.others.commands.BTPS;
import fr.badblock.bungeecord.plugins.others.commands.CLCommand;
import fr.badblock.bungeecord.plugins.others.commands.CheatCommand;
import fr.badblock.bungeecord.plugins.others.commands.CodeCommand;
import fr.badblock.bungeecord.plugins.others.commands.DoneCommand;
import fr.badblock.bungeecord.plugins.others.commands.FKickCommand;
import fr.badblock.bungeecord.plugins.others.commands.GNickCommand;
import fr.badblock.bungeecord.plugins.others.commands.ModoCommand;
import fr.badblock.bungeecord.plugins.others.commands.RCCommand;
import fr.badblock.bungeecord.plugins.others.commands.RNickCommand;
import fr.badblock.bungeecord.plugins.others.commands.ReportLagCommand;
import fr.badblock.bungeecord.plugins.others.commands.ThxCommand;
import fr.badblock.bungeecord.plugins.others.commands.VoteCommand;
import fr.badblock.bungeecord.plugins.others.database.BadblockDatabase;
import fr.badblock.bungeecord.plugins.others.database.Request;
import fr.badblock.bungeecord.plugins.others.database.Request.RequestType;
import fr.badblock.bungeecord.plugins.others.discord.TemmieWebhook;
import fr.badblock.bungeecord.plugins.others.listeners.ChatListener;
import fr.badblock.bungeecord.plugins.others.listeners.PlayerQuitListener;
import fr.badblock.bungeecord.plugins.others.listeners.PluginMessageListener;
import fr.badblock.bungeecord.plugins.others.listeners.PreLoginListener;
import fr.badblock.bungeecord.plugins.others.listeners.ProxyBoundListener;
import fr.badblock.bungeecord.plugins.others.logs.filters.IHConnectedFilter;
import fr.badblock.bungeecord.plugins.others.logs.filters.IHResetByPeerFilter;
import fr.badblock.bungeecord.plugins.others.logs.filters.InjectableFilter;
import fr.badblock.bungeecord.plugins.others.modules.BadAdvertsModule;
import fr.badblock.bungeecord.plugins.others.modules.BadCommunitySpookerModule;
import fr.badblock.bungeecord.plugins.others.modules.BadInsultModule;
import fr.badblock.bungeecord.plugins.others.modules.BadPseudoModule;
import fr.badblock.bungeecord.plugins.others.modules.GuardianModule;
import fr.badblock.bungeecord.plugins.others.modules.PartyChatModule;
import fr.badblock.bungeecord.plugins.others.receivers.PermissionMessageListener;
import fr.badblock.bungeecord.plugins.others.receivers.PlayerMessageListener;
import fr.badblock.bungeecord.plugins.others.receivers.ServerBroadcastListener;
import fr.badblock.bungeecord.plugins.others.tasks.FallbackTask;
import fr.badblock.bungeecord.plugins.others.utils.LagTask;
import fr.badblock.bungeecord.plugins.others.utils.TPS;
import fr.badblock.common.commons.technologies.rabbitmq.RabbitConnector;
import fr.badblock.common.commons.technologies.rabbitmq.RabbitPacketType;
import fr.badblock.common.commons.technologies.rabbitmq.RabbitService;
import fr.badblock.common.commons.technologies.redis.RedisConnector;
import fr.badblock.common.commons.technologies.redis.RedisService;
import fr.badblock.common.commons.utils.Encodage;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.connection.InitialHandler;

@Getter
@Setter
public class BadBlockBungeeOthers extends Plugin {

	@Getter
	@Setter
	private static BadBlockBungeeOthers instance;

	private static Gson gson = new Gson();

	public SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy HH:mm:ss");

	private Configuration configuration;
	private List<InjectableFilter> filters = new LinkedList<>();
	private RabbitService rabbitService;
	private List<UUID> reengagedUUIDs;
	private TimerTask timerTask;
	private TimerTask timerTask2;
	private TimerTask timerTask3;
	private TimerTask timerTask4;
	private boolean done = false;
	private boolean deleted = false;
	private int recordId = -1;
	private long deleteTime = -1;
	private long connections = -1;
	private RedisService redisConnector;
	private boolean finished;
	private long openTime;
	private long time = 3600 * 5;
	private long maxPlayers = 1;
	private int marginDelete;
	public static final Type bungeeDataType = new TypeToken<HashMap<String, Bungee>>() {
	}.getType();

	private TemmieWebhook temmie = new TemmieWebhook(
			"https://discordapp.com/api/webhooks/351074484196868096/EQE2yz9EIgBROBTnkze8ese7jANormT8K8d6SmR1_KRSYrY4UU2f5clb400UJxeSwmHL");

	@SuppressWarnings("deprecation")
	@Override
	public void onEnable() {
		instance = this;
		this.getProxy().registerChannel("BungeeCord");
		new FallbackTask();
		openTime = System.currentTimeMillis();
		reengagedUUIDs = new ArrayList<>();
		ProxyServer proxy = this.getProxy();
		// fichier de config
		try {
			if (!this.getDataFolder().exists())
				this.getDataFolder().mkdirs();
			File file = new File(this.getDataFolder(), "config.yml");
			if (!file.exists())
				file.createNewFile();
			configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String host = configuration.getString("db.host");
		int port = configuration.getInt("db.port");
		String user = configuration.getString("db.user");
		String pass = configuration.getString("db.pass");
		String db = configuration.getString("db.db");
		BadblockDatabase.getInstance().connect(host, port, user, pass, db);
		rabbitService = RabbitConnector.getInstance().newService("default", configuration.getString("rabbit.hostname"),
				configuration.getInt("rabbit.port"), configuration.getString("rabbit.username"),
				configuration.getString("rabbit.password"), configuration.getString("rabbit.virtualhost"));
		redisConnector = RedisConnector.getInstance().newService("default", configuration.getString("redis.hostname"),
				configuration.getInt("redis.port"), configuration.getString("redis.password"),
				configuration.getInt("redis.database"));
		new PlayerMessageListener();
		new PermissionMessageListener();
		new ServerBroadcastListener();
		new TPS();
		new LagTask();
		PluginManager pluginManager = proxy.getPluginManager();
		pluginManager.registerListener(this, new ProxyBoundListener());
		pluginManager.registerListener(this, new PartyChatModule());
		pluginManager.registerListener(this, new ChatListener());
		pluginManager.registerListener(this, new PreLoginListener());
		pluginManager.registerListener(this, new PlayerQuitListener());
		pluginManager.registerListener(this, new PluginMessageListener());
		pluginManager.registerListener(this, new BadInsultModule());
		pluginManager.registerListener(this, new BadCommunitySpookerModule());
		pluginManager.registerListener(this, new BadAdvertsModule());
		pluginManager.registerListener(this, new GuardianModule());
		pluginManager.registerListener(this, new BadPseudoModule());
		pluginManager.registerCommand(this, new FKickCommand());
		pluginManager.registerCommand(this, new BOReloadCommand());
		pluginManager.registerCommand(this, new BListCommand());
		pluginManager.registerCommand(this, new VoteCommand());
		pluginManager.registerCommand(this, new CodeCommand());
		pluginManager.registerCommand(this, new ReportLagCommand());
		pluginManager.registerCommand(this, new ModoCommand());
		pluginManager.registerCommand(this, new RNickCommand());
		pluginManager.registerCommand(this, new GNickCommand());
		pluginManager.registerCommand(this, new DoneCommand());
		// pluginManager.registerCommand(this, new TrackCommand());
		pluginManager.registerCommand(this, new BTPS());
		pluginManager.registerCommand(this, new BOAddInsultCommand());
		pluginManager.registerCommand(this, new ThxCommand());
		pluginManager.registerCommand(this, new CheatCommand());
		pluginManager.registerCommand(this, new RCCommand());
		pluginManager.registerCommand(this, new CLCommand());
		pluginManager.registerCommand(this, new BORemoveInsultCommand());
		proxy.getPlayers().forEach(player -> Player.get(player));

		filters.clear();
		filters.add(new IHConnectedFilter());
		filters.add(new IHResetByPeerFilter());
		for (InjectableFilter filter : filters) {
			filter.inject();
		}
		proxy.registerChannel("GuardianBroad");
		proxy.registerChannel("GuardianReport");
		proxy.registerChannel("GuardianKick");
		proxy.registerChannel("GuardianBan");
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				LadderBungee.getInstance().countEnvironment = 1;
				new PlayersUpdateListener();
				new BungeePlayersUpdateListener();
			}
		}, 1000);
		timerTask2 = new TimerTask() {
			@Override
			public void run() {
				if (System.currentTimeMillis() - openTime > 900_000
						&& LadderBungee.getInstance().bungeePlayerList.size() == 0) {
					setDone(true);
				}
				String a = ProxyServer.getInstance().getConfig().getListeners().iterator().next().getHost()
						.getHostString() + ":"
						+ ProxyServer.getInstance().getConfig().getListeners().iterator().next().getHost().getPort();
				Map<Integer, Integer> versions = new HashMap<>();
				Data data = new Data();
				for (ProxiedPlayer player : BungeeCord.getInstance().getPlayers()) {
					int version = ((InitialHandler) player.getPendingConnection()).getVersion();
					if (!versions.containsKey(version))
						versions.put(version, 1);
					else
						versions.put(version, versions.get(version) + 1);
				}
				data.versions = versions;
				data.bungeeIp = a;
				// int bungeeId =
				rabbitService.sendPacket("bungee.", new Gson().toJson(data), Encodage.UTF8, RabbitPacketType.PUBLISHER,
						5000, false);
				long count = BungeeCord.getInstance().getPlayers().stream()
						.filter(player -> player.getPendingConnection().isOnlineInfo()).count();
				rabbitService.sendPacket("bungee.online", a + ";" + Long.toString(count), Encodage.UTF8,
						RabbitPacketType.PUBLISHER, 5000, false);
			}
		};
		new Timer().schedule(timerTask2, 1000, 1000);


		timerTask3 = new TimerTask() {
			@Override
			public void run() {
				BadblockDatabase.getInstance().addSyncRequest(
						new Request("SELECT `value` FROM keyValues WHERE `key` = 'slots';", RequestType.GETTER) {
							@Override
							public void done(ResultSet resultSet) {
								try {
									if (resultSet.next()) {
										String o = resultSet.getString("value");
										try {
											int p = Integer.parseInt(o);
											LadderBungee.getInstance().slots = p;
										} catch (Exception error) {
											error.printStackTrace();
										}
									}
								} catch (Exception errora) {
									errora.printStackTrace();
								}
							}
						});
			}
		};
		new Timer().schedule(timerTask3, 1000, 30000);

	}

	@Override
	public void onDisable() {
		filters.forEach(filter -> filter.reset());
		if (timerTask != null)
			timerTask.cancel();
		if (timerTask2 != null)
			timerTask2.cancel();
		if (timerTask3 != null)
			timerTask3.cancel();
		if (timerTask4 != null)
			timerTask4.cancel();
	}

	public String getMessage(List<String> list) {
		StringBuilder stringBuilder = new StringBuilder();
		Iterator<String> iterator = list.iterator();
		while (iterator.hasNext()) {
			String message = iterator.next();
			stringBuilder.append(message);
			if (iterator.hasNext())
				stringBuilder.append(System.lineSeparator());
		}
		return stringBuilder.toString();
	}

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

	public static String getDate() {
		return dateFormat.format(new Date());
	}

}
