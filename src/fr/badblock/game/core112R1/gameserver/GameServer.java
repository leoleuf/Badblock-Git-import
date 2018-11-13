package fr.badblock.game.core112R1.gameserver;

import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import fr.badblock.game.core112R1.GamePlugin;
import fr.badblock.game.core112R1.players.GameBadblockPlayer;
import fr.badblock.game.core112R1.players.ingamedata.GameBadblockPlayerData;
import fr.badblock.game.core112R1.players.ingamedata.GameOfflinePlayer;
import fr.badblock.gameapi.BadListener;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.databases.LadderSpeaker;
import fr.badblock.gameapi.game.GameState;
import fr.badblock.gameapi.players.BadblockOfflinePlayer;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.BadblockMode;
import fr.badblock.gameapi.players.BadblockPlayerData;
import fr.badblock.gameapi.players.BadblockTeam;
import fr.badblock.gameapi.utils.BukkitUtils;
import fr.badblock.gameapi.utils.ServerProperties;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_12_R1.MinecraftServer;

/**
 * Overrided GameServer interface
 * 
 * @authors xMalware & LeLanN
 */
@Getter
@Setter
public class GameServer extends BadListener implements fr.badblock.gameapi.game.GameServer {
	private GameState gameState = GameState.WAITING;
	private int maxPlayers = Bukkit.getMaxPlayers();

	private WhileRunningConnectionTypes type = WhileRunningConnectionTypes.SPECTATOR;
	private Map<String, BadblockOfflinePlayer> players = Maps.newConcurrentMap();

	private List<BadblockTeam> savedTeams = Lists.newArrayList();
	private Map<String, BadblockPlayerData> savedPlayers = Maps.newConcurrentMap();
	private boolean saving = false;

	private List<UUID> play = new ArrayList<>();
	private String gameBegin = "";

	private long gameId = new SecureRandom().nextLong();

	@Override
	public void keepAlive() {
		GamePlugin.getInstance().getGameServerManager().getGameServerKeeperAliveTask().keepAlive(0);
	}

	@Override
	public void cancelReconnectionInvitations() {
		type = WhileRunningConnectionTypes.SPECTATOR;

		players.keySet().forEach(name ->
		GameAPI.getAPI().getRabbitSpeaker().sendAsyncUTF8Publisher("removeReconnectionInvitations", name.toLowerCase(),
				10000, false));
		players.clear();
	}

	@Override
	public void cancelReconnectionInvitations(BadblockTeam team) {
		LadderSpeaker ladderSpeaker = GameAPI.getAPI().getLadderDatabase();

		players.entrySet().forEach(entry -> {
			if (team.equals(entry.getValue().getTeam())) {
				GameAPI.getAPI().getRabbitSpeaker().sendAsyncUTF8Publisher("removeReconnectionInvitations", entry.getKey().toLowerCase(),
						10000, false);
				players.remove(entry.getKey());
			}
		});
	}

	@Override
	public Collection<BadblockPlayerData> getSavedPlayers() {

		Collection<BadblockPlayerData> players = savedPlayers.values();
		List<BadblockPlayerData> list = new ArrayList<>();

		players.forEach(player -> list.add(player));
		Bukkit.getOnlinePlayers().forEach(player -> {
			GameBadblockPlayer bplayer = (GameBadblockPlayer) player;
			if (play.contains(bplayer.getUniqueId()))
				list.add(new GameBadblockPlayerData(bplayer));
		});

		return list;
	}

	public void remember(BadblockPlayer player) {
		GameOfflinePlayer offline = new GameOfflinePlayer((GameBadblockPlayer) player);
		players.put(player.getName().toLowerCase(), offline);

		GameAPI.getAPI().getRabbitSpeaker().sendAsyncUTF8Publisher("reconnectionInvitations", player.getName().toLowerCase() + ";" + GameAPI.getServerName(),
				10000, true);
		//GameAPI.getAPI().getLadderDatabase().sendReconnectionInvitation(player.getName().toLowerCase(), true);
	}

	@Override
	public void saveTeamsAndPlayersForResult() {
		if (saving)
			return;

		saving = true;

		GameAPI.getAPI().getTeams().forEach(team -> savedTeams.add(team));
		Bukkit.getOnlinePlayers().forEach(player -> play.add(player.getUniqueId()));
	}

	@Override
	public void setGameState(GameState gameState) {
		this.gameState = gameState;
		if (gameState == GameState.FINISHED)
			cancelReconnectionInvitations();

		if (gameState == GameState.RUNNING) {
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			gameBegin = dateFormat.format(System.currentTimeMillis()) + " (GMT +1)";
			for (BadblockPlayer player : BukkitUtils.getAllPlayers())
			{
				if (player.getGameMode().equals(GameMode.SPECTATOR))
				{
					continue;
				}
				if (player.getBadblockMode().equals(BadblockMode.SPECTATOR))
				{
					continue;
				}
				player.setAllowFlight(false);
			}
		}

		keepAlive();
	}

	@Override
	public void whileRunningConnection(WhileRunningConnectionTypes type) {
		this.type = type;

		if (type == WhileRunningConnectionTypes.SPECTATOR)
			cancelReconnectionInvitations();
	}

	@Override
	public double getPassmarkTps() {
		double[] original = MinecraftServer.getServer().recentTps;
		double[] tps = new double[original.length];
		double total = 0.0D;
		for (int x = 0; x < original.length; x++) {
			double value = original[x];
			if (value > 20.0D) {
				value = 20.0D;
			} else if (value < 0.0D) {
				value = 0.0D;
			}
			tps[x] = value;
			total += value;
		}
		total /= original.length;
		return total;
	}

	@Override
	public boolean isJoinableWhenRunning() {
		return Boolean.parseBoolean(ServerProperties.getProperties().getProperty("docker-runningMatchmaking")) && !Bukkit.getServerName().startsWith("speeduhc") && !Bukkit.getServerName().startsWith("sg");
	}

}
