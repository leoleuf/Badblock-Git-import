package fr.badblock.sentry;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter public class SEntry {

	private String		playerName;
	private String		serverName;

	public SEntry (String playerName, String serverName) {
		this.setPlayerName(playerName);
		this.setServerName(serverName);
	}

}
