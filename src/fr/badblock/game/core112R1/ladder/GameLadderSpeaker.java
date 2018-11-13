

package fr.badblock.game.core112R1.ladder;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.common.collect.Queues;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import fr.badblock.game.core112R1.players.GameBadblockPlayer;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.databases.LadderSpeaker;
import fr.badblock.gameapi.events.api.PlayerDataChangedEvent;
import fr.badblock.gameapi.game.GameState;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.run.RunType;
import fr.badblock.gameapi.utils.general.Callback;
import fr.badblock.permissions.PermissionManager;
import fr.badblock.protocol.PacketHandler;
import fr.badblock.protocol.Protocol;
import fr.badblock.protocol.packets.Packet;
import fr.badblock.protocol.packets.PacketHelloworld;
import fr.badblock.protocol.packets.PacketLadderStop;
//import fr.badblock.protocol.packets.PacketLadderStop;
import fr.badblock.protocol.packets.PacketPlayerChat;
import fr.badblock.protocol.packets.PacketPlayerChat.ChatAction;
import fr.badblock.protocol.packets.PacketPlayerData;
import fr.badblock.protocol.packets.PacketPlayerData.DataAction;
import fr.badblock.protocol.packets.PacketPlayerData.DataType;
import fr.badblock.protocol.packets.PacketPlayerJoin;
import fr.badblock.protocol.packets.PacketPlayerLogin;
import fr.badblock.protocol.packets.PacketPlayerNickSet;
//import fr.badblock.protocol.packets.PacketPlayerLogin;
import fr.badblock.protocol.packets.PacketPlayerPlace;
import fr.badblock.protocol.packets.PacketPlayerQuit;
import fr.badblock.protocol.packets.PacketReconnectionInvitation;
import fr.badblock.protocol.packets.PacketSimpleCommand;
import fr.badblock.protocol.packets.matchmaking.PacketMatchmakingJoin;
import fr.badblock.protocol.packets.matchmaking.PacketMatchmakingKeepalive;
import fr.badblock.protocol.packets.matchmaking.PacketMatchmakingKeepalive.ServerStatus;
import fr.badblock.protocol.packets.matchmaking.PacketMatchmakingPing;
import fr.badblock.protocol.packets.matchmaking.PacketMatchmakingPong;
import fr.badblock.protocol.socket.SocketHandler;
import fr.badblock.utils.CommonFilter;

public class GameLadderSpeaker implements LadderSpeaker, PacketHandler {
	private final Map<String,  Callback<JsonObject>> requestedPlayers = new HashMap<>();
	private final Map<String,  Callback<JsonObject>> requestedIps     = new HashMap<>();
	private final Map<Integer,  Callback<Integer>> 	 requestedPing    = new HashMap<>();

	private SocketHandler					  		 socketHandler;
	private String 									 ip;
	private int    									 port;
	private int										 nextKey		  = 0;

	private boolean trying   = false;

	private Queue<Packet>							 waitingPackets;

	public GameLadderSpeaker(String ip, int port) throws IOException {
		this.ip 		   = ip;
		this.port		   = port;

		Socket socket 	   = new Socket(ip, port);
		this.socketHandler = new SocketHandler(socket, Protocol.LADDER_TO_BUKKIT, Protocol.BUKKIT_TO_LADDER, this, false);

		this.socketHandler.start();

		ip = Bukkit.getIp();
		String fileName = "server.properties";
		List<String> lines = null;
		try {
			lines = Files.readAllLines(Paths.get(fileName), Charset.defaultCharset());
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (String line : lines) {
			if (line.startsWith("docker-ip=")) {
				ip = line.replace("docker-ip=", "");
			}
		}

		socketHandler.getOut().writeUTF(ip);
		this.socketHandler.getOut().writeInt(Bukkit.getPort());

		//System.out.println("new GameLadderSpeaker(" + ip + ", " + Bukkit.getPort() + ")");

		this.waitingPackets = Queues.newLinkedBlockingDeque();
	}

	public void sendPacket(Packet packet){
		if(socketHandler == null || socketHandler.getSocket().isClosed()){
			if(trying){
				if(packet instanceof PacketPlayerData)
					waitingPackets.add(packet);
				return;
			}

			trying = true;
			new Thread("BadBlockAPI/gameLadderSpeaker") {
				@Override
				public void run(){
					try {
						Socket socket = new Socket(ip, port);
						socketHandler = new SocketHandler(socket, Protocol.LADDER_TO_BUKKIT, Protocol.BUKKIT_TO_LADDER, GameLadderSpeaker.this, false);

						socketHandler.start();

						socketHandler.getOut().writeUTF(Bukkit.getIp());
						socketHandler.getOut().writeInt(Bukkit.getPort());

						socketHandler.sendPacket(packet);

						Packet p = null;

						while((p = waitingPackets.poll()) != null){
							socketHandler.sendPacket(p);
						}
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							Thread.sleep(15_000L);
						} catch (InterruptedException unused){}

						trying = false;
					}
				}
			}.start();
		} else socketHandler.sendPacket(packet);
	}

