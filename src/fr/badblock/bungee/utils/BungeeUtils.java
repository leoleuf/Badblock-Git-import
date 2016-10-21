package fr.badblock.bungee.utils;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.util.internal.ConcurrentSet;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.ProxyReloadEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerConnectionFailEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;

public class BungeeUtils extends Plugin implements Listener{
	public static ConfigurationProvider cp = ConfigurationProvider.getProvider(YamlConfiguration.class);
	public static Configuration config;

	public static BungeeUtils instance;

	private int 		hubMaxPlayers;
	private int 		loginMaxPlayers;
	private ServerInfo	skeleton;
	private ConcurrentHashMap<ServerInfo, Long> lobbies = new ConcurrentHashMap<>();
	private ConcurrentHashMap<ServerInfo, Long> logins = new ConcurrentHashMap<>();

	private Set<String> 		  blacklisted    = new ConcurrentSet<>();
	private Map<UUID, ServerInfo> waitingForfail = new ConcurrentHashMap<>();

	@Override
	public void onEnable(){
		instance = this;
		// Création d'un serveur skeleton
		skeleton = BungeeCord.getInstance().constructServerInfo("skeleton", new InetSocketAddress("127.0.0.1", 8889), "skeleton", false);
		BungeeCord.getInstance().getServers().put("skeleton", skeleton);
		ServerInfo lobbySkeleton = BungeeCord.getInstance().constructServerInfo("lobby", new InetSocketAddress("127.0.0.1", 8890), "lobbySkeleton", false);
		BungeeCord.getInstance().getServers().put("lobby", lobbySkeleton);

		getProxy().getPluginManager().registerListener(this, this);
		getProxy().getPluginManager().registerCommand(this, new HubCommand());
		loadConfig();
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				// hub check
				for (ServerInfo serverInfo : BungeeCord.getInstance().getServers().values()) {
					if (serverInfo == null) continue;
					if (!serverInfo.getName().startsWith("hub")) continue;
					serverInfo.ping(new Callback<ServerPing>() {
						@Override
						public void done(ServerPing arg0, Throwable arg1) {
							if (arg0 != null) lobbies.put(serverInfo, System.currentTimeMillis() + 5_000L);
						}
					});
				}
				// hub check
				for (ServerInfo serverInfo : BungeeCord.getInstance().getServers().values()) {
					if (serverInfo == null) continue;
					if (!serverInfo.getName().startsWith("login")) continue;
					serverInfo.ping(new Callback<ServerPing>() {
						@Override
						public void done(ServerPing arg0, Throwable arg1) {
							if (arg0 != null) logins.put(serverInfo, System.currentTimeMillis() + 5_000L);
						}
					});
				}
			}
		}, 1000, 1000);
	}

	public void loadConfig(){
		File f = new File(getDataFolder(), "config.yml");
		getDataFolder().mkdirs();
		if(!f.exists()){
			try {
				f.createNewFile();
			} catch (IOException e) {
				return;
			}
		}
		try {
			config = cp.load(f);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		hubMaxPlayers = config.getInt("hubMaxPlayers", 100);
		loginMaxPlayers = config.getInt("loginMaxPlayers", 100);
	}

	@EventHandler
	public void onReload(ProxyReloadEvent e){
		loadConfig();
	}

	@EventHandler
	public void onCommand(ChatEvent e){
		if(e.isCommand() && e.getSender() instanceof ProxiedPlayer){
			ProxiedPlayer player = (ProxiedPlayer) e.getSender();
			String command = e.getMessage().toLowerCase().split(" ")[0];
			if (player.getServer().getInfo().getName().startsWith("login") && !command.equals("/login") && !command.equals("/l") && !command.equals("/register") && !command.equals("/r") && !command.equals("/pic") && !command.equals("/glist")){
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onServerConnect(ServerConnectEvent e) {
		if (e.getTarget() != null && e.getTarget().getName().equalsIgnoreCase( skeleton.getName() )) {
			ServerInfo serverInfo = this.roundrobinLogin(e.getPlayer());

			if (serverInfo != null) {
				e.setTarget(serverInfo);
			}
		} else if(e.getTarget() == null || (e.getTarget() != null && e.getTarget().getName().equals("lobby"))) {
			ServerInfo serverInfo = this.roundrobinHub(e.getPlayer());

			if (serverInfo != null) {
				e.setTarget(serverInfo);
			}
		}
	}

	@EventHandler
	public void onServerConnectionFail(ServerConnectionFailEvent e){
		ServerInfo info = waitingForfail.get(e.getPlayer().getUniqueId());

		if(info != null){
			waitingForfail.remove(e.getPlayer().getUniqueId());
			blacklisted.add(info.getName());
			// blacklist server for 30 second when fail
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					blacklisted.remove(info.getName());
				}
			}, 30_000L);
		}
	}

	public ServerInfo roundrobinHub(ProxiedPlayer player) {
		/*ServerInfo result = null;
		for (ServerInfo serverInfo : BungeeCord.getInstance().getServers().values()) {
			if (serverInfo == null) continue;
			if (!serverInfo.getName().startsWith("hub")) continue;
			if (!lobbies.containsKey(serverInfo)) continue;
			if (lobbies.get(serverInfo) < System.currentTimeMillis()) continue;
			if (serverInfo.getPlayers().size() >= hubMaxPlayers) continue;
			if (result == null || (result != null && result.getPlayers().size() > serverInfo.getPlayers().size()))
				result = serverInfo;
		}
		return result;*/
		
		return roundrobin(player, "hub", lobbies, hubMaxPlayers);
	}

	private ServerInfo roundrobinLogin(ProxiedPlayer player) {
		/*ServerInfo result = null;
		for (ServerInfo serverInfo : BungeeCord.getInstance().getServers().values()) {
			if (serverInfo == null) continue;
			if (!serverInfo.getName().startsWith("login")) continue;
			if (!logins.containsKey(serverInfo)) continue;
			if (logins.get(serverInfo) < System.currentTimeMillis()) continue;
			if (serverInfo.getPlayers().size() >= loginMaxPlayers) continue;
			if (result == null || (result != null && result.getPlayers().size() > serverInfo.getPlayers().size()))
				result = serverInfo;
		}
		return result;*/
		return roundrobin(player, "login", logins, loginMaxPlayers);
	}

	public ServerInfo roundrobin(ProxiedPlayer player, String type, Map<ServerInfo, Long> servers, int maxPlayers){
		ServerInfo result = null;

		for (ServerInfo serverInfo : BungeeCord.getInstance().getServers().values()) {
			if (serverInfo == null || serverInfo.getName().startsWith(type)) continue;
			if (!servers.containsKey(serverInfo) || servers.get(serverInfo) < System.currentTimeMillis()) continue;

			if (serverInfo.getPlayers().size() >= maxPlayers) continue;
			if (blacklisted.contains( serverInfo.getName() )) continue;
			
			
			if (result == null || (result != null && result.getPlayers().size() > serverInfo.getPlayers().size()))
				result = serverInfo;
		}

		if(result != null){
			waitingForfail.put(player.getUniqueId(), result);
			
			ServerInfo fresult = result;
			
			// Met le joueur deux secondes dans la liste, ne va pas couvrir les timed out :(
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					if(waitingForfail.get(player.getUniqueId()) == fresult)
						waitingForfail.remove(player.getUniqueId());
				}
			}, 2_000L);
		}
		
		return result;
	}
}
