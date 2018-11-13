package fr.badblock.game.core112R1.players.utils;

import java.sql.ResultSet;
import java.sql.Statement;

import org.bukkit.Bukkit;

import com.google.gson.JsonObject;

import fr.badblock.game.core112R1.players.GameBadblockPlayer;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.events.PartyJoinEvent;
import fr.badblock.gameapi.events.api.PlayerLoadedEvent;
import fr.badblock.gameapi.game.rankeds.RankedManager;
import fr.badblock.gameapi.players.RankedPlayer;
import fr.badblock.gameapi.run.RunType;
import fr.badblock.gameapi.utils.general.Callback;

public class PlayerLoginWorkers
{

	public static void workAsync(GameBadblockPlayer player)
	{
		new Thread("loadPlayer-" + player.getName())
		{
			@Override
			public void run()
			{
				loadNick(player);
				loadRankeds(player);
				loadPlayerData(player);
			}
		}.start();
	}

	public static void loadNick(GameBadblockPlayer player)
	{
		try
		{
			Statement statement = GameAPI.getAPI().getSqlDatabase().createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT playerName FROM nick WHERE nick = '" + player.getName() + "'");
			String realName = null;
			if (resultSet.next())
			{
				String playerName = resultSet.getString("playerName");
				if (!playerName.isEmpty())
				{
					realName = playerName;
				}
			}
			player.setRealName(realName);
			resultSet.close();
			statement.close();
		}
		catch (Exception error)
		{
			error.printStackTrace();
		}
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
		GameAPI.getAPI().getLadderDatabase().getPlayerData(player.getRealName() != null ? player.getRealName() : player.getName(), new Callback<JsonObject>() {
			@Override
			public void done(JsonObject result, Throwable error) {
				new Thread() {
					@Override
					public void run() {
						player.setObject(result);
						player.updateData(result);

						while (!player.isHasJoined())
							try {
								Thread.sleep(10L);
							} catch (InterruptedException unused) {}

						player.setDataFetch(true);
						synchronized (Bukkit.getServer()) {
							if (player.getPlayersWithHim() != null && !player.getPlayersWithHim().isEmpty())
								Bukkit.getPluginManager().callEvent(new PartyJoinEvent(player, player.getPlayersWithHim()));
							Bukkit.getPluginManager().callEvent(new PlayerLoadedEvent(player));
						}
					}
				}.start();
			}
		});
	}

}
