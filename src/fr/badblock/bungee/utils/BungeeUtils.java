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

	private int hubMaxPlayers;
	private int loginMaxPlayers;

	@Override
	public void onEnable(){
		instance = this;
		getProxy().getPluginManager().registerListener(this, this);
		getProxy().getPluginManager().registerCommand(this, new HubCommand());
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
		loadConfig();
	}

	public void loadConfig(){
		File f2 = new File(getDataFolder(), "randomhub.yml");
		if(!f2.exists()){
			try {
				f2.createNewFile();
			} catch (IOException e) {
				return;
			}
		}
		hubMaxPlayers = config.getInt("hubMaxPlayers", 200);
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
		if (e.getTarget() == null) {
			if(e.getPlayer().getServer() != null) {
				ServerInfo serverInfo = this.roundrobinHub();
				if (serverInfo != null) e.setTarget(serverInfo);
			}
		} else if(e.getPlayer().getServer() == null){
			if (e.getTarget().getName().startsWith("login")) {
				ServerInfo serverInfo = this.roundrobinLogin();
				if (serverInfo != null) e.setTarget(serverInfo);
			}
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
