package fr.badblock.bungeecord.plugins.ladder.listeners;

import fr.badblock.bungeecord.plugins.ladder.LadderBungee;
import fr.badblock.common.commons.technologies.rabbitmq.RabbitListener;
import fr.badblock.common.commons.technologies.rabbitmq.RabbitListenerType;
import net.md_5.bungee.BungeeCord;

public class BungeePlayersUpdateListener extends RabbitListener {
	
	public BungeePlayersUpdateListener() {
		super(LadderBungee.getInstance().rabbitService, "ladder.playersupdaterBungee." + LadderBungee.getInstance().countEnvironment, false, RabbitListenerType.SUBSCRIBER);
	}

	@Override
	public void onPacketReceiving(String body) {
		int l = LadderBungee.getInstance().bungeePlayerCount;
		int o = (int) (Double.parseDouble(body) * BungeeCord.getInstance().getPlayers().stream().filter(player -> !player.getName().contains("-") && player.getServer() != null && player.getServer().getInfo() != null && !player.getServer().getInfo().getName().startsWith("login")).count());
		if (o - l > 100)
		{
			return;
		}
		LadderBungee.getInstance().bungeePlayerCount = o;
	}
	
}

