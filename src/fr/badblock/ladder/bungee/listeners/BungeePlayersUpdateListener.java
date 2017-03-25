package fr.badblock.ladder.bungee.listeners;

import fr.badblock.bungee.rabbitconnector.RabbitListener;
import fr.badblock.bungee.rabbitconnector.RabbitListenerType;
import fr.badblock.ladder.bungee.LadderBungee;
import net.md_5.bungee.BungeeCord;

public class BungeePlayersUpdateListener extends RabbitListener {
	
	public BungeePlayersUpdateListener() {
		super(LadderBungee.getInstance().rabbitService, "ladder.playersupdaterBungee", false, RabbitListenerType.SUBSCRIBER);
	}

	@Override
	public void onPacketReceiving(String body) {
		LadderBungee.getInstance().bungeePlayerCount = (int) (Double.parseDouble(body) * BungeeCord.getInstance().getPlayers().stream().filter(player -> !player.getName().contains("-") && player.getServer() != null && player.getServer().getInfo() != null && !player.getServer().getInfo().getName().startsWith("login")).count());
	}
	
}

