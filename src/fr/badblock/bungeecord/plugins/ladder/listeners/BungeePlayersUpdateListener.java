package fr.badblock.bungeecord.plugins.ladder.listeners;

import fr.badblock.bungeecord.plugins.ladder.LadderBungee;
import fr.badblock.common.commons.technologies.rabbitmq.RabbitListener;
import fr.badblock.common.commons.technologies.rabbitmq.RabbitListenerType;
import net.md_5.bungee.BungeeCord;

public class BungeePlayersUpdateListener extends RabbitListener {
	
	private boolean first = true;
	
	public BungeePlayersUpdateListener() {
		super(LadderBungee.getInstance().rabbitService, "ladder.playersupdaterBungee." + LadderBungee.getInstance().countEnvironment, false, RabbitListenerType.SUBSCRIBER);
	}

	@Override
	public void onPacketReceiving(String body) {
		int count = (int) (Double.parseDouble(body) * BungeeCord.getInstance().getPlayers().stream().filter(player -> !player.getName().contains("-") && player.getServer() != null && player.getServer().getInfo() != null && !player.getServer().getInfo().getName().startsWith("login")).count());
		if (!first)
		{
			int difference = count - LadderBungee.getInstance().bungeePlayerCount;
			if (difference > 100)
			{
				return;
			}
		}
		else
		{
			first = false;
		}
		LadderBungee.getInstance().bungeePlayerCount = count;
	}
	
}

