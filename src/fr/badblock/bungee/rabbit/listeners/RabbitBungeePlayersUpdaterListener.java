package fr.badblock.bungee.rabbit.listeners;

import fr.badblock.bungee.BadBungee;
import fr.badblock.commons.technologies.rabbitmq.RabbitListener;
import fr.badblock.commons.technologies.rabbitmq.RabbitListenerType;

public class RabbitBungeePlayersUpdaterListener extends RabbitListener {

	public RabbitBungeePlayersUpdaterListener() {
		super(BadBungee.getInstance().getRabbitService(), "bungee.worker.playersupdater", false, RabbitListenerType.SUBSCRIBER);
	}

	@Override
	public void onPacketReceiving(String body) {
		BadBungee.getInstance().setOnlineCount(Integer.parseInt(body));
	}
	
}
