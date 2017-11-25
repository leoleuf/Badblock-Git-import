package fr.badblock.ladder.entities;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import fr.badblock.ladder.Proxy;
import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.chat.Motd;
import fr.badblock.ladder.api.entities.Bukkit;
import fr.badblock.ladder.api.entities.BungeeCord;
import fr.badblock.ladder.api.entities.OfflinePlayer;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.ladder.api.entities.PlayerIp;
import fr.badblock.ladder.api.events.all.PlayerJoinEvent;
import fr.badblock.ladder.api.events.all.PlayerQuitEvent;
import fr.badblock.ladder.api.events.all.ServerSwitchEvent;
import fr.badblock.ladder.data.LadderIpDataHandler;
import fr.badblock.ladder.sql.BadblockDatabase;
import fr.badblock.ladder.sql.Request;
import fr.badblock.ladder.sql.Request.RequestType;
import fr.badblock.permissions.PermissiblePlayer;
import fr.badblock.protocol.PacketHandler;
import fr.badblock.protocol.packets.Packet;
import fr.badblock.protocol.packets.PacketHelloworld;
import fr.badblock.protocol.packets.PacketLadderStop;
import fr.badblock.protocol.packets.PacketPlayerChat;
import fr.badblock.protocol.packets.PacketPlayerChat.ChatAction;
import fr.badblock.protocol.packets.PacketPlayerData;
import fr.badblock.protocol.packets.PacketPlayerData.DataAction;
import fr.badblock.protocol.packets.PacketPlayerData.DataType;
import fr.badblock.protocol.packets.PacketPlayerJoin;
import fr.badblock.protocol.packets.PacketPlayerLogin;
import fr.badblock.protocol.packets.PacketPlayerNickSet;
import fr.badblock.protocol.packets.PacketPlayerPlace;
import fr.badblock.protocol.packets.PacketPlayerQuit;
import fr.badblock.protocol.packets.PacketReconnectionInvitation;
import fr.badblock.protocol.packets.PacketSimpleCommand;
import fr.badblock.protocol.packets.matchmaking.PacketMatchmakingJoin;
import fr.badblock.protocol.packets.matchmaking.PacketMatchmakingKeepalive;
import fr.badblock.protocol.packets.matchmaking.PacketMatchmakingPing;
import fr.badblock.protocol.packets.matchmaking.PacketMatchmakingPong;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data@EqualsAndHashCode(callSuper=false)
public class LadderBungee extends ConsoleCommandSender implements BungeeCord, PacketHandler {
	private final String 			  name;
	private final InetSocketAddress   address;

	public LadderBungee(String name, InetSocketAddress address){
		this.name    = name;
		this.address = address;
	}

	@Override
	public void sendPacket(Packet packet) {
		Proxy.getInstance().getHost().sendPacket(address, packet);
	}

	@Override
	public void broadcast(String... message) {
		sendPacket(new PacketPlayerChat(null, ChatAction.MESSAGE_FLAT, message));
	}

	@Override
	public void sendPermissions() {
		JsonArray array = Proxy.getInstance().getPermissions().saveAsJson();
		//System.out.println("Send permissions (*) [" + array.toString() + "] to '" + name + "'.");
		sendPacket(new PacketPlayerData(DataType.PERMISSION, DataAction.SEND, "*", array.toString()));
	}

	@Override
	public void sendCommands() {
		sendPacket(new PacketPlayerData(DataType.COMMANDS, DataAction.SEND, "*", Proxy.getInstance().getPluginsManager().getJsonCommandsNames().toString()));
	}

	@Override
	public void sendServers() {
		JsonArray toSend = new JsonArray();
		for(Bukkit bukkit : Ladder.getInstance().getBukkitServers()){
			if (bukkit != null && bukkit.getName() != null && bukkit.getAddress() != null && bukkit.getAddress().getAddress() != null && bukkit.getAddress().getAddress().getHostAddress() != null) {
				JsonObject server = new JsonObject();
				server.addProperty("name", bukkit.getName());
				server.addProperty("ip", bukkit.getAddress().getAddress().getHostAddress());
				server.addProperty("port", bukkit.getAddress().getPort());
				toSend.add(server);
			}

		}

		sendPacket(new PacketPlayerData(DataType.SERVERS, DataAction.SEND, "*", toSend.toString()));
	}

	@Override
	public void sendMotd(Motd motd) {
		sendPacket(new PacketPlayerData(DataType.MOTD, DataAction.SEND, "", Proxy.gson.toJson(motd)));
	}

