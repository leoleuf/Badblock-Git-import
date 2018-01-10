package fr.badblock.ladder.http.players;

import com.google.gson.JsonObject;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.chat.ChatColor;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.ladder.http.LadderPage;

public class PagePlayerSendMessage extends LadderPage {
	public PagePlayerSendMessage() {
		super("/players/sendMessage/");
	}

	@Override
	public JsonObject call(JsonObject input) {
		JsonObject object = new JsonObject();
		if (!input.has("name")) {
			object.addProperty("error", "Aucun pseudo!");
		} else if (!input.has("message")) {
			object.addProperty("error", "Aucun message!");
		} else {
			Player player = Ladder.getInstance().getPlayer(input.get("name").getAsString());
			if (player == null) return object;
			
			String string = input.get("message").getAsString();
			string = ChatColor.replaceColor(string);
			player.sendMessage(string);
		}

		return object;
	}
}
