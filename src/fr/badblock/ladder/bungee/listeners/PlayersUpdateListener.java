package fr.badblock.ladder.bungee.listeners;

import fr.badblock.ladder.bungee.LadderBungee;
import fr.badblock.rabbitconnector.RabbitListener;
import fr.badblock.rabbitconnector.RabbitListenerType;

public class PlayersUpdateListener extends RabbitListener {

	public PlayersUpdateListener() {
		super(LadderBungee.getInstance().getRabbitService(), "ladder.playersupdate", false, RabbitListenerType.SUBSCRIBER);
	}

	@Override
	public void onPacketReceiving(String body) {
		LadderBungee.getInstance().setLadderPlayers(Integer.parseInt(body));
	}

	
	
}
