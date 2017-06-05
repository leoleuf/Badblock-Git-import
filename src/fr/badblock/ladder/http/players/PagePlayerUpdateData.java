package fr.badblock.ladder.http.players;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.entities.OfflinePlayer;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.ladder.http.LadderPage;

public class PagePlayerUpdateData extends LadderPage {
	public PagePlayerUpdateData() {
		super("/players/updateData/");
	}

	@Override
	public JsonObject call(Map<String, String> input) {
		JsonObject object = new JsonObject();
		if (!input.containsKey("name")) {
			object.addProperty("error", "Aucun pseudo!");
		} else {
			object.addProperty("name", input.get("name"));
			OfflinePlayer player = Ladder.getInstance().getOfflinePlayer(input.get("name"));
			input.entrySet().stream().filter(entry -> !entry.getKey().equals("name")).forEach(entry -> {
				JsonParser parser = new JsonParser();
				JsonElement jsonObject = parser.parse(entry.getValue());
				player.getData().add(entry.getKey(), jsonObject);
			});
			Player plo = Ladder.getInstance().getPlayer(player.getName());
			if (plo != null) {
				List<String> string = new ArrayList<>();
				for (String entry : input.keySet()) {
					if (entry.equals("name")) break;
					string.add(entry);
				}
				String[] stringS = string.toArray(new String[string.size()]);
				plo.sendToBukkit(stringS);
				plo.sendToBungee(stringS);
			}
			object.add("data", player.getData());
			player.saveData();
		}
		return object;
	}
}
