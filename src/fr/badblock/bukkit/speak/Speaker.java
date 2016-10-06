package fr.badblock.bukkit.speak;

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
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import fr.badblock.protocol.PacketHandler;
import fr.badblock.protocol.Protocol;
import fr.badblock.protocol.packets.Packet;
import fr.badblock.protocol.packets.PacketHelloworld;
import fr.badblock.protocol.packets.PacketLadderStop;
import fr.badblock.protocol.packets.PacketPlayerChat;
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
import fr.badblock.protocol.packets.matchmaking.PacketMatchmakingKeepalive.ServerStatus;
import fr.badblock.protocol.packets.matchmaking.PacketMatchmakingPing;
import fr.badblock.protocol.packets.matchmaking.PacketMatchmakingPong;
import fr.badblock.protocol.socket.SocketHandler;

@SuppressWarnings("unused")
public class Speaker implements PacketHandler {
	private final Map<String,  Callback<JsonObject>> requestedPlayers = new HashMap<>();
	private final Map<String,  Callback<JsonObject>> requestedIps     = new HashMap<>();
	private final Map<Integer, Callback<Integer>>    requestedCount   = new HashMap<>();

	private SocketHandler					  		 socketHandler;
	private String 									 ip;
	private int    									 port;

	private int										 nextKey;

	private boolean trying   = false;
	
	private Queue<Packet>							 waitingPackets;
	
