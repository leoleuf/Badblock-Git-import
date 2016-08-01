package fr.badblock.ladder.http.players;

import com.google.gson.JsonObject;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.entities.OfflinePlayer;
import fr.badblock.ladder.http.LadderPage;

public class PageExist extends LadderPage{
	public PageExist() {
		super("/players/exist/");
	}

	@Override
	public JsonObject call(JsonObject input) {
		JsonObject object = new JsonObject();
		
		if(!input.has("name") || !input.get("name").isJsonPrimitive()){
			object.addProperty("error", "Aucun pseudo!");
		} else {
			OfflinePlayer player = Ladder.getInstance().getPlayer(input.get("name").getAsString());
			object.addProperty("exist", player.hasPlayed());
		}
		
		return object;
	}
}
