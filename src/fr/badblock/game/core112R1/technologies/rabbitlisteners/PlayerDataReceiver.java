package fr.badblock.game.core112R1.technologies.rabbitlisteners;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.BasicDBObject;

import fr.badblock.api.common.sync.bungee.BadBungeeQueues;
import fr.badblock.api.common.tech.rabbitmq.RabbitService;
import fr.badblock.api.common.tech.rabbitmq.listener.RabbitListener;
import fr.badblock.api.common.tech.rabbitmq.listener.RabbitListenerType;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacket;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacketEncoder;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacketMessage;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacketType;
import fr.badblock.api.common.utils.GsonUtils;
import fr.badblock.game.core112R1.GamePlugin;
import fr.badblock.game.core112R1.players.GameBadblockPlayer;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.players.BadblockPlayer;

public class PlayerDataReceiver extends RabbitListener 
{

	public static Map<String, JsonObject> objectsToSet = new HashMap<>();

	public PlayerDataReceiver()
	{
		super(GameAPI.getAPI().getRabbitService(), BadBungeeQueues.BUNGEE_DATA_PLAYERS + Bukkit.getServerName(), RabbitListenerType.SUBSCRIBER, false);
		load();
	}

	@Override
	public void onPacketReceiving(String body)
	{
		Bukkit.getScheduler().runTask(GamePlugin.getInstance(), new Runnable()
		{
			@Override
			public void run()
			{
				BasicDBObject databaseObject = GsonUtils.getGson().fromJson(body, BasicDBObject.class);

				if (!databaseObject.containsField("name"))
				{
					return;
				}

				String name = databaseObject.getString("name");

				if (databaseObject.containsField("nickname"))
				{
					String nickname = databaseObject.getString("nickname");
					if (nickname != null && !nickname.isEmpty() && !name.equalsIgnoreCase(nickname))
					{
						name = nickname;
					}
				}

				GameAPI.logColor(ChatColor.YELLOW + "[GameAPI DATA] Received " + name + "'s data.");

				JsonParser jsonParser = new JsonParser();
				JsonObject jsonObject = jsonParser.parse(databaseObject.toJson()).getAsJsonObject();

				objectsToSet.put(name, jsonObject);
			}
		});
	}

	public static void send(BadblockPlayer player)
	{
		send(player, null);
	}

	public static void send(BadblockPlayer player, JsonObject data)
	{
		if (player.getAddress() == null)
		{
			return;
		}

		GameBadblockPlayer plo = (GameBadblockPlayer) player;

		if (!plo.isLoad())
		{
			return;
		}

		RabbitService rabbitService = GameAPI.getAPI().getRabbitService();
		JsonObject jsonObject = new JsonObject();
		String realName = plo.getRealName() != null ? plo.getRealName().toLowerCase() : plo.getName().toLowerCase();
		jsonObject.addProperty("playerName", realName);
		JsonObject dat = player.getPlayerData().saveData();
		if (data != null)
		{
			for (Entry<String, JsonElement> entry : data.entrySet())
			{
				dat.add(entry.getKey(), entry.getValue());
			}
		}
		jsonObject.add("data", dat);
		RabbitPacketMessage packetMessage = new RabbitPacketMessage(60000, GameAPI.getGson().toJson(jsonObject));
		RabbitPacket rabbitPacket = new RabbitPacket(packetMessage, "bungee.data.receivers.update", false, RabbitPacketEncoder.UTF8,
				RabbitPacketType.PUBLISHER);
		rabbitService.sendPacket(rabbitPacket);
	}

}