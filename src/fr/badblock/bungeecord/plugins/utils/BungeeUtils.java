package fr.badblock.bungeecord.plugins.utils;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import com.google.common.collect.Queues;

import fr.badblock.bungeecord.BungeeCord;
import fr.badblock.bungeecord.api.config.ServerInfo;
import fr.badblock.bungeecord.api.connection.ProxiedPlayer;
import fr.badblock.bungeecord.api.event.ChatEvent;
import fr.badblock.bungeecord.api.event.ProxyReloadEvent;
import fr.badblock.bungeecord.api.event.ServerConnectEvent;
import fr.badblock.bungeecord.api.plugin.Listener;
import fr.badblock.bungeecord.api.plugin.Plugin;
import fr.badblock.bungeecord.config.Configuration;
import fr.badblock.bungeecord.config.ConfigurationProvider;
import fr.badblock.bungeecord.config.YamlConfiguration;
import fr.badblock.bungeecord.event.EventHandler;
import fr.badblock.bungeecord.plugins.ladder.LadderBungee;
import fr.badblock.bungeecord.plugins.ladder.listeners.ScalerPlayersUpdateListener;

public class BungeeUtils extends Plugin implements Listener{
	public static ConfigurationProvider cp = ConfigurationProvider.getProvider(YamlConfiguration.class);
	public static Configuration config;

	public static BungeeUtils instance;

	private int 			hubMaxPlayers;
	private int 			loginMaxPlayers;
	private ServerInfo		skeleton;
	private Queue<String>	hubs;

