package fr.badblock.ladder.chat;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import fr.badblock.ladder.api.chat.ChatColor;

/**
 * Permet de convertir une chaîne de caractère (avec des codes couleurs type &a, &b) en JSON compatible Minecraft
 * @author LeLanN
 */
public class JsonMessageSerializer {
	/**
	 * Convertit la chaîne
	 * @param string La chaîne de base
	 * @return L'objet JSON de retour
	 */
	public static JsonObject serialize(String string){
		string = ChatColor.replaceColor(string);
		
		JsonObject json = null;

		final JsonArray texts = new JsonArray();
		final List<String> colors = new ArrayList<>();
		
		for(int i = 0; i < string.length() - 1; i++) {
			String region = string.substring(i, i + 2);
			if (region.matches("(" + '\247' + "([a-fk-or0-9]))")) {
				colors.add(region);
			}
		}
		
		final String[] split = string.split("(" + '\247' + "([a-fk-or0-9]))");
		
		for (int i = 0; i < colors.size(); i++) {
			if(split.length <= i + 1) break;
			
			char code = colors.get(i).toCharArray()[1];
			ChatColor color = ChatColor.getByChar(code);
			
			JsonObject raw = new JsonObject();
			
			raw.add("text", new JsonPrimitive(split[i + 1]));
			setColor(color, raw);
			
			if(json == null)
				json = raw;
			else texts.add(raw);
		}

		if(json == null){
			json = new JsonObject();
			json.add("text", new JsonPrimitive(""));
		}
		
		if(texts.size() > 0)
			json.add("extra", texts);
		
		return json;
	}
	
	private static void setColor(ChatColor color, JsonObject raw){
		if(color == ChatColor.BOLD)
			raw.add("bold", new JsonPrimitive(true));
		else if(color == ChatColor.ITALIC)
			raw.add("italic", new JsonPrimitive(true));
		else if(color == ChatColor.UNDERLINE)
			raw.add("underlined", new JsonPrimitive(true));
		else if(color == ChatColor.STRIKETHROUGH)
			raw.add("strikethrough", new JsonPrimitive(true));
		else if(color == ChatColor.MAGIC)
			raw.add("obfuscated", new JsonPrimitive(true));
		else raw.add("color", new JsonPrimitive(color.getName()));
	}
}

