package fr.badblock.ladder.entities;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import fr.badblock.ladder.Proxy;
import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.commands.Command;
import fr.badblock.ladder.api.entities.Bukkit;
import fr.badblock.ladder.api.entities.CommandSender;
import fr.badblock.ladder.api.entities.OfflinePlayer;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.ladder.api.entities.PlayerIp;
import fr.badblock.ladder.api.events.all.BukkitCommandEvent;
import fr.badblock.ladder.api.events.all.MatchmakingJoinEvent;
import fr.badblock.ladder.api.events.all.MatchmakingServerEvent;
import fr.badblock.ladder.api.events.all.MatchmakingServerEvent.ServerStatus;
import fr.badblock.ladder.api.plugins.PluginsManager;
import fr.badblock.ladder.commands.CommandAlert;
import fr.badblock.ladder.commands.CommandEnd;
import fr.badblock.ladder.commands.CommandPermissions;
import fr.badblock.permissions.PermissibleGroup;
import fr.badblock.permissions.PermissiblePlayer;
import fr.badblock.protocol.PacketHandler;
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
import fr.badblock.protocol.packets.matchmaking.PacketMatchmakingPing;
import fr.badblock.protocol.packets.matchmaking.PacketMatchmakingPong;
import lombok.Getter;

public class LadderBukkit implements Bukkit, PacketHandler {
	@Getter
	protected final InetSocketAddress address;
	@Getter
	protected final String			name;
	
	public LadderBukkit(InetSocketAddress address, String name){
		this.address = address;
		this.name    = name;
	}

	@Override
	public Collection<UUID> getPlayers() {
		List<UUID> players = new ArrayList<>();
		
		
		for(Player player : Ladder.getInstance().getOnlinePlayers()){
			if(this.equals(player.getBukkitServer()))
				players.add(player.getUniqueId());
		}
		
		return players;
	}

	@Override
	public void sendPacket(Packet packet) {
		Proxy.getInstance().getHost().sendPacket(address, packet);
	}

	@Override
	public void broadcast(String... message) {
		for(UUID uniqueId : getPlayers()){
			Player player = Ladder.getInstance().getPlayer(uniqueId);
				
			if(player != null)
				player.sendMessages(message);
		}
	}
	
	@Override
	public void sendPermissions() {
		JsonArray array = Proxy.getInstance().getPermissions().saveAsJson();
		sendPacket(new PacketPlayerData(DataType.SERVERS, DataAction.SEND, "*", array.toString()));
	}
	