	@Override
	public void addBukkitServer(Bukkit bukkit) {
		JsonObject server = new JsonObject();
		server.addProperty("name", bukkit.getName());
		server.addProperty("ip", bukkit.getAddress().getAddress().getHostAddress());
		server.addProperty("port", bukkit.getAddress().getPort());

		sendPacket(new PacketPlayerData(DataType.SERVERS, DataAction.MODIFICATION, "add", server.toString()));
	}

	@Override
	public void removeBukkitServer(Bukkit bukkit) {
		JsonArray array = new JsonArray();
		array.add(new JsonPrimitive(bukkit.getName()));

		sendPacket(new PacketPlayerData(DataType.SERVERS, DataAction.MODIFICATION, "remove", array.toString()));
	}

	private void broadcastOthers(Packet packet){
		for(BungeeCord server : Ladder.getInstance().getServers()){
			if(!server.getName().equalsIgnoreCase(getName())){
				server.sendPacket(packet);
			}
		}
	}

	@Override
	public void handle(PacketPlayerChat packet) {
		new Thread()
		{
			@Override
			public void run()
			{
				if(packet.getUser() == null) {
					if(packet.getType() == ChatAction.LADDER_COMMAND){
						Ladder.getInstance().getConsoleCommandSender().forceCommand(packet.getMessages());
					} else broadcastOthers(packet);
				} else {
					Player player = Ladder.getInstance().getPlayer(packet.getUser());
					if(player != null){
						if(packet.getType() == ChatAction.LADDER_COMMAND){
							for(String command : packet.getMessages()){
								Ladder.getInstance().getPluginsManager().executeCommand(player, command);
							}
						} else {
							player.sendPacket(packet);
						}
					}
				}
			}
		}.start();
	}

	@Override
	public void handle(PacketPlayerData packet) {
		if(packet.getType() == DataType.PLAYER){
			//if (Proxy.getInstance().getRabbitService() != null)
			//	Proxy.getInstance().getRabbitService().sendPacket("ladder.playersupdate", Integer.toString(Ladder.getInstance().getOnlinePlayers().size()), Encodage.UTF8, RabbitPacketType.PUBLISHER, 5000, false);
			OfflinePlayer player = Ladder.getInstance().getOfflinePlayer(packet.getKey());

			if(player == null) return;

			if(packet.getAction() == DataAction.MODIFICATION && player != null){
				player.updateData(new JsonParser().parse(packet.getData()).getAsJsonObject());
			} else if(packet.getAction() == DataAction.SEND && player != null){
				player.setData(new JsonParser().parse(packet.getData()).getAsJsonObject());
			} else if(packet.getAction() == DataAction.REQUEST){
				JsonObject ret = new JsonObject();
				if(player != null){
					ret = player.getData();
					PermissiblePlayer permissiblePlayer = (PermissiblePlayer) player.getAsPermissible();
					/*if (!player.getName().equalsIgnoreCase(player.getNickName())) {
						permissiblePlayer = LadderPermissionManager.getInstance().createPlayer(player.getNickName(), player.getData());
						final PermissiblePlayer permPlayer = permissiblePlayer;
						PermissibleGroup permissibleGroup = (PermissibleGroup) permissiblePlayer.getParent();
						if (permissibleGroup.isStaff()) {
							permissibleGroup.getPermissions().forEach(permission -> permPlayer.permissions.add(permission));
							permissiblePlayer.removeParent(permissiblePlayer.getParent());
						}
						permPlayer.getAlternateGroups().entrySet().stream().filter(group -> LadderPermissionManager.getInstance().getGroup(group.getKey()) != null && LadderPermissionManager.getInstance().getGroup(group.getKey()).isStaff()).forEach(group -> {
							PermissibleGroup groupe = LadderPermissionManager.getInstance().getGroup(group.getKey());
							if (groupe == null) return;
							groupe.getPermissions().forEach(permission -> permPlayer.permissions.add(permission));
							permPlayer.removeParent(group.getKey());
						});
					}*/
					ret.add("permissions", permissiblePlayer.saveAsJson());
				}
				sendPacket(new PacketPlayerData(DataType.PLAYER, DataAction.SEND, packet.getKey(), ret.toString()));
			}
		} else if(packet.getType() == DataType.IP){
			//if (Proxy.getInstance().getRabbitService() != null)
			//	Proxy.getInstance().getRabbitService().sendPacket("ladder.playersupdate", Integer.toString(Ladder.getInstance().getOnlinePlayers().size()), Encodage.UTF8, RabbitPacketType.PUBLISHER, 5000, false);
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
			//System.out.println("Handle permissions packet [" + packet.getAction() + "] / " + "[" + packet.getKey() + "] / '" + ret.toString() + "' / '" + name + "'.");
			if(packet.getAction() == DataAction.REQUEST){
				sendPacket(new PacketPlayerData(DataType.PERMISSION, DataAction.SEND, packet.getKey(), ret.toString()));
			}
		} else if(packet.getType() == DataType.COMMANDS){
			if(packet.getAction() == DataAction.REQUEST)
				sendCommands();
		} else if(packet.getType() == DataType.SERVERS){
			if(packet.getAction() == DataAction.REQUEST){
				sendServers();
			}
		} else if(packet.getType() == DataType.MOTD){
			sendMotd(Proxy.getInstance().getMotd());
			//if (Proxy.getInstance().getRabbitService() != null)
			//	Proxy.getInstance().getRabbitService().sendPacket("ladder.playersupdate", Integer.toString(Ladder.getInstance().getOnlinePlayers().size()), Encodage.UTF8, RabbitPacketType.PUBLISHER, 5000, false);
		} else if(packet.getType() == DataType.PLAYERS){
			for(Player player : Ladder.getInstance().getOnlinePlayers()){
				sendPacket(new PacketPlayerJoin(player.getName(), player.getName(), player.getUniqueId(), player.getAddress()));
				if(player.getBukkitServer() != null)
					sendPacket(new PacketPlayerPlace(player.getName(), player.getBukkitServer().getName()));
			}
			//if (Proxy.getInstance().getRabbitService() != null)
			//	Proxy.getInstance().getRabbitService().sendPacket("ladder.playersupdate", Integer.toString(Ladder.getInstance().getOnlinePlayers().size()), Encodage.UTF8, RabbitPacketType.PUBLISHER, 5000, false);
		}
	}

