package fr.badblock.api.utils.bukkit.title;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class MessageSerializer {
	@SuppressWarnings("unchecked")
	public static String convertToJSON(String string) {
		JSONObject json = new JSONObject();
		json.put("text", "");

		JSONArray texts = new JSONArray();

		final java.util.List<String> colors = new ArrayList<>();
		for (int i = 0; i < string.length() - 1; i++) {
			String region = string.substring(i, i + 2);
			if (region.matches("(" + '\247' + "([a-fk-or0-9]))")) {
				colors.add(region);
			}
		}
		final String[] split = string.split("(" + '\247' + "([a-fk-or0-9]))");
		for (int i = 0; i < colors.size(); i++) {
			JSONObject raw = new JSONObject();
			raw.put("text", split[i + 1]);
			raw.put("color", ChatColor.getByChar(colors.get(i).substring(1)).name().toLowerCase());
			texts.add(raw);
		}

		json.put("extra", texts);
		return json.toString();
	}
}