	@Override
	public void handle(PacketPlayerData packet) {
		if(packet.getType() == DataType.PLAYER){
			OfflinePlayer player = Ladder.getInstance().getOfflinePlayer(packet.getKey());

			if(packet.getAction() == DataAction.MODIFICATION && player != null){
				player.updateData(new JsonParser().parse(packet.getData()).getAsJsonObject());
			} else if(packet.getAction() == DataAction.SEND && player != null){
				player.setData(new JsonParser().parse(packet.getData()).getAsJsonObject());
			} else if(packet.getAction() == DataAction.REQUEST){
				JsonObject ret = new JsonObject();
				if(player != null){
					ret = player.getData();
					PermissiblePlayer permissiblePlayer = (PermissiblePlayer) player.getAsPermissible();
					if (!player.getName().equalsIgnoreCase(player.getNickName())) {
						permissiblePlayer = LadderPermissionManager.getInstance().createPlayer(player.getNickName(), player.getData());
						if (((PermissibleGroup) permissiblePlayer.getParent()).isStaff()) {
							permissiblePlayer.removeParent(permissiblePlayer.getParent());
						}
						final PermissiblePlayer permPlayer = permissiblePlayer;
						permPlayer.getAlternateGroups().entrySet().stream().filter(group -> LadderPermissionManager.getInstance().getGroup(group.getKey()) != null && LadderPermissionManager.getInstance().getGroup(group.getKey()).isStaff()).forEach(group -> permPlayer.removeParent(group.getKey()));
					}
					ret.add("permissions", permissiblePlayer.saveAsJson());
				}
				sendPacket(new PacketPlayerData(DataType.PLAYER, DataAction.SEND, packet.getKey(), ret.toString()));
			}
		} else if(packet.getType() == DataType.IP){
			PlayerIp player = null;
			try {
				player = Ladder.getInstance().getIpData(InetAddress.getByName(packet.getKey()));

				if(packet.getAction() == DataAction.MODIFICATION && player != null){
					player.updateData(new JsonParser().parse(packet.getData()).getAsJsonObject());
				} else if(packet.getAction() == DataAction.SEND && player != null){
					player.updateData(new JsonParser().parse(packet.getData()).getAsJsonObject());
				} else {
					JsonElement ret = new JsonObject();	
					if(player != null){
						ret = player.getData();
					}

					sendPacket(new PacketPlayerData(DataType.IP, DataAction.SEND, packet.getKey(), ret.toString()));
				}

				player.saveData();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		} else if(packet.getType() == DataType.PERMISSION){
			JsonElement ret = Proxy.getInstance().getPermissions().handlePermissionPacket(packet.getAction(), packet.getKey());
			if(packet.getAction() == DataAction.REQUEST){
				sendPacket(new PacketPlayerData(DataType.PERMISSION, DataAction.SEND, packet.getKey(), ret.toString()));
			}
		}
	}

	@Override
	public void handle(PacketMatchmakingJoin packet) {
		Player player = Ladder.getInstance().getPlayer(packet.getPlayerName());
		
		if(player != null)
			Ladder.getInstance().getPluginsManager().dispatchEvent(new MatchmakingJoinEvent(player, packet.getServerName()));
	}

	@Override
	public void handle(PacketMatchmakingKeepalive packet) {
		Ladder.getInstance().getPluginsManager().dispatchEvent( new MatchmakingServerEvent(this, ServerStatus.getStatus(packet.getStatus().getId()), packet.getPlayers(), packet.getSlots()) );
	}

	@Override
	public void handle(PacketMatchmakingPing packet) {
		int result = 0;
		
		for(String server : packet.getServers()){
			result += countPlayers(server.toLowerCase());
		}
		
		sendPacket(new PacketMatchmakingPong(packet.getId(), result));
	}

	@Override
	public void handle(PacketReconnectionInvitation packet) {
		OfflinePlayer ofplayer = Ladder.getInstance().getOfflinePlayer(packet.getPlayerName());
		Bukkit		  actual   = Proxy.getInstance().getReconnectionInvitation(ofplayer);
		
		if(actual != null && !actual.equals(this)){
			Proxy.getInstance().removeReconnectionInvitation(ofplayer, true);
		}
		
		if(packet.isInvited()){
			Proxy.getInstance().invite(ofplayer, this);
		} else {
			Proxy.getInstance().removeReconnectionInvitation(ofplayer, true);
		}
	}
	
	private int countPlayers(String server){
		if(server.equals("*")){
			return Ladder.getInstance().getBungeeOnlineCount();
		} else {
			int result = 0;
			
			for(Bukkit bukkit : Ladder.getInstance().getBukkitServers()){
				if(bukkit.getName().startsWith(server))
					result += bukkit.getPlayers().size();
			}
			
			return result;
		}
	}

	@Override public void handle(PacketMatchmakingPong packet){}
	
	@Override public void handle(PacketPlayerChat packet){}
	@Override public void handle(PacketPlayerJoin packet){}
	@Override public void handle(PacketPlayerQuit packet){}
	@Override public void handle(PacketPlayerPlace packet){}

	@Override public void handle(PacketHelloworld packet) {}

	@Override public void handle(PacketLadderStop packet){}

	@Override public void handle(PacketPlayerLogin packet){}

	@Override
	public void handle(PacketSimpleCommand packet) {
		System.out.println("Received");
		
		String[] parts = packet.getCommand().split(" ");
		
		Ladder ladder = Ladder.getInstance();
		PluginsManager pmanager = ladder.getPluginsManager();
		
		Command command = pmanager.getCommandByName(parts[0]);
		
		System.out.println("Command : " + parts[0] + " (" + command + ")");
		
		if(command == null)
			return;
		
		if(command instanceof CommandEnd || command instanceof CommandAlert || command instanceof CommandPermissions)
			return;
		
		Player player = packet.getPlayer() != null ? ladder.getPlayer(packet.getPlayer()) : null;
		CommandSender commandSender = player == null ? ladder.getConsoleCommandSender() : player;
		
		System.out.println("Player : " + packet.getPlayer() + "(" + commandSender + ")");
		
		boolean dispatch = !pmanager.dispatchEvent(new BukkitCommandEvent(command, player, packet.getCommand())).isCancelled();
		
		System.out.println("Allow dispatching : " + dispatch);
		
		if(dispatch) commandSender.forceCommand(packet.getCommand());
	}
	
}
