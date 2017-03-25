package fr.badblock.bungeecord.ladder.listeners;

import fr.badblock.bungeecord.ladder.LadderBungee;
import fr.badblock.common.commons.technologies.rabbitmq.RabbitListener;
import fr.badblock.common.commons.technologies.rabbitmq.RabbitListenerType;

public class ScalerPlayersUpdateListener extends RabbitListener {

	public static double percentil = 1;
	
	public ScalerPlayersUpdateListener() {
		super(LadderBungee.getInstance().rabbitService, "ladder.playersupdaterBungeeScaler", false, RabbitListenerType.SUBSCRIBER);
	}

	private static boolean ok = false;
	
	@Override
	public void onPacketReceiving(String body) {
		percentil = Double.parseDouble(body);
		if (!ok) {
			ok = true;
			System.out.println("Scale percentile!");
		}
	}

	/**
	 * Get sur un scaler pour vérifier
	 * @return
	 */
	public static int get() {
		if (ScalerPlayersUpdateListener.percentil > 1.0d) ScalerPlayersUpdateListener.percentil = 1.0d; // pour éviter l'inflation
		return (int) (ScalerPlayersUpdateListener.percentil * LadderBungee.getInstance().bungeePlayerCount);
	}
	
}

