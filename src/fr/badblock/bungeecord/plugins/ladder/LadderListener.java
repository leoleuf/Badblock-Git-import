package fr.badblock.bungeecord.plugins.ladder;

import java.awt.Event;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.Queues;

import fr.badblock.bungeecord.plugins.ladder.bungee.BungeeKeep;
import fr.badblock.bungeecord.plugins.ladder.listeners.BungeePlayerListUpdateListener;
import fr.badblock.bungeecord.plugins.ladder.listeners.ScalerPlayersUpdateListener;
import fr.badblock.bungeecord.plugins.ladder.utils.Motd;
import fr.badblock.bungeecord.plugins.ladder.utils.Punished;
import fr.badblock.bungeecord.plugins.ladder.utils.TimeUnit;
import fr.badblock.bungeecord.plugins.others.haproxy.antibot.AntiBotData;
import fr.badblock.bungeecord.plugins.utils.BungeeUtils;
import fr.badblock.common.commons.utils.StringUtils;
import fr.badblock.common.permissions.Permission;
import fr.badblock.common.protocol.packets.PacketPlayerJoin;
import fr.badblock.common.protocol.packets.PacketPlayerLogin;
import fr.badblock.common.protocol.packets.PacketPlayerPlace;
import fr.badblock.common.protocol.packets.PacketPlayerQuit;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.ServerPing.PlayerInfo;
import net.md_5.bungee.api.connection.PendingConnection;
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
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.event.ServerConnectionFailEvent;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.conf.Configuration;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class LadderListener implements Listener {

	public static long timestampMax = -1;
	public static String finished = "-";
	public static Map<ProxiedPlayer, String> servers = new HashMap<>();

	public static Map<Integer, Queue<Long>> chars = new HashMap<>();
	public static Map<String, Set<String>> usernames = new HashMap<>();

	@EventHandler
	public void onJoin(AsyncDataLoadRequest e){
		String player = e.getPlayer().toLowerCase();
		int count = player.length();

		// Mini anti bot

		if (player.toLowerCase().startsWith("mcbot_"))
		{
			e.getDone().done(new Result(null, ChatColor.RED + "Attaques de bots interdites."), null);
			return;
		}

		// Chars
		Queue<Long> queue = chars.containsKey(count) ? chars.get(count) : Queues.newLinkedBlockingDeque();
		queue.add(System.currentTimeMillis());
		chars.put(count, queue);

		if (queue.size() >= 10)
		{
			long time = System.currentTimeMillis() - queue.poll();
			if (time / queue.size() <= 500)
			{
				BungeeCord bungeeCord = BungeeCord.getInstance();
				Configuration config = bungeeCord.getConfig();
				String address = config.getListeners().iterator().next().getHost().getAddress().getHostAddress();
				AntiBotData.tempBlocks.put(address, System.currentTimeMillis() + 30000);
				e.getDone().done(new Result(null, ChatColor.RED + 
						"Vous avez été bloqué temporairement suite à un comportement de connexion suspect. Réessayez dans cinq minutes. #ID-1"), null);
				return;
			}
		}

		// Multi usernames
		Set<String> q = usernames.containsKey(e.getHandler().getAddress().getAddress().getHostAddress()) ? 
				usernames.get(e.getHandler().getAddress().getAddress().getHostAddress()) : new HashSet<>();
				if (!q.contains(player))
				{
					q.add(player);
				}
				usernames.put(e.getHandler().getAddress().getAddress().getHostAddress(), q);

				if (countSyllables(player) == 0)
				{
					if (q.size() >= 2)
					{
						BungeeCord bungeeCord = BungeeCord.getInstance();
						Configuration config = bungeeCord.getConfig();
						String address = config.getListeners().iterator().next().getHost().getAddress().getHostAddress();
						AntiBotData.tempBlocks.put(address, System.currentTimeMillis() + 30000);
						e.getDone().done(new Result(null, ChatColor.RED + 
								"Vous avez été bloqué temporairement suite à un comportement de connexion suspect! Réessayez dans cinq minutes. #ID-2"), null);
						return;
					}
				}else if (q.size() >= 5)
				{
					BungeeCord bungeeCord = BungeeCord.getInstance();
					Configuration config = bungeeCord.getConfig();
					String address = config.getListeners().iterator().next().getHost().getAddress().getHostAddress();
					AntiBotData.tempBlocks.put(address, System.currentTimeMillis() + 30000);
					e.getDone().done(new Result(null, ChatColor.RED + 
							"Vous avez été bloqué temporairement suite à un comportement de connexion suspect. Réessayez dans cinq minutes. #ID-3"), null);
					return;
				}

				if(e.getPlayer().contains("/") || e.getPlayer().contains("'")){
					e.getDone().done(new Result(null, ChatColor.RED + "Votre pseudonyme est invalide !"), null);
					return;
				}
				for (BungeeKeep bungeeKeep : BungeePlayerListUpdateListener.map.values())
				{
					if (bungeeKeep.isExpired()) continue;
					if (bungeeKeep.players.contains(e.getPlayer().toLowerCase()))
					{
						e.getDone().done(new Result(null, ChatColor.RED + "Vous semblez être déjà connecté sur le serveur sous ce pseudonyme. Code #02"), null);
						break;
					}
				}

				PacketPlayerQuit quitPacket = new PacketPlayerQuit(e.getPlayer().toLowerCase(), null);
				LadderBungee.getInstance().handle(quitPacket);
				LadderBungee.getInstance().getClient().sendPacket(quitPacket);

				System.out.println("Connecting " + e.getPlayer() + " : A");
				PacketPlayerLogin packet = new PacketPlayerLogin(e.getPlayer().toLowerCase(), e.getHandler().getAddress());
				LadderBungee.getInstance().handle(packet, e.getDone());
				LadderBungee.getInstance().getClient().sendPacket(packet);
	}

	protected int countSyllables(String word)
	{
		// TODO: Implement this method so that you can call it from the 
		// getNumSyllables method in BasicDocument (module 1) and 
		// EfficientDocument (module 2).
		int count = 0;
		word = word.toLowerCase();

		if (word.charAt(word.length()-1) == 'e') {
			if (silente(word)){
				String newword = word.substring(0, word.length()-1);
				count = count + countit(newword);
			} else {
				count++;
			}
		} else {
			count = count + countit(word);
		}
		return count;
	}

	private int countit(String word) {
		int count = 0;
		Pattern splitter = Pattern.compile("[^aeiouy]*[aeiouy]+");
		Matcher m = splitter.matcher(word);

		while (m.find()) {
			count++;
		}
		return count;
	}

	private boolean silente(String word) {
		word = word.substring(0, word.length()-1);

		Pattern yup = Pattern.compile("[aeiouy]");
		Matcher m = yup.matcher(word);

		if (m.find()) {
			return true;
		} else
			return false;
	}

	@EventHandler
	public void onJoin(PostLoginEvent e){
		System.out.println("Connecting " + e.getPlayer() + " : D");
		PacketPlayerJoin packet = new PacketPlayerJoin(e.getPlayer().getName().toLowerCase(), e.getPlayer().getName().toLowerCase(), e.getPlayer().getUniqueId(), e.getPlayer().getAddress());
		LadderBungee.getInstance().handle(packet, true);
		LadderBungee.getInstance().getClient().sendPacket(packet);

		if (!LadderBungee.getInstance().uuids.containsKey(e.getPlayer().getName().toLowerCase())) {
			LadderBungee.getInstance().uuids.put(e.getPlayer().getName().toLowerCase(), e.getPlayer().getUniqueId());		
		}
	}

	@EventHandler
	public void onQuit(LoginFailEvent e){
		PacketPlayerQuit packet = new PacketPlayerQuit(e.getHandler().getName().toLowerCase(), null);
		LadderBungee.getInstance().handle(packet, true);
		LadderBungee.getInstance().getClient().sendPacket(packet);
	}

	@EventHandler
	public void onQuit(PlayerDisconnectEvent e){
		PacketPlayerQuit packet = new PacketPlayerQuit(e.getPlayer().getName().toLowerCase(), null);
		LadderBungee.getInstance().handle(packet, true);
		LadderBungee.getInstance().getClient().sendPacket(packet);
	}


	@EventHandler(priority=EventPriority.HIGHEST)
	public void onServerConnected(ServerConnectedEvent e)
	{
		LadderBungee.getInstance().getClient().sendPacket(new PacketPlayerPlace(e.getPlayer().getName().toLowerCase(), e.getServer().getInfo().getName()));
	}

	@EventHandler(priority=EventPriority.HIGHEST)
	public void onServerSwitch(ServerConnectEvent e){
		Player player = LadderBungee.getInstance().getPlayer(e.getPlayer().getName().toLowerCase());
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
			String playerName = e.getPlayer().getName().toLowerCase();
			String server = e.getTarget().getName();

			new Thread(){
				@Override
				public void run(){
					try {
						int i = 0;

						while(LadderBungee.getInstance().getPlayer(player.getName().toLowerCase()) == null){
							Thread.sleep(2L);
							i++;

							if(i == 50) return; // player has disconnected ?
						}

						Thread.sleep(4L);
					} catch (InterruptedException e){
						return;
					} finally {		
						LadderBungee.getInstance().getClient().sendPacket(new PacketPlayerPlace(playerName.toLowerCase(), server));
					}
				}
			}.start();

			return;
		}

	}

	@EventHandler
	public void onFail(ServerConnectionFailEvent e){
		if(!e.isKick()){
			LadderBungee.getInstance().getClient().sendPacket(new PacketPlayerPlace(e.getPlayer().getName().toLowerCase(), e.getFallback().getName()));
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

		motd.setMaxPlayers(LadderBungee.getInstance().slots);
		BungeeCord.getInstance().setPlayerNames(LadderBungee.getInstance().bungeePlayerList);
		BungeeCord.getInstance().setCurrentCount(ScalerPlayersUpdateListener.get());
		reply.setPlayers(new ServerPing.Players(motd.getMaxPlayers(), LadderBungee.getInstance().ladderPlayers, sample));
		String[] motdString = motd.getMotd().clone();
		timestampMax = BungeeUtils.config.getLong("timestampLimit");
		finished = BungeeUtils.config.getString("timestampReachLimit");
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
			Player 		  lPlayer = LadderBungee.getInstance().getPlayer(bPlayer.getName().toLowerCase());

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

			Player 		  lPlayer = LadderBungee.getInstance().getPlayer(bPlayer.getName().toLowerCase());

			if(lPlayer == null){
				e.getSender().disconnect(); return;
			} else if(lPlayer.getPunished() == null) return;
			if (bPlayer.getServer() == null || (bPlayer.getServer() != null && bPlayer.getServer().getInfo() != null && bPlayer.getServer().getInfo().getName() != null && bPlayer.getServer().getInfo().getName().startsWith("login")))
			{
				if (e.getMessage().toLowerCase().contains("mcbot"))
				{
					e.setCancelled(true);
					return;
				}
			}
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
