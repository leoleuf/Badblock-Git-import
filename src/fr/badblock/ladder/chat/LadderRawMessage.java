package fr.badblock.ladder.chat;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.chat.ChatColor;
import fr.badblock.ladder.api.chat.RawMessage;
import fr.badblock.ladder.api.entities.BungeeCord;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.ladder.api.utils.StringUtils;
import fr.badblock.protocol.packets.Packet;
import fr.badblock.protocol.packets.PacketPlayerChat;
import fr.badblock.protocol.packets.PacketPlayerChat.ChatAction;

public class LadderRawMessage implements RawMessage {
	private JsonObject object;
	
	public LadderRawMessage(String base){
		object = JsonMessageSerializer.serialize(base);
	}
	
	@Override
	public void send(Player... players) {
		for(Player player : players){
			player.sendPacket(new PacketPlayerChat(player.getUniqueId(), ChatAction.MESSAGE_JSON, asJsonObject().toString()));		
		}
	}

	@Override
	public void broadcast(BungeeCord... servers) {
		Packet packet = new PacketPlayerChat(null, ChatAction.MESSAGE_JSON, asJsonObject().toString());
		for(BungeeCord server : servers){
			server.sendPacket(packet);
		}
	}

	@Override
	public void broadcastAll() {
		Packet packet = new PacketPlayerChat(null, ChatAction.MESSAGE_JSON, asJsonObject().toString());
		Ladder.getInstance().broadcastPacket(packet);
	}

	@Override
	public RawMessage setClickEvent(ClickEventType type, boolean parse, String... values) {
		if(type == null || values.length == 0){
			object.remove("clickEvent");
		} else {
			String value = StringUtils.join(values, "\n");
			object.add("clickEvent", new JsonObject());
			
			JsonObject event = object.get("clickEvent").getAsJsonObject();
			event.add("action", new JsonPrimitive(type.name().toLowerCase()));
			event.add("value", parse ? JsonMessageSerializer.serialize(value) : new JsonPrimitive(value));
		}
		
		return this;
	}

	@Override
	public RawMessage setHoverEvent(HoverEventType type, boolean parse, String... values) {
		if(type == null || values.length == 0){
			object.remove("hoverEvent");
		} else {
			String value = StringUtils.join(values, "\n");
			object.add("hoverEvent", new JsonObject());
			
			JsonObject event = object.get("hoverEvent").getAsJsonObject();
			event.add("action", new JsonPrimitive(type.name().toLowerCase()));
			event.add("value", parse ? JsonMessageSerializer.serialize(value) : new JsonPrimitive(value));
		}
		
		return this;
	}

	@Override
	public RawMessage setMainColor(ChatColor color) {
		object.add("color", new JsonPrimitive(color.name().toLowerCase()));
		return this;
	}

	@Override
	public RawMessage add(RawMessage message) {
		if(!object.has("extra"))
			object.add("extra", new JsonArray());
		object.get("extra").getAsJsonArray().add(message.asJsonObject());
	
		return this;
	}

	@Override
	public RawMessage addAll(RawMessage... all) {
		for(RawMessage message : all)
			add(message);
	
		return this;
	}

	@Override
	public JsonObject asJsonObject() {
		return object;
	}

}
