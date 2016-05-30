package fr.badblock.gameapi.players.data;

import fr.badblock.gameapi.achievements.PlayerAchievement;
import fr.badblock.gameapi.players.kits.PlayerKit;
import fr.badblock.gameapi.utils.i18n.Locale;

/**
 * R�presente les diff�rentes donn�es d'un joueur (coins/xp, kits, achievements, stats, ...)
 * @author LeLanN
 */
public interface PlayerData {
	/**
	 * R�cup�re le nombre de BadCoins du joueur
	 * @return Le nombre de BadCoins
	 */
	public int getBadcoins();
	
	/**
	 * Ajoute des BadCoins au joueur
	 * @param badcoins Une valeur POSITIVE
	 */
	public void addBadcoins(int badcoins);
	
	/**
	 * Supprime des BadCoins au joueur
	 * @param badcoins Une valeur POSITIVE
	 */
	public void removeBadcoins(int badcoins);
	
	/**
	 * R�cup�re le niveau du joueur
	 * @return Le niveau du joueur
	 */
	public int getLevel();
	
	/**
	 * R�cup�re l'XP obtenue dans le niveau en cours (autrement dit, r�initialis�e � chaque niveau)
	 * @return L'XP
	 */
	public long getXp();
	
	/**
	 * R�cup�re l'XP obtenue pour passer au niveau suivant (r�initialis�e � chaque niveau)
	 * @return L'Xp n�cessaire
	 */
	public long getXpUntilNextLevel();
	
	/**
	 * Ajoute de l'XP au joueur
	 * @param xp L'XP � ajouter
	 */
	public void addXp(long xp);
	
	/**
	 * R�cup�re le bonus en coins (achetable ou durant un �v�nement) du joueur.
	 * @return Le bonus (� multiplier par le nombre normal)
	 */
	public int getCoinsBonus();
	
	/**
	 * R�cup�re le bonus en XP (en fonction du niveau) du joueur.
	 * @return Le bonus (� multiplier par le nombre normal)
	 */
	public int getXpBonus();
	
	/**
	 * R�cup�re l'avancement du joueur dans un achievement
	 * @param achievement L'achievement en question
	 * @return L'avancement du joueur (si pas d'avancement, un nouveau sera cr��)
	 */
	public PlayerAchievementState getAchievementState(PlayerAchievement achievement);

	/**
	 * R�cup�re le niveau que le joueur a pour un kit.
	 * @param kit Le kit
	 * @return Le niveau (0 = pas le Kit)
	 */
	public int getUnlockedKitLevel(PlayerKit kit);
	
	/**
	 * V�rifie si le joueur peut obtenir le niveau suivant du kit (achievements et badcoins)
	 * @param kit Le kit
	 * @return Si il peut l'obtenir (si il est au niveau maximal, false)
	 */
	public boolean canUnlockNextLevel(PlayerKit kit);

	/**
	 * D�bloque le niveau suivant du kit. Ne fonctionne pas si {@link #canUnlockNextLevel(PlayerKit)} retourne false.
	 * @param kit Le kit
	 */
	public void unlockNextLevel(PlayerKit kit);
	
	/**
	 * R�cup�re le nom interne du dernier kit utilis� dans un jeu
	 * @param game Le nom (interne) du jeu
	 * @return Le nom interne du kit ou null si aucun
	 */
	public String getLastUsedKit(String game);
	
	/**
	 * D�finit le nom interne du dernier kit utilis� dans un jeu
	 * @param game Le nom (interne) du jeu
	 * @param kit Le nom (interne) du kit
	 */
	public void setLastUsedKit(String game, String kit);
	
	/**
	 * R�cup�re le langage choisit par le joueur.
	 * @return Le langage.
	 */
	public Locale getLocale();
	
	/**
	 * R�cup�re une statistique du joueur
	 * @param gameName Le jeu
	 * @param stat La statistique
	 * @return La valeur
	 */
	public double getStatistics(String gameName, String stat);
	
	/**
	 * Incr�mente (1) la statistique du joueur
	 * @param gameName Le jeu
	 * @param stat La statistique
	 */
	public void incrementStatistic(String gameName, String stat);
	
	/**
	 * Augment la statistique du joueur
	 * @param gameName Le jeu
	 * @param stat La statistique
	 * @param value La valeur � ajouter
	 */
	public void augmentStatistic(String gameName, String stat, double value);
	
	/**
	 * R�cup�re des donn�es joueurs sp�cialis�es.<br>
	 * Attentin, si la cl� � d�j� �t� charg�e avec une autre classe, peut provoquer une erreur.
	 * @param key La cl�
	 * @param clazz La classe
	 * @return Le PlayerData
	 */
	public <T extends PlayerData> T gameData(String key, Class<T> clazz);
	
	/**
	 * Renvoit les informations modifi�es � Ladder pour qu'elles soient sauvegard�es.
	 */
	public void saveData();
}
