package fr.badblock.ladder.api;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import com.google.common.collect.MapMaker;
import com.google.common.collect.Maps;
import com.google.gson.Gson;

import fr.badblock.ladder.api.chat.ActionBar;
import fr.badblock.ladder.api.chat.Motd;
import fr.badblock.ladder.api.chat.RawMessage;
import fr.badblock.ladder.api.chat.Title;
import fr.badblock.ladder.api.config.ConfigurationProvider;
import fr.badblock.ladder.api.entities.Bukkit;
import fr.badblock.ladder.api.entities.BungeeCord;
import fr.badblock.ladder.api.entities.CommandSender;
import fr.badblock.ladder.api.entities.OfflinePlayer;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.ladder.api.entities.PlayerIp;
import fr.badblock.ladder.api.plugins.PluginsManager;
import fr.badblock.permissions.PermissionManager;
import fr.badblock.protocol.packets.Packet;
import lombok.Getter;
import lombok.Setter;

public abstract class Ladder {
	@Getter protected static Ladder 			   instance;
	
	@Getter
	protected final String						   version;
	
	protected final Map<UUID, Player> 	    	   players;
	protected final Map<String, UUID>			   names;
	protected final Map<String, BungeeCord> 	   bungeeCords;
	protected final Map<String, Bukkit>			   bukkits;
	protected final Map<InetSocketAddress, String> bukkitsAddress;
	protected final Map<InetSocketAddress, String> bungeesAddress;
	
	@Getter
	protected final Logger						   logger;
	
	@Getter
	protected final PluginsManager				   pluginsManager;
	@Getter
	protected final	CommandSender				   consoleCommandSender;
	@Getter
	protected final ConfigurationProvider   	   configurationProvider;
	@Getter@Setter
	protected int maxPlayers;
	
	
	public Ladder(String version, Logger logger, CommandSender consoleCommandSender, ConfigurationProvider configurationProvider) throws IOException {
		instance 				   = this;
		
		this.version        	   = version;
		
		this.players 	    	   = new MapMaker().makeMap();
		this.names          	   = new MapMaker().makeMap();
		this.bungeeCords    	   = Maps.newConcurrentMap();
		this.bukkits        	   = Maps.newConcurrentMap();
		this.bukkitsAddress        = Maps.newConcurrentMap();
		this.bungeesAddress        = Maps.newConcurrentMap();
	
		this.logger		    	   = logger;
		this.consoleCommandSender  = consoleCommandSender;
		this.configurationProvider = configurationProvider;
		
		readConfiguration();
		
		this.pluginsManager 	   = new PluginsManager();
				
		enablePlugins();
	}
	
	public abstract OfflinePlayer	   	   getOfflinePlayer(String name);
	public abstract PlayerIp			   getIpData(InetAddress address);	

	public Player getPlayer(String name){
		name = name.toLowerCase();
		
		if(names.containsKey(name))
			 return getPlayer(names.get(name));
		else return null;
	}
	
	public Player getPlayer(UUID uniqueId){
		return players.get(uniqueId);
	}
	
	
	public Collection<Player> getOnlinePlayers(){
		return Collections.unmodifiableCollection(players.values());
	}

	public BungeeCord getServer(InetSocketAddress address){
		String name = bungeesAddress.get(address);
		if(name != null)
			return getServer(name);
		else return null;
	}
	
	public BungeeCord getServer(String name){
		name = name.toLowerCase();
		return bungeeCords.get(name);
	}
	
	public Collection<BungeeCord> getServers(){
		return Collections.unmodifiableCollection(bungeeCords.values());
	}
	
	public Bukkit getBukkitServer(InetSocketAddress address){
		String name = bukkitsAddress.get(address);
		
		if(name != null)
			return getBukkitServer(name);
		else return null;
	}
	
	public Bukkit getBukkitServer(String name){
		name = name.toLowerCase();
		return bukkits.get(name);
	}
	
	public void removeBukkitServer(Bukkit bukkit){
		bukkits.remove(bukkit.getName().toLowerCase());
		bukkitsAddress.remove(bukkit.getAddress());
		
		for(BungeeCord bungee : bungeeCords.values()){
			bungee.removeBukkitServer(bukkit);
		}
	}
	
	public abstract boolean addBukkitServer(InetSocketAddress address, String name);
	
	public Collection<Bukkit> getBukkitServers(){
		return Collections.unmodifiableCollection(bukkits.values());
	}
	
	public    abstract Gson				   getGson();
	public    abstract PermissionManager   getPermissions();
	public    abstract void 			   stopLadder();
	protected abstract void				   enablePlugins();
	protected abstract void				   readConfiguration() throws IOException;
	
	public abstract Motd				   getMotd();
	
	public abstract void				   broadcastPacket(Packet packet);
	public abstract void 				   broadcast(String... messages);
	
	public abstract Title      		       createTitle(String title, String subtitle);
	public abstract ActionBar  		   	   createActionBar(String base);
	public abstract RawMessage 		       createRawMessage(String base);
}
