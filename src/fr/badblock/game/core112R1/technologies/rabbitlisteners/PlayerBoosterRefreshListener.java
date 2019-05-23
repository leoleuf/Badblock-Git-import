package fr.badblock.game.core112R1.technologies.rabbitlisteners;

import java.sql.ResultSet;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;

import fr.badblock.api.common.tech.rabbitmq.listener.RabbitListener;
import fr.badblock.api.common.tech.rabbitmq.listener.RabbitListenerType;
import fr.badblock.game.core112R1.GamePlugin;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.databases.SQLRequestType;
import fr.badblock.gameapi.game.rankeds.RankedManager;
import fr.badblock.gameapi.players.data.boosters.PlayerBooster;
import fr.badblock.gameapi.run.RunType;
import fr.badblock.gameapi.utils.general.Callback;

public class PlayerBoosterRefreshListener extends RabbitListener {

	public PlayerBoosterRefreshListener() {
		super(GameAPI.getAPI().getRabbitService(), "boosterRefresh", RabbitListenerType.SUBSCRIBER, false);
		load();
	}

	@Override
	public void onPacketReceiving(String body) {
		Bukkit.getScheduler().runTask(GamePlugin.getInstance(), new Runnable()
		{
			@Override
			public void run()
			{
				if (!GameAPI.getAPI().getRunType().equals(RunType.GAME)) {
					return;
				}

				System.out.println("booster: " + GameAPI.getGameName());
				if (RankedManager.instance.getCurrentRankedGameName().toLowerCase().startsWith(body.toLowerCase()))
				{
					GameAPI.getAPI().getSqlDatabase().call("SELECT value FROM keyValues WHERE `key` = 'booster'", SQLRequestType.QUERY, new Callback<ResultSet>() {

						@Override
						public void done(ResultSet result, Throwable error) {
							try {
								result.next();
								String value = result.getString("value");
								Map<String, PlayerBooster> updatedMap = GamePlugin.getGson().fromJson(value, GamePlugin.type);
								for (Entry<String, PlayerBooster> entry : updatedMap.entrySet()) {
									if (RankedManager.instance.getCurrentRankedGameName().toLowerCase().startsWith(entry.getKey().toLowerCase())) {
										GamePlugin.getInstance().gamePrefix = entry.getKey();
										GamePlugin.getInstance().booster = entry.getValue();
										break;
									}
								}
								result.close(); // don't forget to close it
							} catch (Exception exception) {
								exception.printStackTrace();
							}
						}

					});
				}
			}
		});
	}


}
