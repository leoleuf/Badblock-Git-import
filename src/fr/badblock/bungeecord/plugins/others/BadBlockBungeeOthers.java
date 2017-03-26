package fr.badblock.bungeecord.plugins.others;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
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

import com.cloudflare.api.CloudflareAccess;
import com.cloudflare.api.requests.dns.DNSDeleteRecord;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import fr.badblock.bungeecord.BungeeCord;
import fr.badblock.bungeecord.api.ProxyServer;
import fr.badblock.bungeecord.api.connection.ProxiedPlayer;
import fr.badblock.bungeecord.api.plugin.Plugin;
import fr.badblock.bungeecord.api.plugin.PluginManager;
import fr.badblock.bungeecord.config.Configuration;
import fr.badblock.bungeecord.config.ConfigurationProvider;
import fr.badblock.bungeecord.config.YamlConfiguration;
import fr.badblock.bungeecord.connection.InitialHandler;
import fr.badblock.bungeecord.plugins.ladder.LadderBungee;
import fr.badblock.bungeecord.plugins.ladder.LadderListener;
import fr.badblock.bungeecord.plugins.others.commands.BListCommand;
import fr.badblock.bungeecord.plugins.others.commands.BOAddInsultCommand;
import fr.badblock.bungeecord.plugins.others.commands.BOReloadCommand;
import fr.badblock.bungeecord.plugins.others.commands.BORemoveInsultCommand;
import fr.badblock.bungeecord.plugins.others.commands.CheatCommand;
import fr.badblock.bungeecord.plugins.others.commands.DoneCommand;
import fr.badblock.bungeecord.plugins.others.commands.LinkCommand;
import fr.badblock.bungeecord.plugins.others.commands.ThxCommand;
import fr.badblock.bungeecord.plugins.others.database.BadblockDatabase;
import fr.badblock.bungeecord.plugins.others.database.Request;
import fr.badblock.bungeecord.plugins.others.database.WebDatabase;
import fr.badblock.bungeecord.plugins.others.database.Request.RequestType;
import fr.badblock.bungeecord.plugins.others.exceptions.UnableToDeleteDNSException;
import fr.badblock.bungeecord.plugins.others.listeners.PlayerQuitListener;
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
import fr.badblock.common.commons.technologies.rabbitmq.RabbitConnector;
import fr.badblock.common.commons.technologies.rabbitmq.RabbitPacketType;
import fr.badblock.common.commons.technologies.rabbitmq.RabbitService;
import fr.badblock.common.commons.technologies.redis.RedisConnector;
import fr.badblock.common.commons.technologies.redis.RedisService;
import fr.badblock.common.commons.utils.Encodage;
import lombok.Getter;
import lombok.Setter;
import net.sf.json.JSONObject;

@Getter @Setter public class BadBlockBungeeOthers extends Plugin {

	@Getter @Setter private static BadBlockBungeeOthers instance;

	private Configuration							    configuration;
	private List<InjectableFilter> 						filters 			= new LinkedList<>();
	private RabbitService								rabbitService;
	private List<UUID>									reengagedUUIDs;
	private TimerTask									timerTask;
	private TimerTask									timerTask2;
	private TimerTask									timerTask3;
	private boolean										done = false;
	private boolean										deleted = false;
	private int											recordId = -1;
	private long										deleteTime = -1;
	private long										connections = -1;
	private RedisService								redisConnector;
	private boolean										finished;
	private long										delete = 10;
	private long 										nbTimes;
	private long 										lastNb;
	private long										lastCheck;
	private long										openTime;
	private long										time = 14400;
	public static final Type bungeeDataType 	= new TypeToken<HashMap<String, Bungee>>() {}.getType();
	private CloudflareAccess access;

