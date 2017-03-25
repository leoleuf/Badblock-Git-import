package fr.badblock.ladder.plugins.others.rabbit.receivers;

import java.util.Date;

import com.google.gson.Gson;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.ladder.plugins.others.BadBlockOthers;
import fr.badblock.ladder.plugins.others.database.BadblockDatabase;
import fr.badblock.ladder.plugins.others.database.Request;
import fr.badblock.ladder.plugins.others.database.Request.RequestType;
import fr.badblock.ladder.plugins.others.guardian.GuardianKick;
import fr.badblock.rabbitconnector.RabbitConnector;
import fr.badblock.rabbitconnector.RabbitListener;
import fr.badblock.rabbitconnector.RabbitListenerType;

public class GuardianReceiveKickListener extends RabbitListener {

	public GuardianReceiveKickListener() {
		super(RabbitConnector.getInstance().getService("default"), "guardian.kick", false,
				RabbitListenerType.MESSAGE_BROKER);
	}

	@Override
	public void onPacketReceiving(String string) {
		GuardianKick kick = new Gson().fromJson(string, GuardianKick.class);
		Player proxiedPlayer = Ladder.getInstance().getPlayer(kick.getUniqueId());
		if (proxiedPlayer != null) {
			BadblockDatabase.getInstance().addRequest(new Request(
					"INSERT INTO sanctions(pseudo, ip, type, expire, timestamp, date, reason, banner, fromIp) "
							+ "VALUES('"
							+ BadblockDatabase.getInstance().mysql_real_escape_string(proxiedPlayer.getName()) + "', '"
							+ BadblockDatabase.getInstance()
									.mysql_real_escape_string(proxiedPlayer.getAddress().getAddress().getHostAddress())
							+ "', 'kick', '-1', '" + System.currentTimeMillis() + "', '"
							+ BadblockDatabase.getInstance().mysql_real_escape_string(
									BadBlockOthers.getInstance().simpleDateFormat.format(new Date()))
							+ "', '" + BadblockDatabase.getInstance().mysql_real_escape_string(kick.getReason())
							+ "', 'Guardian', '127.0.0.1')",
					RequestType.SETTER));
			proxiedPlayer.disconnect(kick.getMessage());
		}
	}

}
