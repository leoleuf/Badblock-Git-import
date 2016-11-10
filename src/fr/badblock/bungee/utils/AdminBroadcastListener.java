package fr.badblock.bungee.utils;

import fr.badblock.bungee.rabbitconnector.RabbitListener;
import fr.badblock.bungee.rabbitconnector.RabbitListenerType;
import fr.badblock.ladder.bungee.LadderBungee;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;

public class AdminBroadcastListener extends RabbitListener {

	public AdminBroadcastListener() {
		super(LadderBungee.getInstance().rabbitService, "admin.broadcast", false, RabbitListenerType.SUBSCRIBER);
	}

	@Override
	public void onPacketReceiving(String body) {
		BungeeCord.getInstance().broadcast(ChatColor.translateAlternateColorCodes('&', body));
	}
	
}
