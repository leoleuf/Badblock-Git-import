package fr.badblock.ladder.bungee;

import java.lang.reflect.Field;
import java.util.UUID;

import fr.badblock.bungeecord.BungeeCord;
import fr.badblock.bungeecord.api.ChatColor;
import fr.badblock.bungeecord.api.ServerPing;
import fr.badblock.bungeecord.api.ServerPing.PlayerInfo;
import fr.badblock.bungeecord.api.connection.PendingConnection;
import fr.badblock.bungeecord.api.connection.ProxiedPlayer;
import fr.badblock.bungeecord.api.event.AsyncDataLoadRequest;
import fr.badblock.bungeecord.api.event.ChatEvent;
import fr.badblock.bungeecord.api.event.LoginFailEvent;
import fr.badblock.bungeecord.api.event.PermissionCheckEvent;
import fr.badblock.bungeecord.api.event.PlayerDisconnectEvent;
import fr.badblock.bungeecord.api.event.PostLoginEvent;
import fr.badblock.bungeecord.api.event.ProxyPingEvent;
import fr.badblock.bungeecord.api.event.ServerConnectEvent;
import fr.badblock.bungeecord.api.event.ServerConnectionFailEvent;
import fr.badblock.bungeecord.api.event.TabCompleteEvent;
import fr.badblock.bungeecord.api.event.AsyncDataLoadRequest.Result;
import fr.badblock.bungeecord.api.plugin.Listener;
import fr.badblock.bungeecord.event.EventHandler;
import fr.badblock.bungeecord.event.EventPriority;
import fr.badblock.common.commons.utils.StringUtils;
import fr.badblock.common.permissions.Permission;
import fr.badblock.common.protocol.packets.PacketPlayerJoin;
import fr.badblock.common.protocol.packets.PacketPlayerLogin;
import fr.badblock.common.protocol.packets.PacketPlayerPlace;
import fr.badblock.common.protocol.packets.PacketPlayerQuit;
import fr.badblock.ladder.bungee.listeners.ScalerPlayersUpdateListener;
import fr.badblock.ladder.bungee.utils.Motd;
import fr.badblock.ladder.bungee.utils.Punished;
import fr.badblock.ladder.bungee.utils.TimeUnit;

public class LadderListener implements Listener {

	public static long timestampMax = -1;
	public static String finished = "-";

	@EventHandler
	public void onJoin(AsyncDataLoadRequest e){
		if(e.getPlayer().contains("/")){
			e.getDone().done(new Result(null, ChatColor.RED + "Votre pseudonyme est invalide !"), null);
			return;
		}
		if (LadderBungee.getInstance().getPlayer(e.getPlayer()) != null) {
			ProxiedPlayer player = BungeeCord.getInstance().getPlayer(e.getPlayer());
			if (player == null || !player.isConnected()) {
				LadderBungee.getInstance().playerList.remove(e.getPlayer());
				LadderBungee.getInstance().byName.remove(e.getPlayer());
			}else{
				e.getDone().done(new Result(null, ChatColor.RED + "Vous �tes d�j� connect� sur BadBlock!"), null);
			}
			return;
		}
		PacketPlayerLogin packet = new PacketPlayerLogin(e.getPlayer(), e.getHandler().getAddress());
		LadderBungee.getInstance().handle(packet, e.getDone());
		LadderBungee.getInstance().getClient().sendPacket(packet);
	}

	@EventHandler
	public void onJoin(PostLoginEvent e){
		PacketPlayerJoin packet = new PacketPlayerJoin(e.getPlayer().getName(), e.getPlayer().getName(), e.getPlayer().getUniqueId(), e.getPlayer().getAddress());
		LadderBungee.getInstance().handle(packet, true);
		LadderBungee.getInstance().getClient().sendPacket(packet);

		if (!LadderBungee.getInstance().uuids.containsKey(e.getPlayer().getName().toLowerCase())) {
			LadderBungee.getInstance().uuids.put(e.getPlayer().getName().toLowerCase(), e.getPlayer().getUniqueId());		
		}
	}

	@EventHandler
	public void onQuit(LoginFailEvent e){
		PacketPlayerQuit packet = new PacketPlayerQuit(e.getHandler().getName(), null);
		LadderBungee.getInstance().handle(packet, true);
		LadderBungee.getInstance().getClient().sendPacket(packet);
	}

	@EventHandler
	public void onQuit(PlayerDisconnectEvent e){
		PacketPlayerQuit packet = new PacketPlayerQuit(e.getPlayer().getName(), null);
		LadderBungee.getInstance().handle(packet, true);
		LadderBungee.getInstance().getClient().sendPacket(packet);
	}