	private Map<String, LadderPlayer> loginPlayer = Maps.newConcurrentMap();

	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy � HH:mm:ss");

	public String buildBanReason(String pseudo, long expire, String banReason) {
		final StringBuilder fDate = new StringBuilder();
		Request request = new Request("SELECT * FROM sanctions WHERE expire = '" + expire + "' AND pseudo = '" + pseudo + "'", RequestType.GETTER) {
			String date = "";
			@Override
			public void done(ResultSet resultSet) {
				try {
					if (resultSet.next())
						date = sdf.format(new Date(resultSet.getLong("timestamp")));
					fDate.append(date);
				}catch(Exception err) {
					err.printStackTrace();
				}
			}
		};
		BadblockDatabase.getInstance().addSyncRequest(request);
		String muted = sdf.format(new Date(expire));
		return "�8�l��b�l-�8�l��m----�f�8�l��b�l-�8�l��b �b�lBadBlock �8�l��b�l-�8�l��m----�f�8�l��b�l-�8�l��b\n\n�cCe compte a �t� suspendu !\n\nCe compte a enfreint le r�glement de BadBlock sous le motif \"�f" + banReason + "�c\" le " + fDate.toString() + ".\n�cLa suspension se termine le " + muted + "\n�eEn cas de contestation, postez votre r�clamation sur le forum (https://forum.badblock.fr/)\n\n�r�8�l��b�l-�8�l��m-----------------------�f�8�l��b�l-�8�l�";
	}

	@Override
	public void handle(PacketPlayerLogin packet) {
		new Thread()
		{
			@Override
			public void run()
			{
				LadderPlayer    player = new LadderPlayer(LadderBungee.this, packet);
				PlayerJoinEvent event  = new PlayerJoinEvent(player, LadderBungee.this);

				if(Proxy.getInstance().getMaxPlayers() > 0 && Proxy.getInstance().getBungeeOnlineCount() >= Proxy.getInstance().getMaxPlayers() && !player.hasPermission("ladder.maxplayer.bypass")){
					player.disconnect("�cLe serveur est plein, l'acc�s est r�serv� aux VIP.");
				}

				// R�cup�ration des points boutique

				/*BadblockDatabase.getInstance().addSyncRequest(new Request("SELECT ptsboutique FROM joueurs WHERE pseudo = '" + BadblockDatabase.getInstance().mysql_real_escape_string(player.getName()) + "'", RequestType.GETTER) {
			@Override
			public void done(ResultSet resultSet) {
				try {
					if (resultSet.next()) {
						player.getData().addProperty("shoppoints", resultSet.getInt("ptsboutique"));
						player.saveData();
					}else{
						player.getData().addProperty("shoppoints", -1);
						player.saveData();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}*/
				Ladder.getInstance().getPluginsManager().dispatchEvent(event);

				player.getPunished().checkEnd();
				player.getIpAsPunished().checkEnd();

				if(player.getPunished().isBan()){
					player.disconnect(buildBanReason(player.getName(), player.getPunished().getBanEnd(), player.getPunished().getBanReason())); return;
				} else if((player.getIpData()).getAsPunished().isBan()){
					player.disconnect(buildBanReason(player.getName(), player.getIpData().getAsPunished().getBanEnd(), player.getIpData().getAsPunished().getBanReason())); return;
				}

				if(event.isCancelled()) {
					player.disconnect(event.getCancelReason()); return;
				} else {
					loginPlayer.put(player.getName().toLowerCase(), player);
					sendPacket(new PacketPlayerNickSet(player.getName(), player.getUniqueId(), player.getName()));
					sendPacket(new PacketPlayerData(DataType.PLAYER, DataAction.SEND, packet.getPlayerName(), player.getData().toString()));
					sendPacket(new PacketPlayerData(DataType.IP, DataAction.SEND, packet.getPlayerName(), player.getIpData().getData().toString()));
					//if (Proxy.getInstance().getRabbitService() != null)
					//	Proxy.getInstance().getRabbitService().sendPacket("ladder.playersupdate", Integer.toString(Ladder.getInstance().getOnlinePlayers().size()), Encodage.UTF8, RabbitPacketType.PUBLISHER, 5000, false);
				}
			}
		}.start();
		//}
		//	});


	}