	@Override
	public void getPlayerData(BadblockPlayer player, Callback<JsonObject> callback) {
		getPlayerData(player.getName().toLowerCase(), callback);
	}

	@Override
	public void getPlayerData(String player, Callback<JsonObject> callback) {
		player = CommonFilter.reverseFilterNames(player);
		requestedPlayers.put(player.toLowerCase(), callback);
		player = player.toLowerCase();
		sendPacket(new PacketPlayerData(DataType.PLAYER, DataAction.REQUEST, player.toLowerCase(), "*"));
	}

	@Override
	public void updatePlayerData(BadblockPlayer player, JsonObject toUpdate) {
		//System.getSecurityManager().checkPermission(new RuntimePermission("badblockDatabase"));

		if(GameAPI.getAPI().getRunType() == RunType.DEV) return;
		if (!player.isDataFetch()) return;
		updatePlayerData(player.getName().toLowerCase(), toUpdate);
	}

	@Override
	public void updatePlayerData(String player, JsonObject toUpdate) {
		//System.getSecurityManager().checkPermission(new RuntimePermission("badblockDatabase"));
		//System.out.println("updatePlayerData / " + player + " / " + toUpdate.toString());
		//for (StackTraceElement stackTrace : Thread.currentThread().getStackTrace())
		//	System.out.println(stackTrace.toString());
		if(GameAPI.getAPI().getRunType() == RunType.DEV) return;
		if(toUpdate != null)
		{
			player = player.toLowerCase();
			sendPacket(new PacketPlayerData(DataType.PLAYER, DataAction.MODIFICATION, player.toLowerCase(), toUpdate.toString()));
		}
	}

	@Override
	public void getIpPlayerData(BadblockPlayer player, Callback<JsonObject> callback) {
		//System.getSecurityManager().checkPermission(new RuntimePermission("badblockDatabase"));

		String ip = player.getAddress().getAddress().getHostAddress();

		requestedIps.put(ip, callback);
		sendPacket(new PacketPlayerData(DataType.IP, DataAction.REQUEST, ip.toLowerCase(), "*"));
	}

	@Override
	public void updateIpPlayerData(BadblockPlayer player, JsonObject toUpdate) {
		//System.getSecurityManager().checkPermission(new RuntimePermission("badblockDatabase"));

		if(GameAPI.getAPI().getRunType() == RunType.DEV) return;
		if (!player.isDataFetch()) return;
		String ip = player.getAddress().getAddress().getHostAddress();
		if(toUpdate != null)
			sendPacket(new PacketPlayerData(DataType.IP, DataAction.MODIFICATION, ip.toLowerCase(), toUpdate.toString()));
	}

	@Override
	public void askForPermissions() {
//		System.getSecurityManager().checkPermission(new RuntimePermission("badblockDatabase"));

		sendPacket(new PacketPlayerData(DataType.PERMISSION, DataAction.REQUEST, "*", ""));
	}

	@Override
	public void keepAlive(GameState state, int current, int max){
		//		System.getSecurityManager().checkPermission(new RuntimePermission("badblockDatabase"));

		sendPacket(new PacketMatchmakingKeepalive(ServerStatus.getStatus(state.getId()), current, max));
	}

	@Override
	public void sendPing(String[] servers, Callback<Integer> count){
		//		System.getSecurityManager().checkPermission(new RuntimePermission("badblockDatabase"));

		requestedPing.put(nextKey, count);
		sendPacket(new PacketMatchmakingPing(nextKey, servers));
		nextKey++;
	}

	@Override
	public void sendReconnectionInvitation(String name, boolean invited) {
		//		System.getSecurityManager().checkPermission(new RuntimePermission("badblockDatabase"));

		if(GameAPI.getAPI().getRunType() == RunType.DEV) return;
		sendPacket(new PacketReconnectionInvitation(name, invited));
	}

