package fr.badblock.gameapi.servers;

import java.io.File;
import java.util.Map;

import fr.badblock.gameapi.players.kits.PlayerKit;
import fr.badblock.gameapi.run.BadblockGame;

/**
 * Classe permettant de g�rer les items donn�s au joueur leur du join. A utiliser dans le onEnable() :o
 * @author LeLanN
 */
public interface JoinItems {
	/**
	 * Demande � l'API de g�rer l'item de kit
	 * @param slot Le slot dans lequel mettre l'item � la connection
	 * @param kits Les kits charg�s par le plgin
	 * @param kitListConfig Le fichier de configuration pour savoir l'ordre des kits dans l'inventaire
	 */
	public void registerKitItem(int slot, Map<String, PlayerKit> kits, File kitListConfig);

	/**
	 * Demande � l'API de g�rer l'item de team (� appeler apr�s avoir charger les teams)
	 * @param slot Le slot
	 * @param teamListConfig Le fichier de configuration pour savoir l'ordre des teams dans l'inventaire
	 */
	public void registerTeamItem(int slot, File teamListConfig);
	
	/**
	 * Demande � l'API de g�rer l'item de vote (choix de la map)
	 * @param slot Le slot
	 */
	public void registerVoteItem(int slot);
	
	/**
	 * Demande � l'API de g�rer l'item permettant de voir les achievements
	 * @param slot Le slot
	 * @param game Le jeu
	 */
	public void registerAchievementsItem(int slot, BadblockGame game);
	
	/**
	 * Demande � l'API de g�rer l'item pour quitter la partie
	 * @param slot Le slot
	 * @param fallbackServer Le serveur sur lequel t�l�porter le joueur
	 */
	public void registerLeaveItem(int slot, String fallbackServer);
	
	/**
	 * Clear l'inventaire du  joueur � la connection (par d�faut activ�)
	 */
	public void doClearInventory(boolean clear);
	
	/**
	 * A appeler lorsqu'il faut arr�ter les actions demand�es (par exemple, lorsque la partie commence)
	 */
	public void end();
}
