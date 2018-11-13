package fr.badblock.ladder.http.players;

import java.util.Map;

import com.google.gson.JsonObject;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.chat.ChatColor;
import fr.badblock.ladder.api.entities.Bukkit;
import fr.badblock.ladder.http.LadderPage;

public class PageServerBroadcast extends LadderPage {
	public PageServerBroadcast() {
		super("/server/broadcast/");
	}

	@Override
	public JsonObject call(Map<String, String> input) {
		JsonObject object = new JsonObject();
		
		if (!input.containsKey("server")) {
			object.addProperty("error", "Aucun serveur!");
		} else if (!input.containsKey("message")) {
			object.addProperty("error", "Aucun message!");
		} else {
			Bukkit bukkitServer = Ladder.getInstance().getBukkitServer(input.get("server"));
			bukkitServer.broadcast(ChatColor.replaceColor(input.get("message")));
		}
		
		return object;
	}
}
