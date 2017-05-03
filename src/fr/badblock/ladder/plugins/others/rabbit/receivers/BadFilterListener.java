package fr.badblock.ladder.plugins.others.rabbit.receivers;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.ladder.plugins.others.friends.FriendPlayer;
import fr.badblock.rabbitconnector.RabbitConnector;
import fr.badblock.rabbitconnector.RabbitListener;
import fr.badblock.rabbitconnector.RabbitListenerType;

public class BadFilterListener extends RabbitListener {

	public BadFilterListener() {
		super(RabbitConnector.getInstance().getService("default"), "badfilter", false, RabbitListenerType.SUBSCRIBER);
	}

	@Override
	public void onPacketReceiving(String string) {
		work(string);
	}
	
	public static void work(String string) {
		for (Player player : Ladder.getInstance().getOnlinePlayers())
			if (player.getBukkitServer() != null && player.getBukkitServer().getName() != null && !player.getBukkitServer().getName().startsWith("login") && player.hasPermission("others.badfilter")) {
				FriendPlayer fPlayer = FriendPlayer.get(player);
				if (fPlayer == null) continue;
				if (!fPlayer.badfilter) continue;
				player.sendMessage("§4[BF] §7" + string);
			}
	}

}
