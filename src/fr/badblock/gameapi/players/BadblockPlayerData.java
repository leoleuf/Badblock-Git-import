package fr.badblock.gameapi.players;

import java.util.UUID;

import fr.badblock.gameapi.players.data.InGameData;
import fr.badblock.gameapi.players.data.PlayerData;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

/**
 * Repr�sente les donn�es d'un joueur
 * @author LeLanN
 */
public interface BadblockPlayerData {
	/**
	 * R�cup�re l'UUID du joueur
	 * @return L'UUID
	 */
	public UUID getUniqueId();
	
	/**
	 * R�cup�re le pseudo du joueur
	 * @return Le pseudo
	 */
	public String getName();
	
	/**
	 * R�cup�re les donn�es du joueur. Elles ne seront pas redemand�es � Ladder (elles n'ont th�oriquement pas chang�es)
	 * @return Les donn�es
	 */
	public PlayerData getPlayerData();
	
	/**
	 * R�cup�re le pr�fixe (par exemple [Admin]) pour afficher le nom du group du joueur
	 * @return Le pr�fixe
	 */
	public TranslatableString getGroupPrefix();
	
	/**
	 * R�cup�re le pr�fixe (par exemple [Admin]) pour afficher le nom du group du joueur en tablist
	 * @return Le pr�fixe
	 */
	public TranslatableString getTabGroupPrefix();
	
	/**
	 * R�cup�re les donn�es ingame du joueur, avant sa d�connection. Attention, la classe fournie doit avoir un constructeur sans arguments.
	 * 
	 * @param clazz La classe impl�mentant InGameData
	 * @return Les donn�es joueurs (ou null si la classe donn�e n'est pas correcte)
	 */
	public <T extends InGameData> T inGameData(Class<T> clazz);
	
	/**
	 * R�cup�re la team du joueur avant sa d�connection
	 * @return La team
	 */
	public BadblockTeam getTeam();
	
	/**
	 * D�finit la team du joueur. A ne pas utiliser pour la changer r�ellement, simplement une valeur de stockage.
	 * @param team La team
	 */
	public void setTeam(BadblockTeam team);
}
