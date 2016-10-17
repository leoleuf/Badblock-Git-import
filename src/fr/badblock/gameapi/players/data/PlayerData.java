package fr.badblock.gameapi.players.data;

import java.util.List;
import java.util.Optional;

import com.google.gson.JsonObject;

import fr.badblock.gameapi.achievements.PlayerAchievement;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.data.boosters.PlayerBooster;
import fr.badblock.gameapi.players.kits.PlayerKit;
import fr.badblock.gameapi.utils.i18n.Locale;

/**
 * R�presente les diff�rentes donn�es d'un joueur (coins/xp, kits, achievements,
 * stats, ...)
 * 
 * @author LeLanN
 */
public interface PlayerData {
	/**
	 * Ajoute des BadCoins au joueur
	 * 
	 * @param badcoins
	 *            Une valeur POSITIVE
	 * @param applyBonus
	 *            Si il faut appliquer les bonus joueur/serveur
	 * @return Le nombre de badcoins donn�s
	 */
	public int addBadcoins(int badcoins, boolean applyBonus);

	/**
	 * Ajoute de l'XP au joueur
	 * 
	 * @param xp
	 *            L'XP � ajouter
	 * @param applyBonus
	 *            Si il faut appliquer les bonus joueur/serveur
	 * @return Le nombre d'xp donn�
	 */
	public long addXp(long xp, boolean applyBonus);

	/**
	 * Ajoute des "shop points" au joueur
	 * 
	 * @param "shop points"
	 *            Le nombre de "shop points" � ajouter
	 * @return le nombre de "shop points" que le joueur aura alors :p
	 */
	public long addShopPoints(long shopPoints);

	/**
	 * V�rifie si le joueur peut obtenir le niveau suivant du kit (achievements
	 * et badcoins)
	 * 
	 * @param kit
	 *            Le kit
	 * @return Si il peut l'obtenir (si il est au niveau maximal, false)
	 */
	public boolean canUnlockNextLevel(PlayerKit kit);

	/**
	 * R�cup�re des donn�es joueurs sp�cialis�es.<br>
	 * Attentin, si la cl� � d�j� �t� charg�e avec une autre classe, peut
	 * provoquer une erreur.
	 * 
	 * @param key
	 *            La cl�
	 * @param clazz
	 *            La classe
	 * @return Le PlayerData
	 */
	public <T extends GameData> T gameData(String key, Class<T> clazz);

	/**
	 * R�cup�re l'avancement du joueur dans un achievement
	 * 
	 * @param achievement
	 *            L'achievement en question
	 * @return L'avancement du joueur (si pas d'avancement, un nouveau sera
	 *         cr��)
	 */
	public PlayerAchievementState getAchievementState(PlayerAchievement achievement);

	/**
	 * R�cup�re le nombre de BadCoins du joueur
	 * 
	 * @return Le nombre de BadCoins
	 */
	public int getBadcoins();
	
	/**
	 * R�cup�re le nombre de ShopPoints du joueur
	 * 
	 * @return Le nombre de ShopPoints
	 */
	public int getShopPoints();

	/**
	 * R�cup�re les boosters du joueur
	 * 
	 * @return les boosters du joueur dans une liste
	 */
	public List<PlayerBooster> getBoosters();

	/**
	 * R�cup�re un booster actif du joueur
	 * @return le booster (ou null, si aucun n'est activ�)
	 */
	public default PlayerBooster getActiveBooster(){
		Optional<PlayerBooster> res = getBoosters().stream().filter(booster -> booster.isEnabled() && !booster.isExpired()).findAny();
		
		return res.isPresent() ? res.get() : null;
	}
	
	/**
	 * R�cup�re le nom interne du dernier kit utilis� dans un jeu
	 * 
	 * @param game
	 *            Le nom (interne) du jeu
	 * @return Le nom interne du kit ou null si aucun
	 */
	public String getLastUsedKit(String game);

	/**
	 * R�cup�re le niveau du joueur
	 * 
	 * @return Le niveau du joueur
	 */
	public int getLevel();

	/**
	 * R�cup�re le langage choisit par le joueur.
	 * 
	 * @return Le langage.
	 */
	public Locale getLocale();

	/**
	 * R�cup�re une statistique du joueur
	 * 
	 * @param gameName
	 *            Le jeu
	 * @param stat
	 *            La statistique
	 * @return La valeur
	 */
	public double getStatistics(String gameName, String stat);

	/**
	 * R�cup�re le niveau que le joueur a pour un kit.
	 * 
	 * @param kit
	 *            Le kit
	 * @return Le niveau (0 = pas le Kit)
	 */
	public int getUnlockedKitLevel(PlayerKit kit);

	/**
	 * R�cup�re l'XP obtenue dans le niveau en cours (autrement dit,
	 * r�initialis�e � chaque niveau)
	 * 
	 * @return L'XP
	 */
	public long getXp();

	/**
	 * R�cup�re l'XP obtenue pour passer au niveau suivant (r�initialis�e �
	 * chaque niveau)
	 * 
	 * @return L'Xp n�cessaire
	 */
	public long getXpUntilNextLevel();

	/**
	 * Augment la statistique du joueur
	 * 
	 * @param gameName
	 *            Le jeu
	 * @param stat
	 *            La statistique
	 * @param value
	 *            La valeur � ajouter
	 */
	public void increaseStatistic(String gameName, String stat, double value);

	/**
	 * Augmente toutes les valeurs des achievements donn�s de 1
	 * 
	 * @param player
	 *            Le joueur pour tenter de valider les achievements
	 * @param achievements
	 *            Les achievements
	 */
	public void incrementAchievements(BadblockPlayer player, PlayerAchievement... achievements);

	/**
	 * Incr�mente (1) la statistique du joueur
	 * 
	 * @param gameName
	 *            Le jeu
	 * @param stat
	 *            La statistique
	 */
	public void incrementStatistic(String gameName, String stat);

	/**
	 * Supprime des BadCoins au joueur
	 * 
	 * @param badcoins
	 *            Une valeur POSITIVE
	 */
	public void removeBadcoins(int badcoins);

	/**
	 * Renvoit les informations modifi�es � Ladder pour qu'elles soient
	 * sauvegard�es.
	 * 
	 * @return L'objet
	 */
	public JsonObject saveData();

	/**
	 * D�finit le nom interne du dernier kit utilis� dans un jeu
	 * 
	 * @param game
	 *            Le nom (interne) du jeu
	 * @param kit
	 *            Le nom (interne) du kit
	 */
	public void setLastUsedKit(String game, String kit);

	/**
	 * D�bloque le niveau suivant du kit. Ne fonctionne pas si
	 * {@link #canUnlockNextLevel(PlayerKit)} retourne false.
	 * 
	 * @param kit
	 *            Le kit
	 */
	public void unlockNextLevel(PlayerKit kit);
}
