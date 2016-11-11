package fr.badblock.ladder.bungee;

import java.util.UUID;

import fr.badblock.ladder.bungee.utils.Motd;
import fr.badblock.ladder.bungee.utils.Punished;
import fr.badblock.permissions.Permission;
import fr.badblock.protocol.packets.PacketPlayerJoin;
import fr.badblock.protocol.packets.PacketPlayerLogin;
import fr.badblock.protocol.packets.PacketPlayerPlace;
import fr.badblock.protocol.packets.PacketPlayerQuit;
import fr.badblock.protocol.utils.StringUtils;
import fr.badblock.skins.SkinFactoryBungee;
import fr.badblock.skins.format.SkinProfile;
import fr.badblock.skins.storage.SkinStorage;
import fr.badblock.skins.utils.SkinFetchUtils.SkinFetchFailedException;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.ServerPing.PlayerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.AsyncDataLoadRequest;
import net.md_5.bungee.api.event.AsyncDataLoadRequest.Result;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.LoginFailEvent;
import net.md_5.bungee.api.event.PermissionCheckEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerConnectionFailEvent;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.connection.InitialHandler;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class LadderListener implements Listener {
	
	@EventHandler
	public void onJoin(AsyncDataLoadRequest e){
		if(e.getPlayer().contains("/")){
			e.getDone().done(new Result(null, ChatColor.RED + "Votre pseudonyme est invalide !"), null);
			return;
		}

		if(LadderBungee.getInstance().getPlayer(e.getPlayer()) != null){
			e.getDone().done(new Result(null, ChatColor.RED + "Vous êtes déjà connecté sur BadBlock !"), null);
			return;
		}

		PacketPlayerLogin packet = new PacketPlayerLogin(e.getPlayer(), e.getHandler().getAddress());
		LadderBungee.getInstance().handle(packet, e.getDone());
		LadderBungee.getInstance().getClient().sendPacket(packet);
	}

	@EventHandler
	public void onJoin(PostLoginEvent e){
		Player player = LadderBungee.getInstance().getPlayer(e.getPlayer().getName());
		PacketPlayerJoin packet = new PacketPlayerJoin(e.getPlayer().getName(), player == null ? e.getPlayer().getName() : player.getNickNamee(), e.getPlayer().getUniqueId(), e.getPlayer().getAddress());
		LadderBungee.getInstance().handle(packet, true);
		LadderBungee.getInstance().getClient().sendPacket(packet);
		
		final SkinProfile skinprofile = SkinStorage.getInstance().getOrCreateSkinData(e.getPlayer().getName().toLowerCase());
		
		ProxyServer.getInstance().getScheduler().runAsync(LadderBungee.getInstance(), new Runnable() {
			@Override
			public void run() {
				try {
					skinprofile.attemptUpdateBungee();
					SkinFactoryBungee.getFactory().applySkin(e.getPlayer());
				} catch (SkinFetchFailedException e){}
			}
		});
	}

	@EventHandler
	public void onQuit(LoginFailEvent e){
		if(e.getHandler().getUniqueId() == null) return;

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
			InitialHandler handler = (InitialHandler) e.getPlayer().getPendingConnection();
			handler.getLoginRequest().setData(player.getNickNamee());
		}
		if(e.getPlayer().getServer() == null){
			UUID uniqueId = e.getPlayer().getUniqueId();
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
						LadderBungee.getInstance().getClient().sendPacket(new PacketPlayerPlace(uniqueId, server));
					}
				}
			}.start();

			return;
		}

		LadderBungee.getInstance().getClient().sendPacket(new PacketPlayerPlace(e.getPlayer().getUniqueId(), e.getTarget().getName()));
	}

	@EventHandler
	public void onFail(ServerConnectionFailEvent e){
		if(!e.isKick()){
			LadderBungee.getInstance().getClient().sendPacket(new PacketPlayerPlace(e.getPlayer().getUniqueId(), e.getFallback().getName()));
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
		ServerPing     reply  = new ServerPing();

		if(motd == null)
			return;

		PlayerInfo[]   sample = new PlayerInfo[motd.getPlayers().length];

		for(int i=0;i<sample.length;i++){
			sample[i] = new PlayerInfo(ChatColor.translateAlternateColorCodes('&', motd.getPlayers()[i]), UUID.randomUUID());
		}

		reply.setPlayers(new ServerPing.Players(motd.getMaxPlayers(), LadderBungee.getInstance().ladderPlayers, sample));
		reply.setDescription(ChatColor.translateAlternateColorCodes('&', StringUtils.join(motd.getMotd(), " ")));
		String[] motdString = motd.getMotd().clone();
		if (LadderBungee.getInstance().ladderPlayers >= motd.getMaxPlayers()) {
			motdString[1] = LadderBungee.getInstance().fullMotd;
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

			if(lPlayer != null && lPlayer.getPunished().isMute()){
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
