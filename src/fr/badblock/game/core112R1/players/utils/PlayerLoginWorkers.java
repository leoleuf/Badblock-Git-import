package fr.badblock.game.core112R1.players.utils;

import java.util.Map.Entry;
import java.util.Optional;

import org.bukkit.Bukkit;

import com.google.gson.JsonObject;

import fr.badblock.game.core112R1.GamePlugin;
import fr.badblock.game.core112R1.players.GameBadblockPlayer;
import fr.badblock.game.core112R1.technologies.rabbitlisteners.PlayerDataReceiver;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.events.PartyJoinEvent;
import fr.badblock.gameapi.events.api.PlayerLoadedEvent;
import fr.badblock.gameapi.game.rankeds.RankedManager;
import fr.badblock.gameapi.players.RankedPlayer;
import fr.badblock.gameapi.run.RunType;
import fr.badblock.gameapi.utils.general.Callback;
import net.md_5.bungee.api.ChatColor;

public class PlayerLoginWorkers
{

	public static void workAsync(GameBadblockPlayer player)
	{
		new Thread("loadPlayer-" + player.getName())
		{
			@Override
			public void run()
			{
				loadRankeds(player);
				loadPlayerData(player);
			}
		}.start();
	}

	public static void loadRankeds(GameBadblockPlayer player)
	{
		if (!GameAPI.getAPI().getRunType().equals(RunType.GAME))
		{
			return;
		}
		RankedManager rm = RankedManager.instance;
		String game = rm.getCurrentRankedGameName();
		rm.getMonthRank(game, player, new Callback<Integer>()
		{

			@Override
			public void done(Integer result, Throwable error) {
				player.setMonthRank(result.intValue());
			}

		});
		rm.getTotalRank(game, player, new Callback<Integer>()
		{

			@Override
			public void done(Integer result, Throwable error) {
				player.setTotalRank(result.intValue());
			}

		});
		rm.getTotalPoints(game, player, new Callback<Integer>()
		{

			@Override
			public void done(Integer result, Throwable error) {
				player.setTotalPoints(result.intValue());
			}

		});
		player.setRanked(new RankedPlayer(player, player.getTotalPoints(), player.getTotalRank(), player.getMonthRank()));
	}

	public static void loadPlayerData(GameBadblockPlayer player)
	{
		String name = player.getName();
		new Thread("loader-" + name)
		{
			@SuppressWarnings("deprecation")
			@Override
			public void run()
			{
				for (int i = 0; i < 100; i++)
				{
					Optional<Entry<String, JsonObject>> optional = PlayerDataReceiver.objectsToSet.entrySet().parallelStream().filter(entry ->
					{
						return entry.getValue().get("name").getAsString().equalsIgnoreCase(name) || entry.getKey().equalsIgnoreCase(name);
					}).findFirst();

					if (optional.isPresent())
					{
						Bukkit.getScheduler().runTask(GamePlugin.getInstance(), new Runnable()
						{
							@Override
							public void run()
							{
								Entry<String, JsonObject> entry = optional.get();
								entry.getValue().addProperty("uniqueId", player.getUniqueId().toString());
								player.setObject(entry.getValue());
								player.setLoad(true);

								if (entry.getValue().has("nickname") && entry.getValue().has("name")
										&& !entry.getValue().get("nickname").isJsonNull()
										&& !entry.getValue().get("name").isJsonNull())
								{
									String nickname = entry.getValue().get("nickname").getAsString();
									String rName = entry.getValue().get("name").getAsString();
									if (!rName.equalsIgnoreCase(nickname) && !nickname.isEmpty() && !rName.isEmpty())
									{
										player.setRealName(rName);
										GameAPI.logColor(ChatColor.RED + "[NICK] Real name of "+ nickname + " : " + rName);
									}
									else
									{
										player.setRealName(null);
									}
								}
								else
								{
									player.setRealName(null);
								}

								player.updateData(entry.getValue());
								PlayerDataReceiver.objectsToSet.remove(entry.getKey());
								player.setDataFetch(true);
								if (player.getPlayersWithHim() != null && !player.getPlayersWithHim().isEmpty())
									Bukkit.getPluginManager().callEvent(new PartyJoinEvent(player, player.getPlayersWithHim()));
								Bukkit.getPluginManager().callEvent(new PlayerLoadedEvent(player));
							}
						});
						stop();
					}

					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

}
