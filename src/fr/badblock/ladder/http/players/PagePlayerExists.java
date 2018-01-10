package fr.badblock.ladder.http.players;

import com.google.gson.JsonObject;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.entities.OfflinePlayer;
import fr.badblock.ladder.http.LadderPage;

public class PagePlayerExists extends LadderPage{
	public PagePlayerExists() {
		super("/players/exists/");
	}

	@Override
	public JsonObject call(JsonObject input) {
		JsonObject object = new JsonObject();
		
		if (!input.has("name")) {
			object.addProperty("error", "Aucun pseudo!");
		} else {
			OfflinePlayer player = Ladder.getInstance().getOfflinePlayer(input.get("name").getAsString());
			object.addProperty("exist", player.hasPlayed());
		}
		
		return object;
	}
}
