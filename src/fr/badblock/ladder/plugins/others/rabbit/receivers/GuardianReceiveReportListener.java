package fr.badblock.ladder.plugins.others.rabbit.receivers;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.chat.RawMessage;
import fr.badblock.ladder.api.chat.RawMessage.ClickEventType;
import fr.badblock.ladder.api.chat.RawMessage.HoverEventType;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.ladder.plugins.others.guardian.GuardianReport;
import fr.badblock.rabbitconnector.RabbitConnector;
import fr.badblock.rabbitconnector.RabbitListener;
import fr.badblock.rabbitconnector.RabbitListenerType;

public class GuardianReceiveReportListener extends RabbitListener {

	public GuardianReceiveReportListener() {
		super(RabbitConnector.getInstance().getService("default"), "guardian.report", false,
				RabbitListenerType.MESSAGE_BROKER);
	}

	@Override
	public void onPacketReceiving(String string) {
		GuardianReport guardianReport = new Gson().fromJson(string, GuardianReport.class);
		String server = "Inconnu";
		Player player = Ladder.getInstance().getPlayer(guardianReport.getUniqueId());
		if (player != null)
			server = player.getBukkitServer().getName();
		RawMessage component = getMessage(guardianReport.getMessage().replace("[SERVER]", server),
				"§aCliquez pour vous téléporter au joueur" + (player != null ? " §c" + player.getName() : ""));
		component.setClickEvent(ClickEventType.RUN_COMMAND, false, "/gconnect " + player.getName());
		Ladder.getInstance().getConsoleCommandSender()
				.sendMessage(guardianReport.getMessage().replace("[SERVER]", server));
		Ladder.getInstance().getOnlinePlayers()
				.parallelStream().filter(pl -> pl.getBukkitServer() != null
						&& !pl.getBukkitServer().getName().startsWith("login") && pl.hasPermission("guardian.modo"))
				.forEach(pl -> component.send(pl));
	}

	public RawMessage getMessage(String message, String lore) {
		// Envoi du message au joueur
		RawMessage rawMessage = Ladder.getInstance().createRawMessage(message);
		// Ajout du lore
		if (!"".equals(lore)) {
			String[] lines = lore.split("\n");
			List<String> components = new ArrayList<>();
			for (String line : lines) {
				components.add(line);
			}
			String[] arr = new String[] {};
			arr = components.toArray(arr);
			rawMessage.setHoverEvent(HoverEventType.SHOW_TEXT, false, arr);
		}
		return rawMessage;
	}

}
