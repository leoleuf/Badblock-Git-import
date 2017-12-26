package fr.badblock.ladder.http.players;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import com.google.gson.JsonObject;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.chat.ChatColor;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.ladder.http.LadderPage;

public class PagePlayerSendMessage extends LadderPage {
	public PagePlayerSendMessage() {
		super("/players/sendMessage/");
	}

	@Override
	public JsonObject call(Map<String, String> input) {
		JsonObject object = new JsonObject();
		if (!input.containsKey("name")) {
			object.addProperty("error", "Aucun pseudo!");
		} else if (!input.containsKey("message")) {
			object.addProperty("error", "Aucun message!");
		} else {
			Player player = Ladder.getInstance().getPlayer(input.get("name"));
			if (player == null) return object;
			String string;
			try
			{
				string = new String(input.get("message").getBytes("UTF-8"));
				string = ChatColor.replaceColor(string);
				string = string.replace("+", " ");
				player.sendMessage(string);
			}
			catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
		}
		
		return object;
	}
}
