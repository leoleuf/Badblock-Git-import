package fr.badblock.ladder.plugins.others;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.commands.Command;
import fr.badblock.ladder.api.config.Configuration;
import fr.badblock.ladder.api.config.ConfigurationProvider;
import fr.badblock.ladder.api.config.YamlConfiguration;
import fr.badblock.ladder.api.plugins.Plugin;
import fr.badblock.ladder.api.plugins.PluginsManager;
import fr.badblock.ladder.plugins.others.commands.AddServerCommand;
import fr.badblock.ladder.plugins.others.commands.AnimCommand;
import fr.badblock.ladder.plugins.others.commands.BadBlockOthersReloadCommand;
import fr.badblock.ladder.plugins.others.commands.BrohoofCommand;
import fr.badblock.ladder.plugins.others.commands.CBReportCommand;
import fr.badblock.ladder.plugins.others.commands.FriendCommand;
import fr.badblock.ladder.plugins.others.commands.HelpCommand;
import fr.badblock.ladder.plugins.others.commands.LWhitelistCommand;
import fr.badblock.ladder.plugins.others.commands.PartyCommand;
import fr.badblock.ladder.plugins.others.commands.RemoveServerCommand;
import fr.badblock.ladder.plugins.others.commands.big.BigAutoBan;
import fr.badblock.ladder.plugins.others.commands.big.BigBanCommand;
import fr.badblock.ladder.plugins.others.commands.big.BigPardonCommand;
import fr.badblock.ladder.plugins.others.commands.big.BigTempBanCommand;
import fr.badblock.ladder.plugins.others.commands.mod.BadFilterCommand;
import fr.badblock.ladder.plugins.others.commands.mod.ConnectCommand;
import fr.badblock.ladder.plugins.others.commands.mod.GhostConnectCommand;
import fr.badblock.ladder.plugins.others.commands.mod.OnlineStaffCommand;
import fr.badblock.ladder.plugins.others.commands.mod.RConnectCommand;
import fr.badblock.ladder.plugins.others.commands.mod.ReportCommand;
import fr.badblock.ladder.plugins.others.commands.mod.SpyMsgCommand;
import fr.badblock.ladder.plugins.others.commands.mod.TrackCommand;
import fr.badblock.ladder.plugins.others.commands.mod.punish.CommandBan;
import fr.badblock.ladder.plugins.others.commands.mod.punish.CommandBanip;
import fr.badblock.ladder.plugins.others.commands.mod.punish.CommandKick;
import fr.badblock.ladder.plugins.others.commands.mod.punish.CommandLogs;
import fr.badblock.ladder.plugins.others.commands.mod.punish.CommandMute;
import fr.badblock.ladder.plugins.others.commands.mod.punish.CommandSanction;
import fr.badblock.ladder.plugins.others.commands.mod.punish.CommandTempban;
import fr.badblock.ladder.plugins.others.commands.mod.punish.CommandTempbanip;
import fr.badblock.ladder.plugins.others.commands.mod.punish.CommandUnban;
import fr.badblock.ladder.plugins.others.commands.mod.punish.CommandUnbanip;
import fr.badblock.ladder.plugins.others.commands.mod.punish.CommandUnmute;
import fr.badblock.ladder.plugins.others.commands.msg.MsgCommand;
import fr.badblock.ladder.plugins.others.commands.msg.RCommand;
import fr.badblock.ladder.plugins.others.database.BadblockDatabase;
import fr.badblock.ladder.plugins.others.friends.FriendPlayer;
import fr.badblock.ladder.plugins.others.listeners.PlayerDisconnectListener;
import fr.badblock.ladder.plugins.others.listeners.ServerConnectListener;
import fr.badblock.ladder.plugins.others.rabbit.receivers.BadFilterListener;
import fr.badblock.ladder.plugins.others.rabbit.receivers.BadReportListener;
import fr.badblock.ladder.plugins.others.rabbit.receivers.ForceCommandListener;
import fr.badblock.ladder.plugins.others.rabbit.receivers.GuardianReceiveBanListener;
import fr.badblock.ladder.plugins.others.rabbit.receivers.GuardianReceiveBroadcastListener;
import fr.badblock.ladder.plugins.others.rabbit.receivers.GuardianReceiveKickListener;
import fr.badblock.ladder.plugins.others.rabbit.receivers.GuardianReceiveReportListener;
import fr.badblock.ladder.plugins.others.rabbit.receivers.ReceiveSanctionListener;
import fr.badblock.rabbitconnector.RabbitConnector;

public class BadBlockOthers extends Plugin {

	private static BadBlockOthers instance;
	private Gson gson;
	public Configuration configuration;
	public List<String> help;
	private String host;
	private String user;
	private String pass;
	private int port;
	private String db;
	public long launchedTime;
	public AnimCommand animCommand;
	public File configFile;
	public SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy HH:mm:ss");
	public List<String> whitelist = new ArrayList<>();
	public boolean whitelistEnabled = false;
	
