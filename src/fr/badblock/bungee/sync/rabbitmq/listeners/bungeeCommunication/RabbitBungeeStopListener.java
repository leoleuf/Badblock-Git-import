package fr.badblock.bungee.sync.rabbitmq.listeners.bungeeCommunication;

import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.utils.BungeeUtils;
import fr.badblock.commons.technologies.rabbitmq.RabbitListener;
import fr.badblock.commons.technologies.rabbitmq.RabbitListenerType;

public class RabbitBungeeStopListener extends RabbitListener {

	public RabbitBungeeStopListener() {
		super(BadBungee.getInstance().getRabbitService(), "bungee.worker.stop", false, RabbitListenerType.SUBSCRIBER);
	}

	@Override
	public void onPacketReceiving(String body) {
		System.out.println("[BadBungee] Unregistered " + body);
		BungeeUtils.bungees.remove(body);
	}

}
