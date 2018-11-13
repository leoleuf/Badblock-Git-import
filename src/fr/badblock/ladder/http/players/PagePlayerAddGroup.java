package fr.badblock.ladder.http.players;

import java.util.Map;

import com.google.gson.JsonObject;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.entities.OfflinePlayer;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.ladder.http.LadderPage;
import fr.badblock.permissions.PermissibleGroup;
import fr.badblock.permissions.PermissiblePlayer;
import fr.badblock.permissions.PermissionManager;

public class PagePlayerAddGroup extends LadderPage {
	public PagePlayerAddGroup() {
		super("/players/addGroup/");
	}

	@Override
	public JsonObject call(Map<String, String> input) {
		JsonObject object = new JsonObject();
		if (!input.containsKey("name")) {
			object.addProperty("error", "Aucun pseudo!");
		}else if (!input.containsKey("group")) {
			object.addProperty("error", "Aucun groupe!");
		}else if (!input.containsKey("duration")) {
			object.addProperty("error", "Aucune duration!");
		} else {
			object.addProperty("name", input.get("name"));
			OfflinePlayer player = Ladder.getInstance().getOfflinePlayer(input.get("name"));
			PermissiblePlayer p = (PermissiblePlayer) player.getAsPermissible();
			PermissibleGroup group = PermissionManager.getInstance().getGroup(input.get("group"));
			if (group == null) {
				object.addProperty("error", "Groupe inconnu!");
			}
			if (p.getSuperGroup().equalsIgnoreCase(input.get("group"))) {
				object.addProperty("error", "Groupe deja assignee");
				return object;
			}
			for (String key : p.getAlternateGroups().keySet())
				if (key.equalsIgnoreCase(input.get("group"))) {
					object.addProperty("error", "Groupe deja assignee");
					return object;
				}
			if (p.getSuperGroup().equalsIgnoreCase("default")) { // pas de groupe
				p.setParent(Long.parseLong(input.get("duration")), group);
				player.saveData();
				object.addProperty("success", "ok");
			} else {
				p.addParent(Long.parseLong(input.get("duration")), group);
				player.saveData();
				object.addProperty("success", "ok");
			}
			Player playerr = Ladder.getInstance().getPlayer(player.getName());
			if (playerr != null) {
				playerr.sendToBukkit("permissions");
				playerr.sendToBungee("permissions");
			}
		}
		return object;
	}
}
