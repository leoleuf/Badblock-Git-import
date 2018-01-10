package fr.badblock.ladder.http.players;

import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.JsonObject;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.entities.OfflinePlayer;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.ladder.http.LadderPage;

public class PagePlayerUpdateData extends LadderPage {
	public PagePlayerUpdateData() {
		super("/players/updateData/");
	}

	@Override
	public JsonObject call(JsonObject input) {
		JsonObject object = new JsonObject();
		if (!input.has("name")) {
			object.addProperty("error", "Aucun pseudo!");
		} else {
			object.addProperty("name", input.get("name").getAsString());
			OfflinePlayer player = Ladder.getInstance().getOfflinePlayer(input.get("name").getAsString());
						
			input.entrySet().stream().filter(entry -> !entry.getKey().equals("name")).forEach(entry -> {
				player.getData().add(entry.getKey(), entry.getValue());
			});
			Player plo = Ladder.getInstance().getPlayer(player.getName());
			if (plo != null) {
				List<String> string = input.entrySet().stream().map(e -> e.getKey()).collect(Collectors.toList());

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