	@Override
	public void handle(PacketPlayerJoin packet) {
		LadderPlayer player = loginPlayer.get(packet.getPlayerName().toLowerCase());

		if(player == null){
			sendPacket(new PacketPlayerQuit(packet.getPlayerName(), null)); return;
		}

		loginPlayer.remove(packet.getPlayerName().toLowerCase());

		broadcastOthers(packet);

		Proxy.getInstance().playerConnect(player);
		((LadderIpDataHandler) player.getIpData()).getPlayers().add(player.getUniqueId());
	}

	@Override
	public void handle(PacketPlayerQuit packet) {

		if(loginPlayer.containsKey(packet.getUserName().toLowerCase()))
			loginPlayer.remove(packet.getUserName().toLowerCase());

		Player player = Ladder.getInstance().getPlayer(packet.getUserName());

		if(player == null) return;

		if(packet.getReason() != null){
			player.sendPacket(packet);
		} else {
			PlayerQuitEvent event = new PlayerQuitEvent(player);
			Ladder.getInstance().getPluginsManager().dispatchEvent(event);

			player.saveData();
			player.getIpData().saveData();

			broadcastOthers(packet);

			((LadderIpDataHandler) player.getIpData()).getPlayers().remove(player.getUniqueId());

			Proxy.getInstance().playerDisconnect(player);

			getPlayers().remove(player.getUniqueId());
			//if (Proxy.getInstance().getRabbitService() != null)
			//	Proxy.getInstance().getRabbitService().sendPacket("ladder.playersupdate", Integer.toString(Ladder.getInstance().getOnlinePlayers().size()), Encodage.UTF8, RabbitPacketType.PUBLISHER, 5000, false);
		}
	}

	@Override
	public void handle(PacketPlayerPlace packet) {
		Player player = Ladder.getInstance().getPlayer(packet.getPlayerName());
		if(player == null) return;

		Bukkit current = player.getBukkitServer();
		Bukkit changed = Ladder.getInstance().getBukkitServer(packet.getServerName());

		if(changed == null) return;

		ServerSwitchEvent event = new ServerSwitchEvent(player, current, changed);
		Ladder.getInstance().getPluginsManager().dispatchEvent(event);

		((LadderPlayer) player).setBukkitServer(changed);
		if((current != null && (current.getName().startsWith("login") || current.getName().equals("skeleton"))) || (current == null && !changed.getName().startsWith("login"))) {
			Bukkit reconnect = Proxy.getInstance().getReconnectionInvitation(player);

			if(reconnect != null){
				player.connect(reconnect);

				Proxy.getInstance().removeReconnectionInvitation(player, false);
			}

		}
	}

	@Override
	public void handle(PacketHelloworld packet) {
		for(Player player : Ladder.getInstance().getOnlinePlayers()){
			if(this.equals(player.getBungeeServer()))
				handle(new PacketPlayerQuit(player.getName(), null));
		}
	}

	@Override public void handle(PacketMatchmakingJoin packet) {}
	@Override public void handle(PacketMatchmakingKeepalive packet) {}
	@Override public void handle(PacketMatchmakingPing packet) {}
	@Override public void handle(PacketMatchmakingPong packet) {}
	@Override public void handle(PacketReconnectionInvitation packet) {}
	@Override
	public Collection<UUID> getPlayers() {
		List<UUID> players = new ArrayList<>();


		for(Player player : Ladder.getInstance().getOnlinePlayers()){
			if(this.equals(player.getBungeeServer()))
				players.add(player.getUniqueId());
		}

		return players;
	}

	@Override public void handle(PacketLadderStop packet){}
	@Override public void handle(PacketSimpleCommand packet){}

	@Override
	public void handle(PacketPlayerNickSet packet) {
	}
}
