package fr.badblock.gameapi.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

/**
 * Une s�rie de m�thode permettant de simplifier certaines utilisations de l'API Bukkit.
 * @author LelanN
 */
public class BukkitUtils {
	/**
	 * Permet de changer un block de mani�re temporaire
	 * @param block Le block
	 * @param newType Le type � mettre temporairement
	 * @param newData La data � mettre temporairement
	 * @param ticks Le nombre de ticks pendant lesquels le block va rester
	 */
	public static void temporaryChangeBlock(Block block, Material newType, byte newData, int ticks){
		
	}
	
	/**
	 * V�rifie si la location peut acceuillir un joueur
	 * @param location La location
	 * @return Un boolean
	 */
	public static boolean isSafeLocation(Location location) {
		Block block = location.getBlock();
		return !block.getType().isSolid() || !block.getRelative(0, 1, 0).getType().isSolid();
	}
}
