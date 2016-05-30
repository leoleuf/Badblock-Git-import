package fr.badblock.gameapi.players.kits;

import fr.badblock.gameapi.achievements.PlayerAchievement;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.utils.itemstack.ItemStackExtra;

/**
 * Repr�sente un Kit obtenable par un joueur.
 * @author LeLanN
 */
public interface PlayerKit {
	/**
	 * R�cup�re le nom 'interne' (qui ne sera jamais affich�) du kit. Par exemple rush.archer<br>
	 * Pour obtenir la version affichage, passer par le syst�me d'i18n.
	 * @return Le nom 'interne'
	 */
	public String getKitName();
	
	/**
	 * Retourne l'itemstack devant repr�senter l'item dans un inventaire avec
	 * les informations propre au joueur (le niveau, si il est s�l�ctionn�)
	 * @param player Le joueur pour lequel l'item est fait
	 * @return L'itemstack
	 */
	public ItemStackExtra getKitItem(BadblockPlayer player);
	
	/**
	 * R�cup�re les achievements que doit avoir termin� le joueur pour obtenir le Kit au niveau donn�
	 * @param level Le niveau du Kit
	 * @return Les achievements
	 */
	public PlayerAchievement[] getNeededAchievements(int level);
	
	/**
	 * R�cup�re le nombre de BadCoins n�cessaires pour obtenir le kit au niveau donn�
	 * @param level Le niveau du Kit
	 * @return
	 */
	public int getBadcoinsCost(int level);
	
	/**
	 * R�cup�re le niveau maximal du Kit
	 * @return
	 */
	public int getMaxLevel();
	
	/**
	 * V�rifie si le Kit est VIP. Si oui, aucun achievements / badcoins n'est n�cessaire
	 * @return
	 */
	public boolean isVIP();
	
	/**
	 * Donne le Kit � un joueur (le niveau auquel le joueur � le droit sera recherch� automatiquement).
	 * @param player Le joueur
	 */
	public void giveKit(BadblockPlayer player);
}
