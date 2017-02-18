package fr.badblock.bungee.listeners;

import java.util.UUID;

import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.bobjects.Motd;
import fr.badblock.commons.utils.StringUtils;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.ServerPing.PlayerInfo;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ProxyPingListener implements Listener {

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onProxyPing(ProxyPingEvent event) {
		BadBungee badBungee = BadBungee.getInstance();
		event.registerIntent(badBungee);

		Motd 		   motd   = badBungee.getMotd();
		if (motd == null) {
			return;
		}
		if (badBungee.getOnlineCount() >= motd.getMaxPlayers()) {
			motd = badBungee.getFullMotd();
		}

		ServerPing     old    = event.getResponse();
		event.setResponse(null);
		ServerPing     reply  = new ServerPing();

		if(motd == null)
			return;


		PlayerInfo[]   sample = new PlayerInfo[motd.getPlayers().length];

		for(int i=0;i<sample.length;i++){
			sample[i] = new PlayerInfo(ChatColor.translateAlternateColorCodes('&', motd.getPlayers()[i]), UUID.randomUUID());
		}

		reply.setPlayers(new ServerPing.Players(motd.getMaxPlayers(), badBungee.getOnlineCount(), sample));
		String[] motdString = motd.getMotd().clone();

		reply.setDescription(ChatColor.translateAlternateColorCodes('&', StringUtils.join(motdString, " ")));

		reply.setFavicon(old.getFaviconObject());
		reply.setVersion(new ServerPing.Protocol(motd.getVersion(), old.getVersion().getProtocol()));

		event.setResponse(reply);
		event.completeIntent(badBungee);
	}

}
