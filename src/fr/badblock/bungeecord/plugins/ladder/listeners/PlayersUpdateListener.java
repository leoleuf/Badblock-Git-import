package fr.badblock.bungeecord.plugins.ladder.listeners;

import fr.badblock.bungeecord.plugins.ladder.LadderBungee;
import fr.badblock.common.commons.technologies.rabbitmq.RabbitListener;
import fr.badblock.common.commons.technologies.rabbitmq.RabbitListenerType;

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