	@Override
	public void executeCommand(String command) {
		//		System.getSecurityManager().checkPermission(new RuntimePermission("badblockDatabase"));

		if(GameAPI.getAPI().getRunType() == RunType.DEV) return;
		sendPacket(new PacketSimpleCommand(command));
	}

	@Override
	public void executeCommand(BadblockPlayer player, String command) {
		//	System.getSecurityManager().checkPermission(new RuntimePermission("badblockDatabase"));

		if(GameAPI.getAPI().getRunType() == RunType.DEV) return;
		sendPacket(new PacketSimpleCommand(player.getName(), command));
	}

	@Override
	public void handle(PacketPlayerData packet) {
		if(packet.getType() == DataType.PLAYER && packet.getAction() == DataAction.SEND){
			Callback<JsonObject> callback = requestedPlayers.get(packet.getKey().toLowerCase());
			requestedPlayers.remove(packet.getKey().toLowerCase());

			if(callback != null){
				callback.done(new JsonParser().parse(packet.getData()).getAsJsonObject(), null);
			} else {

				GameBadblockPlayer player = getRealPlayer(packet.getKey().toLowerCase());
				if(player != null){
					player.updateData(new JsonParser().parse(packet.getData()).getAsJsonObject());
					Bukkit.getPluginManager().callEvent(new PlayerDataChangedEvent(player));
				}
			}
		}else if(packet.getType() == DataType.PLAYER && packet.getAction() == DataAction.MODIFICATION){
			GameBadblockPlayer player = getRealPlayer(packet.getKey().toLowerCase());
			if (player == null) return;
			JsonElement jsonElement = new JsonParser().parse(packet.getData());
			if (jsonElement != null) {
				JsonObject jsonObject = jsonElement.getAsJsonObject();
				if (jsonObject != null) {
					player.updateData(jsonObject);
				}
			}
			if(player != null){
				Bukkit.getPluginManager().callEvent(new PlayerDataChangedEvent(player));
			}
		} else if(packet.getType() == DataType.IP && packet.getAction() == DataAction.SEND){
			Callback<JsonObject> callback = requestedIps.get(packet.getKey().toLowerCase());
			requestedIps.remove(packet.getKey().toLowerCase());

			if(callback != null){
				callback.done(new JsonParser().parse(packet.getData()).getAsJsonObject(), null);
			}
		} else if(packet.getType() == DataType.PERMISSION && packet.getAction() == DataAction.SEND){
			new PermissionManager(new JsonParser().parse(packet.getData()).getAsJsonArray());
		}
	}

	private GameBadblockPlayer getRealPlayer(String playerName)
	{
		Player playerz = Bukkit.getPlayer(playerName);
		if (playerz != null) return (GameBadblockPlayer) playerz;
		for (BadblockPlayer player : GameAPI.getAPI().getOnlinePlayers())
		{
			GameBadblockPlayer gbp = (GameBadblockPlayer) player;
			if (gbp.getRealName().equalsIgnoreCase(playerName))
			{
				return gbp;
			}
		}
		return null;
	}

	@Override
	public void handle(PacketMatchmakingPong packet){
		if(requestedPing.containsKey(packet.getId())){
			requestedPing.get(packet.getId()).done(packet.getPlayerCount(), null);
		}
	}

	@Override public void handle(PacketPlayerChat packet){}
	@Override public void handle(PacketPlayerJoin packet){}
	@Override public void handle(PacketPlayerQuit packet){}
	@Override public void handle(PacketPlayerPlace packet){}
	@Override public void handle(PacketHelloworld packet){}
	@Override public void handle(PacketMatchmakingJoin packet){}
	@Override public void handle(PacketMatchmakingKeepalive packet){}
	@Override public void handle(PacketMatchmakingPing packet){}
	@Override public void handle(PacketPlayerLogin packet){}
	@Override public void handle(PacketReconnectionInvitation packet){}

	@Override
	public void handle(PacketLadderStop packet) {
		try {
			Thread.sleep(10000L);
			Bukkit.shutdown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	
	}

	@Override public void handle(PacketSimpleCommand packet) {
	}

	@Override
	public void handle(PacketPlayerNickSet packet) {
	}

	@Override
	public void broadcast(String... messages) {
		//System.getSecurityManager().checkPermission(new RuntimePermission("badblockDatabase"));

		if(GameAPI.getAPI().getRunType() == RunType.DEV) return;
		sendPacket(new PacketPlayerChat(null, ChatAction.MESSAGE_FLAT, messages));
	}
}

