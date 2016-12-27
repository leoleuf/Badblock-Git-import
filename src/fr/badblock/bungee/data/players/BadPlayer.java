package fr.badblock.bungee.data.players;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@Data@EqualsAndHashCode(callSuper=false)
public class BadPlayer extends BadOfflinePlayer {

	public static Map<String, BadPlayer> players = new HashMap<>();
	
	public String bungee;
	public String bukkitServer;
	
	public BadPlayer(String name, String bungee, InetAddress address) {
		super(name, address);
		this.bungee = bungee;
		players.put(name.toLowerCase(), this);
	}
	
	@SuppressWarnings("deprecation")
	public void sendMessage(String message) {
		ProxiedPlayer player = asProxied();
		if (player != null) player.sendMessage(message);
		//else // TODO
	}
	
	public ProxiedPlayer asProxied() {
		return BungeeCord.getInstance().getPlayer(this.getName());
	}
	
	public JsonObject createResultObject() {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("uniqueId", this.getUniqueId().toString());
		jsonObject.addProperty("onlineMode", (String) this.getData().get("onlineMode"));
		return jsonObject;
	}
	
	public BadPlayer get(ProxiedPlayer player) {
		return players.get(player.getName().toLowerCase());
	}
	
	public void disconnect() {
		players.remove(this.getName().toLowerCase());
	}
	
}
