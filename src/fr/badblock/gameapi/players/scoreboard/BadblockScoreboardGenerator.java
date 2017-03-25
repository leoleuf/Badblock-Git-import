package fr.badblock.gameapi.players.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitTask;

import fr.badblock.gameapi.GameAPI;

/**
 * Repr�sente la classe permettant de g�n�rer le scoreboard.
 * 
 * @author LeLanN
 */
public abstract class BadblockScoreboardGenerator {

	private int current = 0;
	private CustomObjective objective;
	private BukkitTask		bukkitTask;

	/**
	 * Permet de g�n�rer une derni�re ligne anim�e
	 * 
	 * @param objective
	 *            L'objectif g�r�
	 */
	public void doBadblockFooter(CustomObjective objective) {
		objective.changeLine(1, "&bplay.badblock.fr / " + GameAPI.getServerName());
		this.objective = objective;
		bukkitTask = Bukkit.getScheduler().runTaskTimer(GameAPI.getAPI(), this::doBadblockFooter0, 0, 2L);
	}

	private void doBadblockFooter0() {
		if (objective.getAssignedPlayer() == null || !objective.getAssignedPlayer().isOnline()) {
			bukkitTask.cancel();
			return;
		}

		String base = "badblock.fr";

		if (current < 0) {
			objective.changeLine(1, "&b" + base);
		} else {
			String result = "&b" + base.substring(0, current) + ChatColor.BLUE + base.substring(current, current + 1)
			+ "&b" + base.substring(current + 1);
			objective.changeLine(1, result);
		}

		current++;

		if (current == base.length()) {
			current = -10;
		}
	}

	/**
	 * G�n�re le scoreboard
	 */
	public abstract void generate();
}
