package fr.badblock.bungee.utils;

import java.io.File;
import java.io.IOException;

import fr.badblock.bungee.utils.commands.HubCommand;
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
		skeleton = BungeeCord.getInstance().getServerInfo("skeleton");
		getProxy().getPluginManager().registerListener(this, this);
		getProxy().getPluginManager().registerCommand(this, new HubCommand());
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
		hubMaxPlayers = config.getInt("hubMaxPlayers", 200);
		loginMaxPlayers = config.getInt("loginMaxPlayers", 200);
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
		System.out.println(e.getTarget() + " / " + (e.getTarget() != null ? e.getTarget().getName() : "null"));
		if (e.getTarget() == skeleton) {
			ServerInfo serverInfo = this.roundrobinLogin();
			System.out.println("roundrobinLogin: " + serverInfo + " / " + (serverInfo != null ? serverInfo.getName() : "null"));
			if (serverInfo != null) e.setTarget(serverInfo);
		} else if(e.getTarget().getName().equals("lobby")){
			ServerInfo serverInfo = this.roundrobinHub();
			System.out.println("roundrobinHub: " + serverInfo + " / " + (serverInfo != null ? serverInfo.getName() : "null"));
			if (serverInfo != null) e.setTarget(serverInfo);
		}
	}

	public ServerInfo roundrobinHub() {
		ServerInfo result = null;
		for (ServerInfo serverInfo : BungeeCord.getInstance().getServers().values()) {
			if (serverInfo == null) continue;
			if (!serverInfo.getName().startsWith("hub")) continue;
			if (serverInfo.getPlayers().size() > hubMaxPlayers) continue;
			if (result == null || (result != null && result.getPlayers().size() < serverInfo.getPlayers().size()))
				result = serverInfo;
		}
		return result;
	}

	private ServerInfo roundrobinLogin() {
		ServerInfo result = null;
		for (ServerInfo serverInfo : BungeeCord.getInstance().getServers().values()) {
			if (serverInfo == null) continue;
			if (!serverInfo.getName().startsWith("login")) continue;
			if (serverInfo.getPlayers().size() > loginMaxPlayers) continue;
			if (result == null || (result != null && result.getPlayers().size() > serverInfo.getPlayers().size()))
				result = serverInfo;
		}
		return result;
	}

}
