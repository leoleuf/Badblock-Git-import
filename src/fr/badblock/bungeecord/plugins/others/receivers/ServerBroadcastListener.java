package fr.badblock.bungeecord.plugins.others.receivers;

import fr.badblock.bungeecord.plugins.others.BadBlockBungeeOthers;
import fr.badblock.common.commons.technologies.rabbitmq.RabbitListener;
import fr.badblock.common.commons.technologies.rabbitmq.RabbitListenerType;
import fr.badblock.common.protocol.utils.StringUtils;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.config.ServerInfo;

public class ServerBroadcastListener extends RabbitListener {
	
	public ServerBroadcastListener() {
		super(BadBlockBungeeOthers.getInstance().getRabbitService(), "serverBroadcast", false, RabbitListenerType.SUBSCRIBER);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onPacketReceiving(String body) {
		String[] splitter = body.split(";");
		if (splitter.length < 2) return;
		String server = splitter[0];
		String rest = StringUtils.join(splitter, " ", 1);
		ServerInfo serverInfo = BungeeCord.getInstance().getServerInfo(server);
		if (serverInfo != null)
			serverInfo.getPlayers().forEach(player -> player.sendMessage(ChatColor.translateAlternateColorCodes('&', rest)));
	}



}
