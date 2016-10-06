package fr.badblock.ladder.http.players;

import java.util.Map;

import com.google.gson.JsonObject;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.entities.OfflinePlayer;
import fr.badblock.ladder.http.LadderPage;

public class PageIsConnected extends LadderPage{
	public PageIsConnected() {
		super("/players/isConnected/");
	}

	@Override
	public JsonObject call(Map<String, String> input) {
		JsonObject object = new JsonObject();
		
		if (!input.containsKey("name")) {
			object.addProperty("error", "Aucun pseudo!");
		} else {
			OfflinePlayer player = Ladder.getInstance().getOfflinePlayer(input.get("name"));
			object.addProperty("connected", player != null);
		}
		
		return object;
	}
}
