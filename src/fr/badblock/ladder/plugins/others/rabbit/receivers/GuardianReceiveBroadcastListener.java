package fr.badblock.ladder.plugins.others.rabbit.receivers;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.chat.ChatColor;
import fr.badblock.rabbitconnector.RabbitConnector;
import fr.badblock.rabbitconnector.RabbitListener;
import fr.badblock.rabbitconnector.RabbitListenerType;

public class GuardianReceiveBroadcastListener extends RabbitListener {

	public GuardianReceiveBroadcastListener() {
		super(RabbitConnector.getInstance().getService("default"), "guardian.broadcast", false,
				RabbitListenerType.MESSAGE_BROKER);
	}

	@Override
	public void onPacketReceiving(String string) {
		Ladder.getInstance().broadcast(ChatColor.translateAlternateColorCodes('&', string));
	}

}
