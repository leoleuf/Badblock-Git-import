package fr.badblock.bungee.utils;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.ProxyReloadEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
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

	@Override
	public void onEnable(){
		instance = this;
		// Création d'un serveur skeleton
		skeleton = BungeeCord.getInstance().constructServerInfo("skeleton", new InetSocketAddress("127.0.0.1", 8889), "skeleton", false);
		BungeeCord.getInstance().getServers().put("skeleton", skeleton);
		ServerInfo lobbySkeleton = BungeeCord.getInstance().constructServerInfo("lobby", new InetSocketAddress("127.0.0.1", 8890), "lobbySkeleton", false);
		BungeeCord.getInstance().getServers().put("lobby", lobbySkeleton);
		new AdminBroadcastListener();
		getProxy().getPluginManager().registerListener(this, this);
		getProxy().getPluginManager().registerCommand(this, new HubCommand());
		getProxy().getPluginManager().registerCommand(this, new BUReloadCommand());
		getProxy().getPluginManager().registerCommand(this, new LBReloadCommand());
		loadConfig();
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
			ServerInfo serverInfo = this.roundrobinLogin();

			if (serverInfo != null) {
				e.setTarget(serverInfo);
			}
		} else if(e.getTarget() == null || (e.getTarget() != null && e.getTarget().getName().equals("lobby"))) {
			ServerInfo serverInfo = this.roundrobinHub();

			if (serverInfo != null) {
				e.setTarget(serverInfo);
			}
		}
	}

	public ServerInfo roundrobinHub() {
		List<ServerInfo> servers = new ArrayList<>();
		for (ServerInfo serverInfo : BungeeCord.getInstance().getServers().values()) {
			if (serverInfo == null) continue;
			if (!serverInfo.getName().startsWith("hub")) continue;
			//if (!lobbies.containsKey(serverInfo)) continue;
			//if (lobbies.get(serverInfo) < System.currentTimeMillis()) continue;
			if (serverInfo.getPlayers().size() >= hubMaxPlayers) continue;
			servers.add(serverInfo);
		}
		return servers.get(new SecureRandom().nextInt(servers.size()));
	}

	private ServerInfo roundrobinLogin() {
		List<ServerInfo> servers = new ArrayList<>();
		for (ServerInfo serverInfo : BungeeCord.getInstance().getServers().values()) {
			if (serverInfo == null) continue;
			if (!serverInfo.getName().startsWith("login")) continue;
			//if (!logins.containsKey(serverInfo)) continue;
			//if (logins.get(serverInfo) < System.currentTimeMillis()) continue;
			if (serverInfo.getPlayers().size() >= loginMaxPlayers) continue;
			servers.add(serverInfo);
		}
		return servers.get(new SecureRandom().nextInt(servers.size()));
	}

}
