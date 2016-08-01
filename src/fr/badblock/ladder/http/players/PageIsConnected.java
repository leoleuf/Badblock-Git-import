package fr.badblock.ladder.http.players;

import com.google.gson.JsonObject;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.ladder.http.LadderPage;

public class PageIsConnected extends LadderPage{
	public PageIsConnected() {
		super("/players/isConnected/");
	}

	@Override
	public JsonObject call(JsonObject input) {
		JsonObject object = new JsonObject();
		
		if(!input.has("name") || !input.get("name").isJsonPrimitive()){
			object.addProperty("error", "Aucun pseudo!");
		} else {
			Player player = Ladder.getInstance().getPlayer(input.get("name").getAsString());
			object.addProperty("connected", player != null);
		}
		
		return object;
	}
}
