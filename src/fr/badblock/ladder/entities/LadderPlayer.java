package fr.badblock.ladder.entities;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.gson.JsonObject;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.chat.ChatMessage;
import fr.badblock.ladder.api.entities.Bukkit;
import fr.badblock.ladder.api.entities.BungeeCord;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.protocol.packets.Packet;
import fr.badblock.protocol.packets.PacketPlayerChat;
import fr.badblock.protocol.packets.PacketPlayerChat.ChatAction;
import fr.badblock.protocol.packets.PacketPlayerData;
import fr.badblock.protocol.packets.PacketPlayerData.DataAction;
import fr.badblock.protocol.packets.PacketPlayerData.DataType;
import fr.badblock.protocol.packets.PacketPlayerLogin;
import fr.badblock.protocol.packets.PacketPlayerPlace;
import fr.badblock.protocol.packets.PacketPlayerQuit;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter public class LadderPlayer extends LadderOfflinePlayer implements Player {
	
	
	@Setter public  UUID			uniqueId;
	private final InetSocketAddress address;
	
	private final BungeeCord		bungeeServer;
	@Setter(value=AccessLevel.PACKAGE)
	private       Bukkit		    bukkitServer;
	
	@Setter
	private String					requestedGame;
	private boolean				    canJoinHimself;
	@Getter
	private List<UUID>			    playersWithHim;
	
	public LadderPlayer(BungeeCord server, PacketPlayerLogin packet){
		super(packet.getPlayerName().toLowerCase(), packet.getAddress().getAddress());
		
		boolean first = !getData().has("lastIp");
		
		this.address  	  = packet.getAddress();
		this.playersWithHim = new ArrayList<>();
		this.bungeeServer = server;
		getData().addProperty("lastIp", address.getAddress().getHostAddress());
		
		if(!getData().has("uniqueId")){
			if(first)
				uniqueId = UUID.randomUUID();
			else uniqueId = UUID.nameUUIDFromBytes(("OfflinePlayer:" + packet.getPlayerName()).getBytes());
			getData().addProperty("uniqueId", uniqueId.toString());
		} else uniqueId = UUID.fromString(getData().get("uniqueId").getAsString());
		
		if(!getData().has("onlineMode")){
			getData().addProperty("onlineMode", false);
		}
		
		getIpData().saveData();
		saveData();
	}

	@Override
	public void sendPacket(Packet packet) {
		bungeeServer.sendPacket(packet);
	}

	@Override
	public void sendMessages(String... messages) {
		sendPacket(new PacketPlayerChat(getName(), ChatAction.MESSAGE_FLAT, messages));
	}
	
	@Override
	public void sendMessage(String message){
		sendMessages(message);
	}

	@Override
	public void forceCommand(String... commands) {
		sendPacket(new PacketPlayerChat(getName(), ChatAction.COMMAND, commands));
	}
	
	@Override
	public void disconnect(String... reason) {
		sendPacket(new PacketPlayerQuit(name, reason));
	}

	@Override
	public void send(ChatMessage message) {
		message.send(this);
	}

	@Override
	public void sendTab(String... tab) {
		sendPacket(new PacketPlayerChat(getName(), ChatAction.TABLIST, tab));
	}
	
	@Override
	public void connect(Bukkit server){
		sendPacket(new PacketPlayerPlace(getName(), server.getName()));
	}

	@Override
	public void sendToBungee(String... dataPart) {
		JsonObject obj = new JsonObject();
		for(String part : dataPart){
			if(getData().has(part))
				obj.add(part, getData().get(part));
		}
		Packet packet = new PacketPlayerData(DataType.PLAYER, DataAction.MODIFICATION, name.toLowerCase(), obj.toString());
		sendPacket(packet);
	}

	@Override
	public void sendToBukkit(String... dataPart) {
		JsonObject obj = new JsonObject();
		for(String part : dataPart){
			if(getData().has(part))
				obj.add(part, getData().get(part));
		}
		
		Packet packet = new PacketPlayerData(DataType.PLAYER, DataAction.MODIFICATION, name.toLowerCase(), obj.toString());
		if(bukkitServer != null)
			bukkitServer.sendPacket(packet);
	}

	@Override
	public void canJoinHimself(boolean can) {
		this.canJoinHimself = can;
	}

	@Override
	public boolean canJoinHimself() {
		return canJoinHimself;
	}
	
	@Override
	public void setPlayersWithHim(List<UUID> uuids) {
		this.playersWithHim = uuids;
		getData().addProperty("playersWithHim", Ladder.getInstance().getGson().toJson(this.getPlayersWithHim()));
	}
	
}
