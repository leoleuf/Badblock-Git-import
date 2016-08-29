package fr.badblock.gameapi.players.data.boosters;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlayerBooster {

	private long	expire;
	private boolean enabled;
	private Booster booster;

	/**
	 * R�cup�re le fait que le booster est expir� ou non
	 * 
	 * @return Un bool�en
	 */
	public boolean isExpired() {
		return !isValid();
	}

	/**
	 * R�cup�re le fait que le booster est encore valide ou non
	 * 
	 * @return Un bool�en
	 */
	public boolean isValid() {
		return expire > System.currentTimeMillis();
	}

}
