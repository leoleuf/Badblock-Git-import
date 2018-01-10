package fr.badblock.ladder.http.players;

import com.google.gson.JsonObject;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.ladder.http.LadderPage;

public class PagePlayerGetConnectedServer extends LadderPage {
	public PagePlayerGetConnectedServer() {
		super("/players/getConnectedServer/");
	}

	@Override
	public JsonObject call(JsonObject input) {
		JsonObject object = new JsonObject();
		
		if (!input.has("name")) {
			object.addProperty("error", "Aucun pseudo!");
		} else {
			Player player = Ladder.getInstance().getPlayer(input.get("name").getAsString());
			if (player == null) object.addProperty("server", "null");
			else object.addProperty("server", player.getBukkitServer().getName());
		}
		
		return object;
	}
}
