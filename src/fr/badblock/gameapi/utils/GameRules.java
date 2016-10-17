package fr.badblock.gameapi.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;

import fr.badblock.gameapi.utils.reflection.Reflector;

/**
 * Repr�sentation des diff�rentes GameRules par d�faut de MineCraft, afin de
 * pouvoir les modifier ais�ment.<br>
 * Utilise les commandes pour �viter de passer par NMS (inutile).<br>
 * Pour plus d'informations sur leur effet, rechercher sur Internet (ou dans
 * votre cerveau, d'ailleurs).<br>
 * <br>
 * Par exemple, doFireTick (false) est tr�s pratique pour beaucoup de mini-jeux,
 * cela �vite d'avoir � bloquer la propagation du feu (qui n�cessite de cancel 2
 * ou 3 events).
 * 
 * @author LeLanN
 */
public enum GameRules {
	doFireTick, mobGriefing, keepInventory, doMobSpawning, doMobLoot, doTileDrops, doEntityDrops, commandBlockOutput, naturalRegeneration, doDaylightCycle, logAdminCommands, showDeathMessages, randomTickSpeed, sendCommandFeedback, reducedDebugInfo, spectatorsGenerateChunks;

	private void dispatch(String command) {

		new Thread() {
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
					
					Thread.sleep(200L);
				} catch (Exception e) {
					e.printStackTrace();
				}

				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
			}
		}.start();

	}

	/**
	 * D�finit une GameRule. Marche pour toute sauf randomTickSpeed qui
	 * n�cessite une valeur num�rique.
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
	 * D�finit une GameRule. Marche uniquement pour randomTickSpeed qui
	 * n�cessite une valeur num�rique.
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
