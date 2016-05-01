package fr.badblock.gameapi.players;

import fr.badblock.gameapi.utils.i18n.TranslatableString;

/**
 * Repr�sente un achievment (succ�s) qu'un joueur pourra d�bloquer en jouant sur BadBlock
 * @author LeLanN
 */
public interface PlayerAchievement {
	/**
	 * R�cup�re le nom 'interne' (qui ne sera jamais affich�) de l'achievement. Par exemple rush.kill1<br>
	 * Pour obtenir la version affichage, passer par le syst�me d'i18n.
	 * @return Le nom 'interne'
	 */
	public String getAchievementName();
	
	/**
	 * Retourne le nom d'affichage de l'achievement (la cl�)
	 * @return Le nom d'affichage
	 */
	public TranslatableString getDisplayName();
	
	/**
	 * Retourne la description de l'achievement (quelques mots) (la cl�)
	 * @return La description de l'achievement
	 */
	public TranslatableString getDescription();
	
	/**
	 * V�rifie si l'achievement se r�initialise � chaque partie ou si il est un achievement cumulatif.<br>
	 * Par exemple, pour les achievements 'faire 10/100/100 kills', retourne false.<br>
	 * @return Si l'achievement est 'tout seul'
	 */
	public boolean isOnegameAchievement();
	
	/**
	 * R�cup�re la valeur demand�e pour que l'achievement soit d�bloquer. Peut �tre des kills, des lits cass�s, des morts, ...
	 * @return La valeur demand�e
	 */
	public int getAchievementNeededValue();
	
	/**
	 * R�cup�re le gain en points boutiques
	 * @return Le gain en points boutiques
	 */
	public int getMegaCoinsReward();
	
	/**
	 * R�cup�re le gain en XP
	 * @return Le gain en XP
	 */
	public int getXpReward();
	
	/**
	 * R�cup�re le gain en BadCoins
	 * @return Le gain en BadCoins
	 */
	public int getBadcoinsReward();
}
