package fr.badblock.ladder.http.players;

import java.util.Map;

import com.google.gson.JsonObject;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.chat.ChatColor;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.ladder.http.LadderPage;

public class PagePlayerSendServerMessage extends LadderPage {
	public PagePlayerSendServerMessage() {
		super("/players/sendServerMessage/");
	}

	@Override
	public JsonObject call(Map<String, String> input) {
		JsonObject object = new JsonObject();
		if (!input.containsKey("name")) {
			object.addProperty("error", "Aucun pseudo!");
		} else if (!input.containsKey("message")) {
			object.addProperty("error", "Aucun message!");
		} else {
			Player player = Ladder.getInstance().getPlayer(input.get("name"));
			if (player == null || player.getBukkitServer() == null) return object;
			player.getBukkitServer().broadcast(ChatColor.replaceColor(input.get("message")));
		}
		
		return object;
	}
}
