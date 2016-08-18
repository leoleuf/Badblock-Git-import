package fr.badblock.gameapi.players.data;

import com.google.gson.JsonObject;

import fr.badblock.gameapi.achievements.PlayerAchievement;
import fr.badblock.gameapi.players.kits.PlayerKit;
import fr.badblock.gameapi.utils.i18n.Locale;

/**
 * R�presente les donn�es du booster actif d'un joueur
 * @author LeLanN
 */
public interface BoosterData {
	
	/**
	 * R�cup�re le bonus en coins (achetable ou durant un �v�nement) du joueur.
	 * @return Le bonus (� multiplier par le nombre normal)
	 */
	public int getCoinsMultiplier();

	/**
	 * R�cup�re le bonus en XP (en fonction du niveau) du joueur.
	 * @return Le bonus (� multiplier par le nombre normal)
	 */
	public int getXPMultiplier();
	
	/**
	 * R�cup�re le timestamp d'expiration du booster.
	 * @return Le timestamp d'expiration du booster
	 */
	public long getExpireTime();

	/**
	 * R�cup�re le fait que le booster est encore valide ou non
	 * @return Un bool�en
	 */
	public boolean isValid();
	
	/**
	 * R�cup�re le fait que le booster est expir� ou non
	 * @return Un bool�en
	 */
	public boolean isExpired();
	
	/**
	 * R�cup�re des donn�es joueurs sp�cialis�es.<br>
	 * Attentin, si la cl� � d�j� �t� charg�e avec une autre classe, peut provoquer une erreur.
	 * @param key La cl�
	 * @param clazz La classe
	 * @return Le PlayerData
	 */
	public <T extends GameData> T gameData(String key, Class<T> clazz);
	
	/**
	 * Renvoit les informations modifi�es � Ladder pour qu'elles soient sauvegard�es.
	 * @return L'objet
	 */
	public JsonObject saveData();
}
