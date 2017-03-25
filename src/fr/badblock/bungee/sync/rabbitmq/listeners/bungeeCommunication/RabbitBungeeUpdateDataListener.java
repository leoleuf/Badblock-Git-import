package fr.badblock.bungee.sync.rabbitmq.listeners.bungeeCommunication;

import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.data.players.BadPlayer;
import fr.badblock.commons.technologies.rabbitmq.RabbitListener;
import fr.badblock.commons.technologies.rabbitmq.RabbitListenerType;

public class RabbitBungeeUpdateDataListener extends RabbitListener {
	
	public RabbitBungeeUpdateDataListener() {
		super(BadBungee.getInstance().getRabbitService(), "bungee.worker.updateData", false, RabbitListenerType.SUBSCRIBER);
	}

	@Override
	public void onPacketReceiving(String body) {
		BadPlayer badPlayer = BadBungee.getInstance().getExposeGson().fromJson(body, BadBungee.playerType);
		if (badPlayer == null) return;
		BadPlayer currentPlayer = BadBungee.getInstance().get(badPlayer.getName());
		if (currentPlayer == null) return;
		currentPlayer.updateDataFromClone(badPlayer);
		currentPlayer.updateData();
	}
	
}
