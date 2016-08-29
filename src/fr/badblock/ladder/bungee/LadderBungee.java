package fr.badblock.ladder.bungee;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import fr.badblock.ladder.bungee.entities.CommandDispatcher;
import fr.badblock.ladder.bungee.entities.LadderHandler;
import fr.badblock.ladder.bungee.utils.FileUtils;
import fr.badblock.ladder.bungee.utils.IOUtils;
import fr.badblock.ladder.bungee.utils.Motd;
import fr.badblock.ladder.bungee.utils.Punished;
import fr.badblock.permissions.PermissionManager;
import fr.badblock.protocol.PacketHandler;
import fr.badblock.protocol.packets.PacketHelloworld;
import fr.badblock.protocol.packets.PacketLadderStop;
import fr.badblock.protocol.packets.PacketPlayerChat;
import fr.badblock.protocol.packets.PacketPlayerChat.ChatAction;
import fr.badblock.protocol.packets.PacketPlayerData;
import fr.badblock.protocol.packets.PacketPlayerData.DataAction;
import fr.badblock.protocol.packets.PacketPlayerData.DataType;
import fr.badblock.protocol.packets.PacketPlayerJoin;
import fr.badblock.protocol.packets.PacketPlayerLogin;
import fr.badblock.protocol.packets.PacketPlayerPlace;
import fr.badblock.protocol.packets.PacketPlayerQuit;
import fr.badblock.protocol.packets.PacketReconnectionInvitation;
import fr.badblock.protocol.packets.PacketSimpleCommand;
import fr.badblock.protocol.packets.matchmaking.PacketMatchmakingJoin;
import fr.badblock.protocol.packets.matchmaking.PacketMatchmakingKeepalive;
import fr.badblock.protocol.packets.matchmaking.PacketMatchmakingPing;
import fr.badblock.protocol.packets.matchmaking.PacketMatchmakingPong;
import fr.badblock.protocol.utils.StringUtils;
import fr.badblock.skins.SkinFactoryBungee;
import lombok.Getter;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.AsyncDataLoadRequest.Result;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.chat.ComponentSerializer;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.protocol.packet.Title;
import net.md_5.bungee.protocol.packet.Title.Action;

@SuppressWarnings("deprecation")
public class LadderBungee extends Plugin implements PacketHandler {
	@Getter private static LadderBungee instance;
	@Getter private LadderHandler	    client;

	@Getter private PermissionManager 	permissions;
	@Getter private Motd			  	motd;

	protected Map<UUID, Player>   		players;
	protected Map<String, UUID>   		byName;
	protected Map<String, Punished> 	ips;

	private ConfigurationProvider cp;
	private Configuration config;

	public Punished getIpPunishable(Player player){
		return ips.get(player.getAddress().getAddress().getHostAddress());
	}

	public Player getPlayer(UUID uniqueId){
		return players.get(uniqueId);
	}

	public Player getPlayer(String name){
		UUID uniqueId = byName.get(name.toLowerCase());
		return uniqueId != null ? getPlayer(uniqueId) : null;
	}

	public Collection<Player> getPlayers(){
		return Collections.unmodifiableCollection(players.values());
	}

	@Override
	public void onEnable(){
		instance = this;
		new SkinFactoryBungee();
		
		cp = ConfigurationProvider.getProvider(YamlConfiguration.class);

		try {
			getDataFolder().mkdirs();
			File f = new File(getDataFolder(), "config.yml");
			if(!f.exists())
				f.createNewFile();
			config = cp.load(f);

			players = Maps.newConcurrentMap();
			byName  = Maps.newConcurrentMap();
			ips		= Maps.newConcurrentMap();

			if(config.get("localHost") == null){
				config.set("localHost.ip", "127.0.0.1");
				config.set("localHost.port", 25577);
			}
			if(config.get("ladderHost") == null){
				config.set("ladderHost", "127.0.0.1:26850");
			}

			if(config.get("socketThreads") == null){
				config.set("socketThreads", 4);
			}


			client = new LadderHandler(StringUtils.getAddress(config.getString("ladderHost")), this,
					config.getString("localHost.ip"), config.getInt("localHost.port"));

			registerCommands(new JsonArray());

			client.sendPacket(new PacketHelloworld());

			handle(new PacketHelloworld());
			handle(new PacketPlayerData(DataType.PLAYERS, DataAction.REQUEST, "*", "*"));

			cp.save(config, f);

			getProxy().getPluginManager().registerListener(this, new LadderListener());

			while(true){
				if(getProxy().getServers().size() < 10)
					Thread.sleep(1000L);
				else break;
			}
		} catch(Exception e){
			e.printStackTrace();

			System.out.println("=============== LADDER IS AWAY FROM KEYBOARD #SAD =================\nOn attend 10 secondes et bye bye ...");
			try {
				Thread.sleep(10000L);
			} catch (InterruptedException unused){}
			getProxy().stop();
			return;
		}
	}

