package fr.badblock.bungeecord.plugins.others.receivers;

import fr.badblock.bungeecord.plugins.others.BadBlockBungeeOthers;
import fr.badblock.common.commons.technologies.rabbitmq.RabbitListener;
import fr.badblock.common.commons.technologies.rabbitmq.RabbitListenerType;
import fr.badblock.common.protocol.utils.StringUtils;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;

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
