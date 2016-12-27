package fr.badblock.bungee.rabbit.listeners;

import fr.badblock.bungee.BadBungee;
import fr.badblock.commons.technologies.rabbitmq.RabbitListener;
import fr.badblock.commons.technologies.rabbitmq.RabbitListenerType;

public class RabbitBungeeHelloWorldListener extends RabbitListener {

	public static boolean done = false;
	
	public RabbitBungeeHelloWorldListener() {
		super(BadBungee.getInstance().getRabbitService(), "bungee.worker.helloWorld", false, RabbitListenerType.SUBSCRIBER);
	}

	@Override
	public void onPacketReceiving(String body) {
		BadBungee.getInstance().keepAlive();
	}
	
}
