package fr.badblock.bungeecord.plugins.others.receivers;

import fr.badblock.bungeecord.BungeeCord;
import fr.badblock.bungeecord.api.ChatColor;
import fr.badblock.bungeecord.api.connection.ProxiedPlayer;
import fr.badblock.bungeecord.plugins.others.BadBlockBungeeOthers;
import fr.badblock.commons.technologies.rabbitmq.RabbitListener;
import fr.badblock.commons.technologies.rabbitmq.RabbitListenerType;
import fr.badblock.protocol.utils.StringUtils;

public class PlayerMessageListener extends RabbitListener {
	
	public PlayerMessageListener() {
		super(BadBlockBungeeOthers.getInstance().getRabbitService(), "playerMessage", false, RabbitListenerType.SUBSCRIBER);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onPacketReceiving(String body) {
		String[] splitter = body.split(";");
		if (splitter.length < 2) return;
		String rest = StringUtils.join(splitter, " ", 1);
		ProxiedPlayer player = BungeeCord.getInstance().getPlayer(splitter[0]);
		if (player == null) return;
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', rest));
	}

}
