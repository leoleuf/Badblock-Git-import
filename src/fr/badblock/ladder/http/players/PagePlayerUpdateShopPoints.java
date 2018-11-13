package fr.badblock.ladder.http.players;

import java.util.Map;

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
	public JsonObject call(Map<String, String> input) {
		JsonObject object = new JsonObject();
		if (!input.containsKey("name")) {
			object.addProperty("error", "Aucun pseudo!");
		}else if (!input.containsKey("shoppoints")) {
			object.addProperty("error", "Aucun shoppoints!");
		} else {
			OfflinePlayer offlinePlayer = Ladder.getInstance().getOfflinePlayer(input.get("name"));
			if (offlinePlayer == null) return object;
			offlinePlayer.getData().addProperty("shoppoints", input.get("shoppoints"));
			offlinePlayer.saveData();
			Player player = Ladder.getInstance().getPlayer(input.get("name"));
			if (player == null) return object;
			player.sendToBukkit("shoppoints");
			player.sendToBungee("shoppoints");
		}
		return object;
	}
}