	@EventHandler(priority=EventPriority.HIGHEST)
	public void onServerSwitch(ServerConnectEvent e){
		Player player = LadderBungee.getInstance().getPlayer(e.getPlayer().getName());
		if (player != null) {
			try {
				ProxiedPlayer proxiedPlayer = BungeeCord.getInstance().getPlayer(player.getName());
				PendingConnection pendingConnection = proxiedPlayer.getPendingConnection();
				Field uniqueId = pendingConnection.getClass().getDeclaredField("uniqueId");
				uniqueId.setAccessible(true);
				if (player.getCustomUUID() != null) {
					uniqueId.set(pendingConnection, player.getCustomUUID());
				}
			}catch(Exception error) {
				error.printStackTrace();
			}
		}
		if(e.getPlayer().getServer() == null){
			String playerName = e.getPlayer().getName();
			String server = e.getTarget().getName();

			new Thread(){
				@Override
				public void run(){
					try {
						int i = 0;

						while(LadderBungee.getInstance().getPlayer(player.getName()) == null){
							Thread.sleep(2L);
							i++;

							if(i == 50) return; // player has disconnected ?
						}

						Thread.sleep(4L);
					} catch (InterruptedException e){
						return;
					} finally {		
						LadderBungee.getInstance().getClient().sendPacket(new PacketPlayerPlace(playerName, server));
					}
				}
			}.start();

			return;
		}

		LadderBungee.getInstance().getClient().sendPacket(new PacketPlayerPlace(e.getPlayer().getName(), e.getTarget().getName()));
	}

	@EventHandler
	public void onFail(ServerConnectionFailEvent e){
		if(!e.isKick()){
			LadderBungee.getInstance().getClient().sendPacket(new PacketPlayerPlace(e.getPlayer().getName(), e.getFallback().getName()));
		}
	}

	@EventHandler
	public void onTabComplete(TabCompleteEvent e){
		if(e.getCursor().startsWith("/") && e.getCursor().split(" ").length == 1){
			e.setCancelled(true);
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority=EventPriority.HIGH)
	public void onPing(ProxyPingEvent e){
		e.registerIntent(LadderBungee.getInstance());

		Motd 		   motd   = LadderBungee.getInstance().getMotd();
		ServerPing     old    = e.getResponse();
		e.setResponse(null);
		ServerPing     reply  = new ServerPing();

		if(motd == null)
			return;


		PlayerInfo[]   sample = new PlayerInfo[motd.getPlayers().length];

		for(int i=0;i<sample.length;i++){
			sample[i] = new PlayerInfo(ChatColor.translateAlternateColorCodes('&', motd.getPlayers()[i]), UUID.randomUUID());
		}

		int m = LadderBungee.getInstance().slots;
		BungeeCord.getInstance().setPlayerNames(LadderBungee.getInstance().bungeePlayerList);
		BungeeCord.getInstance().setCurrentCount(ScalerPlayersUpdateListener.get());
		reply.setPlayers(new ServerPing.Players(m, LadderBungee.getInstance().getOnlineCount(), sample));
		String[] motdString = motd.getMotd().clone();
		if (motdString[1].contains("@1")) {
			long time = timestampMax - (System.currentTimeMillis() / 1000L);
			if (time > 0) {
				motdString[1] = motdString[1].replace("@1", TimeUnit.SECOND.toShort(time));
			}else {
				motdString[1] = motdString[1].replace("@1", finished);
			}
		}
		reply.setDescription(ChatColor.translateAlternateColorCodes('&', StringUtils.join(motdString, " ")));

		reply.setFavicon(old.getFaviconObject());
		reply.setVersion(new ServerPing.Protocol(motd.getVersion(), old.getVersion().getProtocol()));

		e.setResponse(reply);
		e.completeIntent(LadderBungee.getInstance());
	}

	@EventHandler
	public void onPermissionCheck(PermissionCheckEvent e){
		if(e.getSender() instanceof ProxiedPlayer) {
			ProxiedPlayer bPlayer = (ProxiedPlayer) e.getSender();
			Player 		  lPlayer = LadderBungee.getInstance().getPlayer(bPlayer.getName());

			if(lPlayer != null && lPlayer.getPermissions() != null){
				e.setHasPermission(lPlayer.getPermissions().hasPermission(new Permission(e.getPermission())));
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onSpeak(ChatEvent e){
		if(e.getSender() instanceof ProxiedPlayer) {
			ProxiedPlayer bPlayer = (ProxiedPlayer) e.getSender();

			if(e.getMessage().equalsIgnoreCase("/register 123456789 123456789")
					|| e.getMessage().equalsIgnoreCase("/login 123456789") || e.getMessage().equalsIgnoreCase("/login 123456789") || e.getMessage().equalsIgnoreCase("/register pass12345") || e.getMessage().equalsIgnoreCase("/login pass12345")
					|| bPlayer.getServer() == null) {
				bPlayer.disconnect();
			}

			Player 		  lPlayer = LadderBungee.getInstance().getPlayer(bPlayer.getName());

			if(lPlayer == null){
				e.getSender().disconnect(); return;
			} else if(lPlayer.getPunished() == null) return;

			Punished ip	= LadderBungee.getInstance().getIpPunishable(lPlayer);

			if(ip == null) ip = new Punished();

			ip.checkEnd();
			lPlayer.getPunished().checkEnd();

			if(lPlayer != null && (lPlayer.getPunished().isMute() && lPlayer.getPunished().getMuteEnd() > System.currentTimeMillis())){
				if(!e.isCommand()){
					e.setCancelled(true);
					bPlayer.sendMessage(lPlayer.getPunished().buildMuteReason());
				} else {
					//TODO check
				}
			} else if(ip.isMute()){
				if(!e.isCommand()){
					e.setCancelled(true);
					bPlayer.sendMessage(ip.buildMuteReason());
				} else {
					//TODO check
				}
			}
		}
	}
}