	public Speaker(String ip, int port){
		try {
			this.ip 		   = ip;
			this.port		   = port;
			System.out.println("Loaded Speaker(" + ip + ", " + port + ")");
			Socket socket 	   = new Socket(ip, port);
			this.socketHandler = new SocketHandler(socket, Protocol.LADDER_TO_BUKKIT, Protocol.BUKKIT_TO_LADDER, this, false);

			this.socketHandler.start();
			String ipZ = Bukkit.getIp();
			String fileName = "server.properties";
			List<String> lines = null;
			try {
				lines = Files.readAllLines(Paths.get(fileName), Charset.defaultCharset());
			} catch (IOException e) {
				e.printStackTrace();
			}
			for (String line : lines) {
				if (line.startsWith("docker-ip=")) {
					ipZ = line.replace("docker-ip=", "");
				}
			}
			this.socketHandler.getOut().writeUTF(ipZ);
			this.socketHandler.getOut().writeInt(Bukkit.getPort());
			
			this.waitingPackets = Queues.newLinkedBlockingDeque();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendPacket(Packet packet){
		if(socketHandler == null || socketHandler.getSocket().isClosed()){
			//Bukkit.shutdown();
			
			/*if(trying){
				if(packet instanceof PacketPlayerData)
					waitingPackets.add(packet);
				return;
			}
			
			trying = true;
			new Thread(){
				@Override
				public void run(){
					try {
						Socket socket = new Socket(ip, port);
						socketHandler = new SocketHandler(socket, Protocol.LADDER_TO_BUKKIT, Protocol.BUKKIT_TO_LADDER, Speaker.this, false);

						socketHandler.start();

						socketHandler.getOut().writeUTF(Bukkit.getIp());
						socketHandler.getOut().writeInt(Bukkit.getPort());
						
						socketHandler.sendPacket(packet);
						
						Packet p = null;
						
						while((p = waitingPackets.poll()) != null){
							socketHandler.sendPacket(p);
						}
					} catch (IOException e) {
						trying = false;
						e.printStackTrace();
					} finally {
						try {
							Thread.sleep(15_000L);
						} catch (InterruptedException unused){}
						
						trying = false;
					}
				}
			}.start();*/
		} else socketHandler.sendPacket(packet);
	}

	public void updatePlayerData(Player player, JsonObject toUpdate){
		sendPacket(new PacketPlayerData(DataType.PLAYER, DataAction.MODIFICATION, player.getName(), toUpdate.toString()));
		System.out.println("Send > " + player.getName() + " / " + DataType.PLAYER + " / " + DataAction.MODIFICATION + " / " + toUpdate.toString());
		
	}

	public void updatePlayerData(String name, JsonObject toUpdate){
		if(toUpdate != null) {
			sendPacket(new PacketPlayerData(DataType.PLAYER, DataAction.MODIFICATION, name, toUpdate.toString()));
			System.out.println("Send > " + name + " / " + DataType.PLAYER + " / " + DataAction.MODIFICATION + " / " + toUpdate.toString());
		}
	}
	
	public void updateIpPlayerData(Player player, JsonObject toUpdate){
		String ip = player.getAddress().getAddress().getHostAddress();
		if(toUpdate != null) {
			sendPacket(new PacketPlayerData(DataType.IP, DataAction.MODIFICATION, ip, toUpdate.toString()));
			System.out.println("Send > " + player.getName() + " / " + ip + " / " + DataType.PLAYER + " / " + DataAction.MODIFICATION + " / " + toUpdate.toString());
		}
	}

	public void getPlayerData(Callback<JsonObject> object, Player player){
		requestedPlayers.put(player.getName().toLowerCase(), object);
		sendPacket(new PacketPlayerData(DataType.PLAYER, DataAction.REQUEST, player.getName(), "*"));
	}
	
	public void getPlayerData(Callback<JsonObject> object, String player){
		requestedPlayers.put(player.toLowerCase(), object);
		sendPacket(new PacketPlayerData(DataType.PLAYER, DataAction.REQUEST, player, "*"));
	}

	public void getIpPlayerData(Callback<JsonObject> object, Player player){
		String ip = player.getAddress().getAddress().getHostAddress();

		requestedIps.put(ip, object);
		sendPacket(new PacketPlayerData(DataType.IP, DataAction.REQUEST, ip, "*"));
	}

	public void countPlayers(Callback<Integer> callback, String... servers){
		requestedCount.put(nextKey, callback);
		sendPacket(new PacketMatchmakingPing(nextKey, servers));
		
		nextKey++;
	}

	public void sendJoin(Player player, String server){
		sendPacket(new PacketMatchmakingJoin(server, player.getName()));
	}

	public void sendKeepalive(ServerStatus status, int curr, int max){
		sendPacket(new PacketMatchmakingKeepalive(status, curr, max));
	}

	@Override
	public void handle(PacketPlayerData packet) {
		System.out.println("Receive > " + packet.getType() + " / " + packet.getAction() + " / " + packet.getKey() + " / " + packet.getData());
		if(packet.getType() == DataType.PLAYER && packet.getAction() == DataAction.SEND){
			Callback<JsonObject> callback = requestedPlayers.get(packet.getKey().toLowerCase());

			if(callback != null){
				callback.call(new JsonParser().parse(packet.getData()).getAsJsonObject(), null);
			}
		} else if(packet.getType() == DataType.IP && packet.getAction() == DataAction.SEND){
			Callback<JsonObject> callback = requestedIps.get(packet.getKey().toLowerCase());

			if(callback != null){
				callback.call(new JsonParser().parse(packet.getData()).getAsJsonObject(), null);
			}
		}
	}

	@Override
	public void handle(PacketMatchmakingPong packet) {
		Callback<Integer> callback = requestedCount.get(packet.getId());

		if(callback != null)
			callback.call(packet.getPlayerCount(), null);
	}

	@Override public void handle(PacketPlayerChat packet){}
	@Override public void handle(PacketPlayerJoin packet){}
	@Override public void handle(PacketPlayerQuit packet){}
	@Override public void handle(PacketPlayerPlace packet){}
	@Override public void handle(PacketHelloworld packet){}
	@Override public void handle(PacketMatchmakingJoin packet){}
	@Override public void handle(PacketMatchmakingKeepalive packet){}
	@Override public void handle(PacketMatchmakingPing packet){}

	@Override
	public void handle(PacketLadderStop packet) {
		try {
			Thread.sleep(10000L);
			Bukkit.shutdown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handle(PacketPlayerLogin packet) {
		
	}

	@Override
	public void handle(PacketReconnectionInvitation packet) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handle(PacketSimpleCommand packet) {
		// TODO Auto-generated method stub
		
	}
}
