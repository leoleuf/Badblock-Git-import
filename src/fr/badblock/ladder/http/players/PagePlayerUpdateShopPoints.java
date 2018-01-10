package fr.badblock.ladder.http.players;

import com.google.gson.JsonObject;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.entities.OfflinePlayer;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.ladder.http.LadderPage;

public class PagePlayerUpdateShopPoints extends LadderPage {
	public PagePlayerUpdateShopPoints() {
		super("/players/updateShopPointsData/");
	}

	@Override
	public JsonObject call(JsonObject input) {
		JsonObject object = new JsonObject();
		if (!input.has("name")) {
			object.addProperty("error", "Aucun pseudo!");
		}else if (!input.has("shoppoints")) {
			object.addProperty("error", "Aucun shoppoints!");
		} else {
			OfflinePlayer offlinePlayer = Ladder.getInstance().getOfflinePlayer(input.get("name").getAsString());
			if (offlinePlayer == null) return object;
			offlinePlayer.getData().addProperty("shoppoints", input.get("shoppoints").getAsString());
			offlinePlayer.saveData();
			Player player = Ladder.getInstance().getPlayer(input.get("name").getAsString());
			if (player == null) return object;
			player.sendToBukkit("shoppoints");
			player.sendToBungee("shoppoints");
		}
		return object;
	}
}