	@Override
	public void onEnable(){
		instance = this;
		hubs = Queues.newLinkedBlockingDeque();
		// Création d'un serveur skeleton
		skeleton = BungeeCord.getInstance().constructServerInfo("skeleton", new InetSocketAddress("127.0.0.1", 8889), "skeleton", false);
		BungeeCord.getInstance().getServers().put("skeleton", skeleton);
		ServerInfo lobbySkeleton = BungeeCord.getInstance().constructServerInfo("lobby", new InetSocketAddress("127.0.0.1", 8890), "lobbySkeleton", false);
		BungeeCord.getInstance().getServers().put("lobby", lobbySkeleton);
		StringBuilder stringBuilder = new StringBuilder();
		this.getProxy().getServers().values().forEach(server -> stringBuilder.append("addserver " + server.getName() + " " + server.getAddress().getHostString() + ":" + server.getAddress().getPort() + System.lineSeparator()));
		System.out.println(stringBuilder);
		getProxy().getPluginManager().registerListener(this, this);
		getProxy().getPluginManager().registerCommand(this, new GSCommand());
		getProxy().getPluginManager().registerCommand(this, new GPlayerCommand());
		getProxy().getPluginManager().registerCommand(this, new HubCommand());
		getProxy().getPluginManager().registerCommand(this, new AddBServerCommand());
		getProxy().getPluginManager().registerCommand(this, new AddPlayerCommand());
		getProxy().getPluginManager().registerCommand(this, new BUReloadCommand());
		getProxy().getPluginManager().registerCommand(this, new LBReloadCommand());
		loadConfig();
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				BungeeCord.getInstance().getServers().keySet().stream().filter(name -> name.startsWith("hub_") && !hubs.contains(name)).forEach(name -> hubs.add(name));
				hubs.stream().filter(name -> BungeeCord.getInstance().getServerInfo(name) == null).forEach(name -> hubs.remove(name));
				LadderBungee ladderBungee = LadderBungee.getInstance();
				while (ladderBungee.connectPlayers.size() < ladderBungee.ladderPlayers) {
					if (ladderBungee.connectPlayers.size() < ladderBungee.ladderPlayers) {
						for (String totalPlayer : ladderBungee.totalPlayers)
							if (!ladderBungee.connectPlayers.contains(totalPlayer) && ladderBungee.connectPlayers.size() < ladderBungee.ladderPlayers)
								ladderBungee.connectPlayers.add(totalPlayer);
					}
					break;
				}
				BungeeCord.getInstance().setCurrentCount(ScalerPlayersUpdateListener.get());
				if (ladderBungee.bungeePlayerList.size() < ladderBungee.bungeePlayerCount) {
					while (ladderBungee.bungeePlayerList.size() < ladderBungee.bungeePlayerCount) {
						if (ladderBungee.bungeePlayerList.size() < ladderBungee.bungeePlayerCount) {
							for (String totalPlayer : ladderBungee.totalPlayers)
								if (!ladderBungee.bungeePlayerList.contains(totalPlayer) && ladderBungee.bungeePlayerList.size() < ladderBungee.bungeePlayerCount) 
									ladderBungee.bungeePlayerList.add(totalPlayer);
						}
						break;
					}
				}
				if (ladderBungee.connectPlayers.size() > ladderBungee.ladderPlayers) {
					for (String totalPlayer : ladderBungee.totalPlayers)
						if (ladderBungee.connectPlayers.contains(totalPlayer) && ladderBungee.connectPlayers.size() > ladderBungee.ladderPlayers) {
							ladderBungee.connectPlayers.remove(totalPlayer);
						}
				}
				if (ladderBungee.bungeePlayerList.size() > ladderBungee.bungeePlayerCount) {
					for (String totalPlayer : ladderBungee.totalPlayers)
						if (ladderBungee.bungeePlayerList.contains(totalPlayer) && ladderBungee.bungeePlayerList.size() > ladderBungee.bungeePlayerCount) {
							ladderBungee.bungeePlayerList.remove(totalPlayer);
						}
				}
			}
		}, 100, 100);
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
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onServerConnect(ServerConnectEvent e) {
		if (e.getTarget() != null && e.getTarget().getName().equalsIgnoreCase( skeleton.getName() )) {
			ServerInfo serverInfo = this.roundrobinLogin();
			if (serverInfo != null) {
				e.setTarget(serverInfo);
			}else{
				if (e.getPlayer().getServer() != null && e.getPlayer().getServer().getInfo() != null && !e.getPlayer().getServer().getInfo().getName().equals("skeleton")) {
					e.setCancelled(true);
					e.getPlayer().sendMessage("§cAucun serveur de connexion disponible pour vous téléporter, veuillez patienter, nous recalculons votre itinéraire...");
				}else e.getPlayer().disconnect("§cAucun serveur de connexion disponible pour vous téléporter, veuillez réitérer.");
			}
		} else if(e.getTarget() == null || (e.getTarget() != null && e.getTarget().getName().equals("lobby"))) {
			ServerInfo serverInfo = this.roundrobinHub();

			if (serverInfo != null) {
				e.setTarget(serverInfo);
			}else{
				if (e.getPlayer().getServer() != null && e.getPlayer().getServer().getInfo() != null && !e.getPlayer().getServer().getInfo().getName().equals("lobby")) {
					e.setCancelled(true);
					e.getPlayer().sendMessage("§cAucun hub disponible pour vous téléporter, veuillez patienter, nous recalculons votre itinéraire...");
				}else e.getPlayer().disconnect("§cAucun hub disponible pour vous téléporter, veuillez réitérer.");
			}
		}
	}
	
	public ServerInfo roundrobinHubQueue() {
		if (hubs.isEmpty()) return null;
		boolean okay = false;
		int queueSize = hubs.size();
		int i = 0;
		while (!okay) {
			i++;
			if (i > queueSize) {
				return null;
			}
			String name = hubs.peek();
			ServerInfo serverInfo = BungeeCord.getInstance().getServerInfo(name);
			if (serverInfo == null) {
				hubs.remove(name);
				continue;
			}
			if (!serverInfo.getName().startsWith("hub")) {
				hubs.remove(name);
				continue;
			}
			if (serverInfo.getPlayers().size() >= hubMaxPlayers - (hubMaxPlayers / 10)) {
				continue;
			}
			hubs.remove(name);
			hubs.add(name);
			okay = true;
			return serverInfo;
		}
		return null;
	}

	public ServerInfo roundrobinHub_() {
		ServerInfo server = null;
		for (ServerInfo serverInfo : BungeeCord.getInstance().getServers().values()) {
			if (serverInfo == null) continue;
			if (!serverInfo.getName().startsWith("hub")) continue;
			//if (!lobbies.containsKey(serverInfo)) continue;
			//if (lobbies.get(serverInfo) < System.currentTimeMillis()) continue;
			if (serverInfo.getPlayers().size() >= hubMaxPlayers - (hubMaxPlayers / 10)) continue;
			if (server == null || server.getPlayers().size() < serverInfo.getPlayers().size() || server.getPlayers().size() >= hubMaxPlayers - (hubMaxPlayers / 10))
				server = serverInfo;
		}
		return server;
	}

	private ServerInfo roundrobinLogin() {
		List<ServerInfo> servers = new ArrayList<>();
		for (ServerInfo serverInfo : BungeeCord.getInstance().getServers().values()) {
			if (serverInfo == null) continue;
			if (!serverInfo.getName().startsWith("login")) continue;
			//if (!logins.containsKey(serverInfo)) continue;
			//if (logins.get(serverInfo) < System.currentTimeMillis()) continue;
			if (serverInfo.getPlayers().size() >= loginMaxPlayers - (loginMaxPlayers / 10)) continue;
			servers.add(serverInfo);
		}
		if (servers == null || servers.isEmpty()) return null;
		return servers.get(new SecureRandom().nextInt(servers.size()));
	}

}
