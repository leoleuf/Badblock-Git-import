package fr.badblock.ladder.bungee.listeners;

import fr.badblock.bungee.rabbitconnector.RabbitListener;
import fr.badblock.bungee.rabbitconnector.RabbitListenerType;
import fr.badblock.ladder.bungee.LadderBungee;

/**
 * Cet updater permet de gérer les fluctuations de cache avec un multiplier temporaire le temps que les packets
 * en retard arrivent
 * @author root
 *
 */
public class PlayersUpdateListener extends RabbitListener {

	public PlayersUpdateListener() {
		super(LadderBungee.getInstance().rabbitService, "ladder.playersupdater", false, RabbitListenerType.SUBSCRIBER);
	}

	@Override
	public void onPacketReceiving(String body) {
		LadderBungee.getInstance().ladderPlayers = Integer.parseInt(body);
	}
	
}
