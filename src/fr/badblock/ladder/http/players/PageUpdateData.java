package fr.badblock.ladder.http.players;

import com.google.gson.JsonObject;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.entities.OfflinePlayer;
import fr.badblock.ladder.http.LadderPage;

public class PageUpdateData extends LadderPage {
	public PageUpdateData() {
		super("/players/updateData/");
	}

	@Override
	public JsonObject call(JsonObject input) {
		JsonObject object = new JsonObject();
		
		if(!input.has("name") || !input.get("name").isJsonPrimitive()){
			object.addProperty("error", "Aucun pseudo!");
		} else if(!input.has("data") || !input.get("data").isJsonObject()){
			object.addProperty("error", "Aucune data!");
		} else {
			OfflinePlayer player = Ladder.getInstance().getOfflinePlayer(input.get("name").getAsString());
			object.add("data", player.getData());
		}
		
		return object;
	}
}
