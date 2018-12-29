package fr.badblock.game.core112R1.gameserver.threading;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import fr.badblock.api.common.minecraft.DockerRabbitQueues;
import fr.badblock.api.common.minecraft.GameState;
import fr.badblock.api.common.minecraft.InstanceKeepAlive;
import fr.badblock.game.core112R1.GamePlugin;
import fr.badblock.game.core112R1.gameserver.DevAliveFactory;
import fr.badblock.game.core112R1.gameserver.GameServerManager;
import fr.badblock.game.core112R1.jsonconfiguration.data.GameServerConfig;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.game.GameServer;
import fr.badblock.gameapi.run.RunType;
import fr.badblock.gameapi.utils.BukkitUtils;
import fr.badblock.gameapi.utils.threading.TaskManager;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameServerKeeperAliveTask extends GameServerTask {
	private static boolean openStaff = false;

	public static boolean isOpenToStaff()
	{
		return openStaff;
	}

	public static boolean switchOpenStaff()
	{
		return (openStaff = !openStaff);
	}

	private long 		joinTime;
	private boolean 	lastJoinable = true;
	private String	gameState;

	public GameServerKeeperAliveTask(GameServerConfig apiConfig) {
		this.incrementJoinTime();
		TaskManager.scheduleAsyncRepeatingTask("gameServerKeeperAlive", this, 0, apiConfig.ticksBetweenKeepAlives);
		TaskManager.scheduleAsyncRepeatingTask("gameServerChange", toChange(), 0, 1);
	}

	public void incrementJoinTime() {
		this.setJoinTime(System.currentTimeMillis() + this.getGameServerManager().getGameServerConfig().uselessUntilTime);
	}

	public Runnable toChange()
	{
		return new Runnable()
		{
			@Override
			public void run()
			{
				String gms = gameState != null ? gameState : "";
				String strati = GameAPI.getAPI().getGameServer().getGameState() != null ? GameAPI.getAPI().getGameServer().getGameState().name() : "";
				
				if (lastJoinable != isJoinable() || !gms.equalsIgnoreCase(strati))
				{
					if ("RUNNING".equals(strati) && (gameState == null || gameState.equals("WAITING")))
					{
						TaskManager.runTask(new Runnable()
						{
							@Override
							public void run()
							{
								for (Player player : BukkitUtils.getPlayers())
								{
									if (player.getGameMode().equals(GameMode.SPECTATOR))
										continue;
									player.setAllowFlight(false);
								}
							}
						});
					}
					lastJoinable = isJoinable();
					gameState = GameAPI.getAPI().getGameServer().getGameState().name();
					GameAPI.getAPI().getGameServer().keepAlive();
				}
			}
		};
	}

	private boolean isJoinable() {
		String gameState = GamePlugin.getInstance().getGameServer().getGameState().name();
		if (gameState.equals("RUNNING") && GameAPI.getAPI().getGameServer().isJoinableWhenRunning()) {
			if (!GameAPI.getAPI().getTeams().isEmpty()) {
				// team pas full mais team avec > 0
				long count = GameAPI.getAPI().getTeams().stream().filter(team -> team.playersCurrentlyOnline() < team.getMaxPlayers() && team.playersCurrentlyOnline() > 0 && !team.isDead()).count();
				if (count == 0) return false;
				return GameAPI.isJoinable();
			}
			return GameAPI.isJoinable();
		}
		return GameAPI.getAPI().getRunType().equals(RunType.GAME) ? gameState.equals("WAITING") : !GameAPI.getAPI().isFinished();
	}

	public void keepAlive(int addedPlayers) {
		keepAlive(GameAPI.getAPI().getGameServer().getGameState().name(), addedPlayers);
	}

	public void keepAlive(String gameState, int addedPlayers) {
		sendKeepAlivePacket(gameState, addedPlayers);
	}

	@Override
	public void run() {
		GameAPI.logColor("&b[GameServer] &eDEBUG Stop: " + isJoinable() + " | " + Bukkit.getOnlinePlayers().size() + " | " + (getJoinTime() - System.currentTimeMillis()));
		if (getJoinTime() < System.currentTimeMillis() && Bukkit.getOnlinePlayers().size() == 0) {
			GameAPI.logColor("&b[GameServer] &cNobody during few minutes, shutdown..");
			Bukkit.shutdown();
			return;
		}
		this.keepAlive(0);
	}

	private void sendKeepAlivePacket(String gameState, int addedPlayers) {
		GameAPI gameApi = GameAPI.getAPI();
		GameServerManager gameServerManager = this.getGameServerManager();
		// ServerConfigurationFactory serverConfigurationFactory =
		// gameServerManager.getServerConfigurationFactory();
		InstanceKeepAlive gameAliveFactory = new InstanceKeepAlive(gameApi.getServer().getServerName(), isJoinable(), GameState.getStatus(gameState), Bukkit.getOnlinePlayers().size() + addedPlayers);
		sendDevSignal(true, addedPlayers);
		String queue = GamePlugin.getInstance().isNewbackend() ? DockerRabbitQueues.INSTANCE_KEEPALIVE.getQueue() : "networkdocker.instance.keepalive";
		gameApi.getRabbitSpeaker().sendAsyncUTF8Publisher(queue, gameServerManager.getGson().toJson(gameAliveFactory), 5000, false);
	}

	public void sendStopPacket() {
		GameAPI gameApi = GameAPI.getAPI();
		// ServerConfigurationFactory serverConfigurationFactory =
		// gameServerManager.getServerConfigurationFactory();
		sendDevSignal(false, 0);
		String queue = GamePlugin.getInstance().isNewbackend() ? DockerRabbitQueues.INSTANCE_STOP.getQueue() : "networkdocker.instance.stop";
		gameApi.getRabbitSpeaker().sendSyncUTF8Publisher(queue, gameApi.getServer().getServerName(), 5000, false);
	}

	public static void sendDevSignal(boolean open, int addedPlayers)
	{
		GameAPI gameApi = GameAPI.getAPI();
		GameServerManager gameServerManager = GamePlugin.getInstance().getGameServerManager();
		GameServer gameServer = gameApi.getGameServer();

		if(gameApi.getRunType() != RunType.DEV)
			return;

		DevAliveFactory devAliveFactory = new DevAliveFactory(gameApi.getServer().getServerName(), open, Bukkit.getOnlinePlayers().size() + addedPlayers, gameServer.getMaxPlayers(), openStaff);
		gameApi.getRabbitSpeaker().sendAsyncUTF8Publisher("dev", gameServerManager.getGson().toJson(devAliveFactory), 5000, false);
	}

}