	@SuppressWarnings("deprecation")
	@Override
	public void onEnable() {
		instance = this;
		openTime = System.currentTimeMillis();
		reengagedUUIDs = new ArrayList<>();
		ProxyServer proxy = this.getProxy();
		// fichier de config
		try {
			if (!this.getDataFolder().exists()) this.getDataFolder().mkdirs();
			File file = new File(this.getDataFolder(), "config.yml");
			if (!file.exists()) file.createNewFile();
			configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String host = configuration.getString("db.host");
		int port = configuration.getInt("db.port");
		String user = configuration.getString("db.user");
		String pass = configuration.getString("db.pass");
		String db = configuration.getString("db.db");
		String webhost = configuration.getString("webdb.host");
		int webport = configuration.getInt("webdb.port");
		String webuser = configuration.getString("webdb.user");
		String webpass = configuration.getString("webdb.pass");
		String webdb = configuration.getString("webdb.db");
		WebDatabase.getInstance().connect(webhost, webport, webuser, webpass, webdb);
		BadblockDatabase.getInstance().connect(host, port, user, pass, db);
		access = new CloudflareAccess(configuration.getString("cloudflare.email"), configuration.getString("cloudflare.key"));
		System.out.println(ProxyServer.getInstance().getConfig().getListeners().iterator().next().getHost().getHostString());
		rabbitService = RabbitConnector.getInstance().newService("default", configuration.getString("rabbit.hostname"), configuration.getInt("rabbit.port"), configuration.getString("rabbit.username"),
				configuration.getString("rabbit.password"), configuration.getString("rabbit.virtualhost"));
		redisConnector = RedisConnector.getInstance().newService("default", configuration.getString("redis.hostname"), configuration.getInt("redis.port"), configuration.getString("redis.password"), configuration.getInt("redis.database"));
		new PlayerMessageListener();
		new PermissionMessageListener();
		new ServerBroadcastListener();
		PluginManager pluginManager = proxy.getPluginManager();
		pluginManager.registerListener(this, new ProxyBoundListener());
		pluginManager.registerListener(this, new PartyChatModule());
		pluginManager.registerListener(this, new PreLoginListener());
		pluginManager.registerListener(this, new PlayerQuitListener());
		pluginManager.registerListener(this, new BadInsultModule());
		pluginManager.registerListener(this, new BadCommunitySpookerModule());
		pluginManager.registerListener(this, new BadAdvertsModule());
		pluginManager.registerListener(this, new GuardianModule());
		pluginManager.registerListener(this, new BadPseudoModule());
		pluginManager.registerCommand(this, new BOReloadCommand());
		pluginManager.registerCommand(this, new BListCommand());
		pluginManager.registerCommand(this, new DoneCommand());
		//pluginManager.registerCommand(this, new TrackCommand());
		pluginManager.registerCommand(this, new BOAddInsultCommand());
		pluginManager.registerCommand(this, new ThxCommand());
		pluginManager.registerCommand(this, new CheatCommand());
		pluginManager.registerCommand(this, new LinkCommand());
		pluginManager.registerCommand(this, new BORemoveInsultCommand());
		proxy.getPlayers().forEach(player -> Player.get(player));
		lastCheck = System.currentTimeMillis() + 1800_000L;
		lastNb = 0;

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				deleteDNS();
			}
		});

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
		timerTask2 = new TimerTask() {
			@Override
			public void run() {
				if (System.currentTimeMillis() - openTime > 900_000 && LadderBungee.getInstance().bungeePlayerList.size() == 0) {
					setDone(true);
				}
				if (System.currentTimeMillis() > lastCheck) {
					if (LadderBungee.getInstance().bungeePlayerList.size() < lastNb) {
						if (nbTimes >= 5)
							setDone(true);
						nbTimes++;
					}else nbTimes = 0;

					lastNb = LadderBungee.getInstance().bungeePlayerList.size();
					lastCheck = System.currentTimeMillis() + 1800_000L;
				}
				String a = ProxyServer.getInstance().getConfig().getListeners().iterator().next().getHost().getHostString() + ":" + ProxyServer.getInstance().getConfig().getListeners().iterator().next().getHost().getPort();
				Map<Integer, Integer> versions = new HashMap<>();
				Data data = new Data();
				for (ProxiedPlayer player : BungeeCord.getInstance().getPlayers()) {
					int version = ((InitialHandler)player.getPendingConnection()).getVersion();
					if (!versions.containsKey(version)) versions.put(version, 1);
					else versions.put(version, versions.get(version) + 1);
				}
				data.versions = versions;
				data.bungeeIp = a;
				//int bungeeId = 
				rabbitService.sendPacket("bungee.", new Gson().toJson(data), Encodage.UTF8, RabbitPacketType.PUBLISHER, 5000, false);
			}
		};
		new Timer().schedule(timerTask2, 1000, 1000);
		timerTask3 = new TimerTask() {
			@Override
			public void run() {
				boolean d = !finished ? done : false;
				long bungeeTimestamp = !finished ? System.currentTimeMillis() + 30000 : 0;
				String a = ProxyServer.getInstance().getConfig().getListeners().iterator().next().getHost().getHostString() + ":" + ProxyServer.getInstance().getConfig().getListeners().iterator().next().getHost().getPort();
				BadblockDatabase.getInstance().addSyncRequest(new Request("UPDATE absorbances SET done = '" + d + "', bungeeTimestamp = '" + bungeeTimestamp + "' WHERE ip = '" + a + "'", RequestType.SETTER));
				BadblockDatabase.getInstance().addSyncRequest(new Request("SELECT `value` FROM keyValues WHERE `key` = 'slots';", RequestType.GETTER) {
					@Override
					public void done(ResultSet resultSet) {
						try {
							if (resultSet.next()) {
								String o = resultSet.getString("value");
								try {
									int p = Integer.parseInt(o);
									LadderBungee.getInstance().slots = p;
								}catch(Exception error) {
									try {
										BadblockDatabase.getInstance().addSyncRequest(new Request("SELECT slots FROM absorbances;", RequestType.GETTER) {
											@Override
											public void done(ResultSet result) {
												try {
													int slots = 0;
													while (result.next())
														slots += result.getInt("slots");
													LadderBungee.getInstance().slots = slots;
												}catch(Exception errorb) {
													errorb
													.printStackTrace();
												}
											}
										});
									}catch(Exception errore) {
										errore.printStackTrace();
									}
								}
							}
						}catch(Exception errora) {
							errora.printStackTrace();
						}
					}
				});
				BadblockDatabase.getInstance().addSyncRequest(new Request("SELECT `value` FROM keyValues WHERE `key` = 'timestampMax';", RequestType.GETTER) {
					@Override
					public void done(ResultSet resultSet) {
						try {
							if (resultSet.next()) {
								String o = resultSet.getString("value");
								try {
									int p = Integer.parseInt(o);
									LadderListener.timestampMax = p;
								}catch(Exception error) {
									error.printStackTrace();
								}
							}
						}catch(Exception errora) {
							errora.printStackTrace();
						}
					}
				});
				BadblockDatabase.getInstance().addSyncRequest(new Request("SELECT `value` FROM keyValues WHERE `key` = 'finishedMessage';", RequestType.GETTER) {
					@Override
					public void done(ResultSet resultSet) {
						try {
							if (resultSet.next()) {
								String o = resultSet.getString("value");
								LadderListener.finished = o;
							}
						}catch(Exception errora) {
							errora.printStackTrace();
						}
					}
				});
			}
		};
		new Timer().schedule(timerTask3, 1000, 20000);
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				BadblockDatabase database = BadblockDatabase.getInstance();
				database.addSyncRequest(new Request("SELECT pseudo FROM friends ORDER BY id DESC LIMIT 10000;", RequestType.GETTER) {
					@Override
					public void done(ResultSet resultSet) {
						try {
							while (resultSet.next()) {
								String o = resultSet.getString("pseudo");
								if (o != null) {
									if (LadderBungee.getInstance() != null && LadderBungee.getInstance().totalPlayers != null) {
										LadderBungee.getInstance().totalPlayers.add(o);
									}
								}
							}
						}catch(Exception error) {
							error.printStackTrace();
						}
					}
				});
			}
		}, 2500);
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				BadblockDatabase database = BadblockDatabase.getInstance();
				for (ProxiedPlayer plo : BungeeCord.getInstance().getPlayers())
					if (plo.isConnected())
						database.addSyncRequest(new Request("UPDATE cheatReports SET lastLogin = '" + (System.currentTimeMillis() + 600000) + "' WHERE pseudo = '" + database.mysql_real_escape_string(plo.getName()) + "' AND timestamp > '" + System.currentTimeMillis() + "'", RequestType.SETTER));
			}
		}, 5000, 580000);
		if (configuration.getBoolean("dns")) {
			timerTask = new TimerTask() {
				@Override
				public void run() {
					double o = ((double) PreLoginListener.getNotLoggedPlayers() / 250.0D) * 100.0D;
					if (done && delete == -1 && BungeeCord.getInstance().getOnlineCount() <= 0) {
						System.out.println("/!\\ BUNGEEDNS!<EVENT-BYEBUNGEE!/" + o + "%/" + LadderBungee.getInstance().bungeePlayerList.size() + "/" + BadBlockBungeeOthers.getInstance().getConnections() + "> /!\\");
						finished = true;
						String a = ProxyServer.getInstance().getConfig().getListeners().iterator().next().getHost().getHostString() + ":" + ProxyServer.getInstance().getConfig().getListeners().iterator().next().getHost().getPort();
						if (BadblockDatabase.getInstance().isConnected())
							BadblockDatabase.getInstance().addSyncRequest(new Request("UPDATE absorbances SET done = 'false', bungeeTimestamp = '0' WHERE ip = '" + a + "'", RequestType.SETTER));
						System.exit(-1);
					}else if (done && delete > 0) {
						System.out.println("/!\\ BUNGEEDNS<WAITING-FOR-DNS-DELETE(" + o + "%/" + delete + ")/" + LadderBungee.getInstance().bungeePlayerList.size() + "/" + BadBlockBungeeOthers.getInstance().getConnections() + "> /!\\");
					}else if (done && deleteTime > System.currentTimeMillis()) {
						System.out.println("/!\\ BUNGEEDNS<WAITING-FOR-DELETE(" + o + "%/" + (deleteTime - System.currentTimeMillis()) + "/" + LadderBungee.getInstance().bungeePlayerList.size() + "/" + BadBlockBungeeOthers.getInstance().getConnections() + "> /!\\");
					}else if (done) {
						System.out.println("/!\\ BUNGEEDNS<DONE-WAIT-FOR-PLAYERS-UNFILL/" + o + "%/" + time + "/" + LadderBungee.getInstance().bungeePlayerList.size() + "/" + BadBlockBungeeOthers.getInstance().getConnections() + "> /!\\");
					}else{
						System.out.println("/!\\ BUNGEEDNS<RUNNING/" + o + "%/" + LadderBungee.getInstance().bungeePlayerList.size() + "/" + PreLoginListener.players.size() + "> /!\\");
					}
					if (done && delete == 0) {
						delete = -1;
						deleteTime = System.currentTimeMillis() + 7200_000L;
						System.out.println("/!\\ BUNGEEDNS!<EVENT-BYEDNS!/" + o + "%/" + LadderBungee.getInstance().bungeePlayerList.size() + "/" + BadBlockBungeeOthers.getInstance().getConnections() + "> /!\\");
					}else if (done && delete > 0) delete--;
					if (done && delete == -1 && deleteTime < System.currentTimeMillis()) {
						time--;
						if (time == 0) {
							BungeeCord.getInstance().broadcast("§f[§cAbsorbance§f] §eDéconnexion de cette absorbance...");
							for (ProxiedPlayer player : BungeeCord.getInstance().getPlayers()) {
								player.disconnect("§cDéconnexion de l'aborsbance. Vous pouvez vous reconnecter au serveur.");
							}
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							finished = true;
							String a = ProxyServer.getInstance().getConfig().getListeners().iterator().next().getHost().getHostString() + ":" + ProxyServer.getInstance().getConfig().getListeners().iterator().next().getHost().getPort();
							if (BadblockDatabase.getInstance().isConnected())
								BadblockDatabase.getInstance().addSyncRequest(new Request("UPDATE absorbances SET done = 'false', bungeeTimestamp = '0' WHERE ip = '" + a + "'", RequestType.SETTER));
							System.exit(-1);
						}else if (time % 60 == 0 && ((time % 900 == 0 && time <= 3600) || (time % 600 == 0 && time <= 1800) || (time % 300 == 0 && time <= 900))) {
							BungeeCord.getInstance().broadcast("§f[§cAbsorbance§f] §eVous allez être déconnecté de cette absorbance dans " +
									(time / 60) + " minute" + (time > 60 ? "s" : "") + ".");
						}else if (time % 60 == 0 && time % 900 == 0 && time <= 3600) {
							BungeeCord.getInstance().broadcast("§f[§cAbsorbance§f] §eVous allez être déconnecté de cette absorbance dans " +
									(time / 60) + " minute" + (time > 60 ? "s" : "") + ".");
						}else if (time > 0 && time < 60 && time % 15 == 0) {
							BungeeCord.getInstance().broadcast("§f[§cAbsorbance§f] §eVous allez être déconnecté de cette absorbance dans " + time + " seconde" + (time > 1 ? "s" : "") + ".");
						} 
					}
				}
			};
			new Timer().schedule(timerTask, 1000L, 1000L);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onDisable() {
		filters.forEach(filter -> filter.reset());
		deleteDNS();
		if (timerTask != null) timerTask.cancel();
		if (timerTask2 != null) timerTask2.cancel();
		if (timerTask3 != null) timerTask3.cancel();
		finished = true;
		String a = ProxyServer.getInstance().getConfig().getListeners().iterator().next().getHost().getHostString() + ":" + ProxyServer.getInstance().getConfig().getListeners().iterator().next().getHost().getPort();
		if (BadblockDatabase.getInstance().isConnected())
			BadblockDatabase.getInstance().addSyncRequest(new Request("UPDATE absorbances SET done = 'false', bungeeTimestamp = '0' WHERE ip = '" + a + "'", RequestType.SETTER));
	}

	public void deleteDNS() {
		if (deleted) return;
		if (access != null) {
			@SuppressWarnings("deprecation")
			String a = ProxyServer.getInstance().getConfig().getListeners().iterator().next().getHost().getHostString() + ":" + ProxyServer.getInstance().getConfig().getListeners().iterator().next().getHost().getPort();
			BadblockDatabase.getInstance().addSyncRequest(new Request("SELECT recordId FROM absorbances WHERE ip = '" + a + "'", RequestType.GETTER) {
				@Override
				public void done(ResultSet resultSet) {
					try {
						if (resultSet.next()) {
							int recordId = resultSet.getInt("recordId");
							if (recordId != 0) {
								System.out.println("Deleting record ID " + recordId + " / " + access);
								DNSDeleteRecord dns = new DNSDeleteRecord(access, "badblock.fr", recordId);
								try {
									JSONObject json = dns.executeBasic();
									StringBuilder stringBuilder = new StringBuilder();
									for (StackTraceElement element : Thread.currentThread().getStackTrace())
										stringBuilder.append(element.toString() + System.lineSeparator());
									if (json != null) {
										BadblockDatabase.getInstance().addSyncRequest(new Request("INSERT INTO history(ip, action, log, date) VALUES('" + a + "', 'Deleted record in cloudflare (DeleteDNS1 / ID " + recordId + ")', '" + json.toString() + System.lineSeparator() + stringBuilder.toString() + "', '" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SS").format(new Date()) + "')", RequestType.SETTER));
										BadblockDatabase.getInstance().addSyncRequest(new Request("UPDATE absorbances SET recordId = '0' WHERE ip = '" + a + "'", RequestType.SETTER));
									}else{
										BadblockDatabase.getInstance().addSyncRequest(new Request("INSERT INTO history(ip, action, log, date) VALUES('" + a + "', 'Fail: Deleted record in cloudflare (DeleteDNS1 / ID " + recordId + ")', 'null" + System.lineSeparator() + stringBuilder.toString() + "', '" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SS").format(new Date()) + "')", RequestType.SETTER));
									}
									System.out.println(json.toString());
									deleted = true;
								} catch (Exception e) {
									e.printStackTrace();
								}
								try {
									dns.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			});
		}else{
			throw new UnableToDeleteDNSException();
		}
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
