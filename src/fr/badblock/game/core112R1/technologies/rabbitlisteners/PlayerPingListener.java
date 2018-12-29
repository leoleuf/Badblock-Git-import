package fr.badblock.game.core112R1.technologies.rabbitlisteners;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import fr.badblock.api.common.tech.rabbitmq.listener.RabbitListener;
import fr.badblock.api.common.tech.rabbitmq.listener.RabbitListenerType;
import fr.badblock.gameapi.GameAPI;

public class PlayerPingListener extends RabbitListener {

	public static Map<String, Integer> ping = new HashMap<>();
	private static Gson gson = new Gson();
	public static final Type collectionType = new TypeToken<Map<String, Integer>>() {}.getType();

	public PlayerPingListener() {
		super(GameAPI.getAPI().getRabbitService(), "playerPing", RabbitListenerType.SUBSCRIBER, false);
		load();
	}

	@Override
	public void onPacketReceiving(String body) {
		Map<String, Integer> temp = gson.fromJson(body, collectionType);
		for (Entry<String, Integer> entry : temp.entrySet()) {
			ping.put(entry.getKey(), entry.getValue());
		}
	}

}
