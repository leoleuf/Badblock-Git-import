package fr.badblock.bungeecord.plugins.others.receivers;

import fr.badblock.bungeecord.BungeeCord;
import fr.badblock.bungeecord.api.ChatColor;
import fr.badblock.bungeecord.plugins.others.BadBlockBungeeOthers;
import fr.badblock.common.commons.technologies.rabbitmq.RabbitListener;
import fr.badblock.common.commons.technologies.rabbitmq.RabbitListenerType;
import fr.badblock.common.protocol.utils.StringUtils;

public class PermissionMessageListener extends RabbitListener {
	
	public PermissionMessageListener() {
		super(BadBlockBungeeOthers.getInstance().getRabbitService(), "permissionMessage", false, RabbitListenerType.SUBSCRIBER);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onPacketReceiving(String body) {
		String[] splitter = body.split(";");
		if (splitter.length < 2) return;
		String rest = StringUtils.join(splitter, " ", 1);
		BungeeCord.getInstance().getPlayers().parallelStream().filter(player -> player.hasPermission(splitter[0])).forEach(player -> player.sendMessage(ChatColor.translateAlternateColorCodes('&', rest)));
	}

}
