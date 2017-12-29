package fr.badblock.bungeecord.plugins.ladder.listeners;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import fr.badblock.bungeecord.plugins.ladder.LadderBungee;
import fr.badblock.bungeecord.plugins.ladder.bungee.BungeeKeep;
import fr.badblock.common.commons.technologies.rabbitmq.RabbitListener;
import fr.badblock.common.commons.technologies.rabbitmq.RabbitListenerType;

public class BungeePlayerListUpdateListener extends RabbitListener 
{
	
	public static final Type type = new TypeToken<BungeeKeep>() {}.getType();
	
	public static Map<String, List<String>> map = new HashMap<>();
	public static Gson						gson = new Gson();
	
	public BungeePlayerListUpdateListener()
	{
		super(LadderBungee.getInstance().rabbitService, "ladder.playerlistupdateBungee", false, RabbitListenerType.SUBSCRIBER);
	}

	@Override
	public void onPacketReceiving(String body)
	{
		if (body == null)
		{
			return;
		}
		BungeeKeep bungeeKeep = gson.fromJson(body, type);
		if (bungeeKeep == null)
		{
			return;
		}
		map.put(bungeeKeep.getBungeeName(), bungeeKeep.getPlayers());
	}
	
}

