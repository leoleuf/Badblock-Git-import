package fr.badblock.gameapi.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.scheduler.BukkitRunnable;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.utils.reflection.Reflector;

/**
 * Représentation des différentes GameRules par défaut de MineCraft, afin de
 * pouvoir les modifier aisément.<br>
 * Utilise les commandes pour éviter de passer par NMS (inutile).<br>
 * Pour plus d'informations sur leur effet, rechercher sur Internet (ou dans
 * votre cerveau, d'ailleurs).<br>
 * <br>
 * Par exemple, doFireTick (false) est trčs pratique pour beaucoup de mini-jeux,
 * cela évite d'avoir ŕ bloquer la propagation du feu (qui nécessite de cancel 2
 * ou 3 events).
 * 
 * @author LeLanN
 */
public enum GameRules {
	doFireTick, mobGriefing, keepInventory, doMobSpawning, doMobLoot, doTileDrops, doEntityDrops, commandBlockOutput, naturalRegeneration, doDaylightCycle, logAdminCommands, showDeathMessages, randomTickSpeed, sendCommandFeedback, reducedDebugInfo, spectatorsGenerateChunks;

	private void dispatch(String command) {

		new Thread("BadBlockAPI/gamerules") {
			@Override
			public void run() {
				try {
					CommandMap map = (CommandMap) new Reflector(Bukkit.getServer()).getFieldValue("commandMap");

					while (map.getCommand("gamerule") == null) {
						try {
							Thread.sleep(20L);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

					new BukkitRunnable() {
						@Override
						public void run() {
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
						}
					}.runTaskLater(GameAPI.getAPI(), 40L);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();

	}

	/**
	 * Définit une GameRule. Marche pour toute sauf randomTickSpeed qui
	 * nécessite une valeur numérique.
	 * 
	 * @param value
	 *            La valeur
	 */
	public void setGameRule(boolean value) {
		if (GameRules.randomTickSpeed == this)
			return;

		String command = "gamerule " + this.name() + " " + value;
		dispatch(command);
	}

	/**
	 * Définit une GameRule. Marche uniquement pour randomTickSpeed qui
	 * nécessite une valeur numérique.
	 * 
	 * @param value
	 *            La valeur
	 */
	public void setGameRule(int value) {
		if (GameRules.randomTickSpeed != this)
			return;

		String command = "gamerule " + this.name() + " " + value;
		dispatch(command);
	}
}
