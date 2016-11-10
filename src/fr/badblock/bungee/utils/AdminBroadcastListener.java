package fr.badblock.bungee.utils;

import fr.badblock.bungee.rabbitconnector.RabbitListener;
import fr.badblock.bungee.rabbitconnector.RabbitListenerType;
import fr.badblock.ladder.bungee.LadderBungee;
import net.md_5.bungee.BungeeCord;

public class AdminBroadcastListener extends RabbitListener {

	public AdminBroadcastListener() {
		super(LadderBungee.getInstance().rabbitService, "admin_broadcast", false, RabbitListenerType.SUBSCRIBER);
	}

	@Override
	public void onPacketReceiving(String body) {
		BungeeCord.getInstance().broadcast(body);
	}
	
}
