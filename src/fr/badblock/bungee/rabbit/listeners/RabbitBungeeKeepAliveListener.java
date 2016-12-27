package fr.badblock.bungee.rabbit.listeners;

import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.Bungee;
import fr.badblock.bungee.utils.BungeeUtils;
import fr.badblock.commons.technologies.rabbitmq.RabbitListener;
import fr.badblock.commons.technologies.rabbitmq.RabbitListenerType;

public class RabbitBungeeKeepAliveListener extends RabbitListener {

	public static boolean done = false;
	
	public RabbitBungeeKeepAliveListener() {
		super(BadBungee.getInstance().getRabbitService(), "bungee.worker.keepAlive", false, RabbitListenerType.SUBSCRIBER);
	}

	@Override
	public void onPacketReceiving(String body) {
		BadBungee instance = BadBungee.getInstance();
		Bungee bungee = instance.getExposeGson().fromJson(body, BadBungee.bungeeType);
		if (bungee == null) return;
		if (!BungeeUtils.bungees.containsKey(bungee.getBungeeName())) {
			System.out.println("[BadBungee] Registered " + bungee.getBungeeName());
		}
		BungeeUtils.bungees.put(bungee.getBungeeName(), bungee);
		done = true;
	}
	
}
