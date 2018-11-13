package fr.badblock.ladder.http.players;

import java.util.Map;

import com.google.gson.JsonObject;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.ladder.http.LadderPage;

public class PagePlayerIsConnected extends LadderPage{
	public PagePlayerIsConnected() {
		super("/players/isConnected/");
	}

	@Override
	public JsonObject call(Map<String, String> input) {
		JsonObject object = new JsonObject();
		
		if (!input.containsKey("name")) {
			object.addProperty("error", "Aucun pseudo!");
		} else {
			Player player = Ladder.getInstance().getPlayer(input.get("name"));
			object.addProperty("connected", player != null);
		}
		
		return object;
	}
}
