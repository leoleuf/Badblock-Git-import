package fr.badblock.game.core112R1.jsonconfiguration.data;

import fr.badblock.gameapi.run.RunType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameServerConfig {

	// GameServer part
	public boolean deleteFiles;
	public int timeBetweenLogs;
	public int ticksBetweenMonitoreLogs;
	public int ticksBetweenKeepAlives;
	public long uselessUntilTime;
	public boolean leaverBusterEnabled;
	public String serverName;
	public String serverDisplayName;
	
	// Run type
	public RunType runType = RunType.LOBBY;

}