package fr.badblock.bungeecord.plugins.others.tmputils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlayerBooster {

	private String username;
	private long expire;
	private boolean enabled;
	private long addedXp;
	private long addedBadcoins;
	private String gameName;
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
		return expire > System.currentTimeMillis() || expire == -1;
	}

}
