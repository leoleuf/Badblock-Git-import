package fr.badblock.ladder.http.players;

import com.google.gson.JsonObject;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.chat.ChatColor;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.ladder.http.LadderPage;

public class PageSendMessage extends LadderPage {
	public PageSendMessage() {
		super("/players/sendMessage/");
	}

	@Override
	public JsonObject call(JsonObject input) {
		JsonObject object = new JsonObject();
		
		if(!input.has("name") || !input.get("name").isJsonPrimitive()){
			object.addProperty("error", "Aucun pseudo!");
		} else if(!input.has("message") || !input.get("message").isJsonPrimitive()){
			object.addProperty("error", "Aucun message!");
		} else {
			Player player = Ladder.getInstance().getPlayer(input.get("name").getAsString());
			player.sendMessage(ChatColor.replaceColor(object.get("message").getAsString()));
		}
		
		return object;
	}
}
