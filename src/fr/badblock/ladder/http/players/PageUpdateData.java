package fr.badblock.ladder.http.players;

import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.entities.OfflinePlayer;
import fr.badblock.ladder.http.LadderHttpHandler;
import fr.badblock.ladder.http.LadderPage;

public class PageUpdateData extends LadderPage {
	public PageUpdateData() {
		super("/players/updateData/");
	}

	@Override
	public JsonObject call(Map<String, String> input) {
		JsonObject object = new JsonObject();
		
		if (!input.containsKey("name")) {
			object.addProperty("error", "Aucun pseudo!");
		} else {
			OfflinePlayer player = Ladder.getInstance().getOfflinePlayer(input.get("name"));
			
			input.entrySet().forEach(entry -> player.getData().add(entry.getKey(), LadderHttpHandler.gson.fromJson(entry.getValue(), JsonElement.class)));
			object.add("data", player.getData());
		}
		
		return object;
	}
}