	@Override
	public void onDisable(){
		players.clear();
		byName.clear();

		try {
			if(client != null){
				client.sendPacket(new PacketHelloworld());
				client.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void registerCommands(JsonArray array){
		getProxy().getPluginManager().unregisterCommands(this);

		for(int i=0;i<array.size();i++){
			JsonObject object = array.get(i).getAsJsonObject();
			getProxy().getPluginManager().registerCommand(this, new CommandDispatcher(
					object.get("name").getAsString(),
					object.get("bypass").getAsBoolean()
			));
		}
	}

	@Override
	public void handle(PacketPlayerChat chat) {
		if(chat.getUser() != null){
			Player player = players.get(chat.getUser());

			if(player != null)
				player.handle(chat);
		} else if(chat.getType() == ChatAction.MESSAGE_FLAT){
			for(String message : chat.getMessages()){
				getProxy().broadcast(message);
			}
		} else if(chat.getType() == ChatAction.MESSAGE_JSON){
			try {
				BaseComponent[] message = ComponentSerializer.parse(chat.getMessages()[0].toString());
				getProxy().broadcast(message);
			} catch(Exception e){
				e.printStackTrace();
			}
		} else if(chat.getType() == ChatAction.ACTION_BAR){
			Title title = new Title();
			title.setAction(Action.TIMES);
			title.setFadeIn(chat.getFadeIn());
			title.setStay(chat.getStay());
			title.setFadeOut(chat.getFadeOut());

			for(ProxiedPlayer player : getProxy().getPlayers()){
				player.unsafe().sendPacket(title);
				player.sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(chat.getMessages()[0]));
			}
		} else if(chat.getType() == ChatAction.COMMAND){
			for(String command : chat.getMessages())
				BungeeCord.getInstance().getPluginManager().dispatchCommand(getProxy().getConsole(), command);
		} else if(chat.getType() == ChatAction.TABLIST){
			for(ProxiedPlayer player : getProxy().getPlayers())
				player.setTabHeader(TextComponent.fromLegacyText(chat.getMessages()[0]), TextComponent.fromLegacyText(chat.getMessages()[1]));
		} else if(chat.getType() == ChatAction.TITLE){
			for(ProxiedPlayer player : getProxy().getPlayers())
				player.sendTitle(BungeeCord.getInstance().createTitle().fadeIn(chat.getFadeIn())
						.fadeOut(chat.getFadeOut())
						.stay(chat.getStay())
						.title(TextComponent.fromLegacyText(chat.getMessages()[0]))
						.subTitle(TextComponent.fromLegacyText(chat.getMessages()[1])));
		}
	}

	@Override
	public void handle(PacketPlayerData packet){
		if(packet.getType() == DataType.PLAYER){
			if(packet.getAction() == DataAction.SEND){
				Player player = getPlayer(packet.getKey());
				
				if(player == null)
					player = playersTemp.get(packet.getKey().toLowerCase());
				
				if(player != null) player.receiveData(packet.getData());
			} else if(packet.getAction() == DataAction.MODIFICATION){
				Player player = getPlayer(packet.getKey());
				if(player != null) player.updateData(packet.getData());
			}
		} else if(packet.getType() == DataType.PERMISSION && packet.getAction() == DataAction.SEND){
			try {
				System.out.println("Unknown permissionsManager? set it from packet data > " + packet.getData());
				IOUtils.save(packet.getData(), new File("permissions.json"));
				permissions = new PermissionManager(FileUtils.loadArray(new File("permissions.json")));
			} catch (IOException e){}
		} else if(packet.getType() == DataType.MOTD && packet.getAction() == DataAction.SEND){
			motd = BungeeCord.getInstance().gson.fromJson(packet.getData(), Motd.class);
		} else if(packet.getType() == DataType.PLAYERS && packet.getAction() == DataAction.REQUEST){
			for(ProxiedPlayer player : getProxy().getPlayers()){
				PacketPlayerJoin  join = new PacketPlayerJoin(player.getName(), player.getUniqueId(), player.getAddress());

				handle(join);

				getClient().sendPacket(join);

				if(player.getServer() != null)
					getClient().sendPacket(new PacketPlayerPlace(player.getUniqueId(), player.getServer().getInfo().getName()));
			}
		} else if(packet.getType() == DataType.SERVERS){
			if(packet.getAction() == DataAction.SEND){
				List<String> servers 				  = new ArrayList<>();
				JsonArray array 					  = new JsonParser().parse(packet.getData()).getAsJsonArray();
				Map<String, ServerInfo> serversBungee = ProxyServer.getInstance().getConfig().getServers();

				for(int i=0;i<array.size();i++){
					JsonObject object 		  = array.get(i).getAsJsonObject();

					String name 			  = object.get("name").getAsString().toLowerCase();
					InetSocketAddress address = new InetSocketAddress(object.get("ip").getAsString(), object.get("port").getAsInt());

					servers.add(name);

					ServerInfo info = getProxy().getServerInfo(name);
					if(info != null && !info.getAddress().equals(address)){
						if(info.getPlayers().size() > 0){
							for(ProxiedPlayer player : info.getPlayers()){
								player.disconnect(ChatColor.RED + "Votre serveur a été supprimé !");
							}
						}

						serversBungee.remove(name);
					} else if(info != null && info.getAddress().equals(address)){
						continue;
					}

					ServerInfo server = getProxy().constructServerInfo(name, address, "Serveur BadBlock", false);
					serversBungee.put(name, server);
				}

				List<String> toRemove = new ArrayList<>();

				for(ServerInfo server : serversBungee.values()){
					if(servers.contains(server.getName().toLowerCase()))
						continue;
					if(server.getPlayers().size() > 0){
						for(ProxiedPlayer player : server.getPlayers()){
							player.disconnect(ChatColor.RED + "Votre serveur a été supprimé !");
						}
					}

					toRemove.add(server.getName().toLowerCase());
				}

				for(String remove : toRemove){
					serversBungee.remove(remove);
				}
			} else if(packet.getAction() == DataAction.MODIFICATION){
				if(packet.getKey().equalsIgnoreCase("add")){
					JsonObject object 					  = new JsonParser().parse(packet.getData()).getAsJsonObject();
					Map<String, ServerInfo> serversBungee = ProxyServer.getInstance().getServers();

					String name 			  = object.get("name").getAsString().toLowerCase();
					InetSocketAddress address = new InetSocketAddress(object.get("ip").getAsString(), object.get("port").getAsInt());

					ServerInfo info = getProxy().getServerInfo(name);
					if(info != null && !info.getAddress().equals(address)){
						if(info.getPlayers().size() > 0){
							for(ProxiedPlayer player : info.getPlayers()){
								player.disconnect(ChatColor.RED + "Votre serveur a été supprimé !");
							}
						}

						serversBungee.remove(name);
					} else if(info != null && info.getAddress().equals(address)){
						return;
					}

					ServerInfo server = getProxy().constructServerInfo(name, address, "Serveur BadBlock", false);
					serversBungee.put(name, server);
				} else if(packet.getKey().equalsIgnoreCase("remove")){
					JsonArray array 					  = new JsonParser().parse(packet.getData()).getAsJsonArray();
					Map<String, ServerInfo> serversBungee = ProxyServer.getInstance().getServers();

					for(int i=0;i<array.size();i++){
						String name     = array.get(i).getAsString().toLowerCase();
						ServerInfo info = getProxy().getServerInfo(name);

						if(info == null) continue;

						if(info.getPlayers().size() > 0){
							for(ProxiedPlayer player : info.getPlayers()){
								player.disconnect(ChatColor.RED + "Votre serveur a été supprimé !");
							}
						}

						serversBungee.remove(name);
					}
				}
			}
		} else if(packet.getType() == DataType.COMMANDS && packet.getAction() == DataAction.SEND){
			registerCommands(new JsonParser().parse(packet.getData()).getAsJsonArray());
		} else if(packet.getType() == DataType.IP && packet.getAction() == DataAction.SEND){
			ips.put(packet.getKey(), Punished.fromJson(new JsonParser().parse(packet.getData()).getAsJsonObject()));
		}
	}

	@Override
	public void handle(PacketPlayerJoin packet) {
		handle(packet, false);
	}
	
	public void handle(PacketPlayerJoin packet, boolean falsePacket) {
		if(players.containsKey(packet.getUniqueId()))
			return;
		
		Player player = null;
		
		if(falsePacket && playersTemp.containsKey(packet.getPlayerName().toLowerCase())){
			player = playersTemp.remove(packet.getPlayerName().toLowerCase());
			player.update(packet);
		} else player = new Player(packet);
		
		players.put(player.getUniqueId(), player);
		byName.put(player.getName().toLowerCase(), player.getUniqueId());
	}
	
	private Map<String, Player> playersTemp = Maps.newConcurrentMap();
	
	public void handle(PacketPlayerLogin packet, Callback<Result> done) {
		if(byName.containsKey(packet.getPlayerName()))
			return;

		playersTemp.put(packet.getPlayerName().toLowerCase(), new Player(packet, done));
	}

	@Override
	public void handle(PacketPlayerQuit packet) {
		handle(packet, false);
	}
	
	public void handle(PacketPlayerQuit packet, boolean kick) {
		if(!byName.containsKey(packet.getUserName().toLowerCase()) && !playersTemp.containsKey(packet.getUserName().toLowerCase()))
			return;
		Player		  lPlayer = getPlayer(packet.getUserName());
		ProxiedPlayer bPlayer = getProxy().getPlayer(packet.getUserName());

		if(lPlayer == null){
			lPlayer = playersTemp.get(packet.getUserName().toLowerCase());
		}
		
		if(lPlayer.getDone() != null){
			Callback<Result> res = lPlayer.getDone();
			lPlayer.setDone(null);
			res.done(new Result(null, packet.getReason() == null ? "" : StringUtils.join(packet.getReason(), "\\n")), null);
		}
		
		if(bPlayer != null && !kick){
			players.remove(bPlayer.getUniqueId());

			if(packet.getReason() != null){
				bPlayer.disconnect(StringUtils.join(packet.getReason(), "\\n"));
			} else {
				bPlayer.disconnect();
			}
		} else if(lPlayer.getUniqueId() != null)
			players.remove(lPlayer.getUniqueId());

		byName.remove(lPlayer.getName().toLowerCase());
		playersTemp.remove(lPlayer.getName().toLowerCase());
	}

	@Override
	public void handle(PacketPlayerPlace packet) {
		if(!players.containsKey(packet.getUniqueId()))
			return;

		ProxiedPlayer bPlayer = getProxy().getPlayer(packet.getUniqueId());
		ServerInfo    server  = getProxy().getServerInfo(packet.getServerName());
		Player        lPlayer = players.get(packet.getUniqueId());

		if(server == null) return;

		if(bPlayer != null){
			bPlayer.connect(server);
		} else {
			lPlayer.setServer(server);	
		}
	}

	@Override
	public void handle(PacketHelloworld packet) {
		players.clear();

		System.out.println("Send * permission packet data (DataAction.REQUEST - handle(PacketHelloworld)");
		getClient().sendPacket(new PacketPlayerData(DataType.PERMISSION, DataAction.REQUEST, "*", "*"));
		getClient().sendPacket(new PacketPlayerData(DataType.MOTD, DataAction.REQUEST, "*", "*"));
		getClient().sendPacket(new PacketPlayerData(DataType.COMMANDS, DataAction.REQUEST, "*", "*"));
		getClient().sendPacket(new PacketPlayerData(DataType.SERVERS, DataAction.REQUEST, "*", "*"));
		getClient().sendPacket(new PacketPlayerData(DataType.PLAYERS, DataAction.REQUEST, "*", "*"));
	}

	@Override public void handle(PacketMatchmakingJoin packet){}
	@Override public void handle(PacketMatchmakingKeepalive packet){}
	@Override public void handle(PacketMatchmakingPing packet){}
	@Override public void handle(PacketMatchmakingPong packet){}

	@Override
	public void handle(PacketLadderStop packet) {
		try {
			Thread.sleep(10000L);
			BungeeCord.getInstance().stop();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override public void handle(PacketPlayerLogin packet){}
	@Override public void handle(PacketReconnectionInvitation packet){}
	@Override public void handle(PacketSimpleCommand packet){}
	
}