	@Override
	public void onEnable() {
		instance = this;
		launchedTime = System.currentTimeMillis();
		// Set
		gson = new GsonBuilder().create();
		File file = this.getDataFolder();
		if (!file.exists())
			file.mkdir();
		configFile = new File(file, "config.yml");
		try {
			configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.whitelist = configuration.getStringList("whitelist.list");
		this.whitelistEnabled = configuration.getBoolean("whitelist.state");
		RabbitConnector.getInstance().newService("default", configuration.getInt("rabbit.port"),
				configuration.getString("rabbit.username"), configuration.getString("rabbit.password"), configuration.getString("rabbit.virtualhost"), configuration.getString("rabbit.hostname"));
		new GuardianReceiveBanListener();
		new GuardianReceiveBroadcastListener();
		new GuardianReceiveKickListener();
		new GuardianReceiveReportListener();
		new BadFilterListener();
		new BadReportListener();
		new ReceiveSanctionListener();
		new ForceCommandListener();
		host = configuration.getString("db.host");
		port = configuration.getInt("db.port");
		user = configuration.getString("db.user");
		pass = configuration.getString("db.pass");
		db = configuration.getString("db.db");
		help = configuration.getStringList("help");
		BadblockDatabase.getInstance().connect(host, port, user, pass, db);
		PluginsManager pluginManager = this.getLadder().getPluginsManager();
		pluginManager.registerEvents(this, new PlayerDisconnectListener());
		pluginManager.registerEvents(this, new ServerConnectListener());
		pluginManager.registerCommand(this, animCommand = new AnimCommand());
		pluginManager.registerCommand(this, new FriendCommand());
		pluginManager.registerCommand(this, new BadFilterCommand());
		pluginManager.registerCommand(this, new BadBlockOthersReloadCommand());
		pluginManager.registerCommand(this, new AddServerCommand());
		pluginManager.registerCommand(this, new CBReportCommand());
		pluginManager.registerCommand(this, new RemoveServerCommand());
		pluginManager.registerCommand(this, new HelpCommand());
		pluginManager.registerCommand(this, new PartyCommand());
		pluginManager.registerCommand(this, new MsgCommand());
		pluginManager.registerCommand(this, new LWhitelistCommand());
		pluginManager.registerCommand(this, new RCommand());
		pluginManager.registerCommand(this, new SpyMsgCommand());
		pluginManager.registerCommand(this, new OnlineStaffCommand());
		pluginManager.registerCommand(this, new BigAutoBan());
		pluginManager.registerCommand(this, new BrohoofCommand());
		pluginManager.registerCommand(this, new BigBanCommand());
		pluginManager.registerCommand(this, new BigPardonCommand());
		pluginManager.registerCommand(this, new BigTempBanCommand());
		pluginManager.registerCommand(this, new ConnectCommand());
		pluginManager.registerCommand(this, new GhostConnectCommand());
		// pluginManager.registerCommand(this, new TrackCommand());
		pluginManager.registerCommand(this, new TrackCommand());
		pluginManager.registerCommand(this, new ReportCommand());
		pluginManager.registerCommand(this, new RConnectCommand());
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					Field field = PluginsManager.class.getDeclaredField("commands");
					field.setAccessible(true);
					@SuppressWarnings("unchecked")
					Map<String, Command> value = (Map<String, Command>) field.get(pluginManager);
					value.remove("ban");
					value.remove("banip");
					value.remove("kick");
					value.remove("mute");
					value.remove("sanction");
					value.remove("tempban");
					value.remove("tempbanip");
					value.remove("unban");
					value.remove("unbanip");
					value.remove("unmute");
					value.remove("pardon");
					value.remove("pardonip");
				} catch (Exception error) {
					error.printStackTrace();
				}
				pluginManager.registerCommand(instance, new CommandBan());
				pluginManager.registerCommand(instance, new CommandBanip());
				pluginManager.registerCommand(instance, new CommandKick());
				pluginManager.registerCommand(instance, new CommandMute());
				pluginManager.registerCommand(instance, new CommandSanction());
				pluginManager.registerCommand(instance, new CommandTempban());
				pluginManager.registerCommand(instance, new CommandTempbanip());
				pluginManager.registerCommand(instance, new CommandUnban());
				pluginManager.registerCommand(instance, new CommandUnbanip());
				pluginManager.registerCommand(instance, new CommandUnmute());
				pluginManager.registerCommand(instance, new CommandLogs());
			}
		}, 1000);
		try {
			// Map<String, CustomBungee> bungees = new HashMap<>();
			Field field = Ladder.getInstance().getClass().getSuperclass().getDeclaredField("bungeeCords");
			field.setAccessible(true);
			// bungeeCords.entrySet().forEach(bungee ->
			// bungees.put(bungee.getKey(), new
			// CustomBungee(bungee.getValue())));
			// bungeeCords.clear();
			// bungeeCords.putAll(bungees);
		} catch (Exception error) {
			error.printStackTrace();
		}
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				System.gc();
			}
		}, 30000, 30000);
		Ladder ladder = this.getLadder();
		ladder.getOnlinePlayers().forEach(player -> {
			FriendPlayer friendPlayer = FriendPlayer.load(player);
			if (friendPlayer == null)
				return;
			if (player.getBukkitServer() == null)
				return;
			if (player.getBukkitServer() == null)
				return;
			if (player.getBukkitServer().getName() == null)
				return;
			if (!player.getBukkitServer().getName().startsWith("login"))
				friendPlayer.send(player);
		});
		// skeleton servers
		// ladder.addBukkitServer(new InetSocketAddress("127.0.0.1", 65530),
		// "login");
		// ladder.addBukkitServer(new InetSocketAddress("127.0.0.1", 65531),
		// "lobby");
	}

	@Override
	public void onDisable() {
		this.getLadder().getOnlinePlayers().forEach(player -> FriendPlayer.unload(player, true));
	}

	public Gson getGson() {
		return this.gson;
	}

	public static BadBlockOthers getInstance() {
		return instance;
	}

	public void whitelistSave() {
		this.getConfig().set("whitelist.list", whitelist);
		this.getConfig().set("whitelist.state", whitelistEnabled);
		this.saveConfig();
	}

}
