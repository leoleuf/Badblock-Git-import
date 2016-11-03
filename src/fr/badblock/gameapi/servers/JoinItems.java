package fr.badblock.gameapi.servers;

import java.io.File;
import java.util.Map;

import fr.badblock.gameapi.players.kits.PlayerKit;
import fr.badblock.gameapi.run.BadblockGame;
import fr.badblock.gameapi.utils.itemstack.ItemEvent;
import fr.badblock.gameapi.utils.itemstack.ItemStackFactory;

/**
 * Classe permettant de g�rer les items donn�s au joueur leur du join. A
 * utiliser dans le onEnable() :o
 * 
 * @author LeLanN
 */
public interface JoinItems {
	/**
	 * Clear l'inventaire du joueur � la connection (par d�faut activ�)
	 */
	public void doClearInventory(boolean clear);

	/**
	 * A appeler lorsqu'il faut arr�ter les actions demand�es (par exemple,
	 * lorsque la partie commence)
	 */
	public void end();

	/**
	 * Demande � l'API de g�rer l'item permettant de voir les achievements
	 * 
	 * @param slot
	 *            Le slot
	 * @param game
	 *            Le jeu
	 */
	public void registerAchievementsItem(int slot, BadblockGame game);

	/**
	 * Demande � l'API de g�rer l'item de kit
	 * 
	 * @param slot
	 *            Le slot dans lequel mettre l'item � la connection
	 * @param kits
	 *            Les kits charg�s par le plgin
	 * @param kitListConfig
	 *            Le fichier de configuration pour savoir l'ordre des kits dans
	 *            l'inventaire
	 */
	public void registerKitItem(int slot, Map<String, PlayerKit> kits, File kitListConfig);

	/**
	 * Demande � l'API de g�rer l'item pour quitter la partie
	 * 
	 * @param slot
	 *            Le slot
	 * @param fallbackServer
	 *            Le serveur sur lequel t�l�porter le joueur
	 */
	public void registerLeaveItem(int slot, String fallbackServer);

	/**
	 * Demande � l'API de g�rer l'item de team (� appeler apr�s avoir charger
	 * les teams)
	 * 
	 * @param slot
	 *            Le slot
	 * @param teamListConfig
	 *            Le fichier de configuration pour savoir l'ordre des teams dans
	 *            l'inventaire
	 */
	public void registerTeamItem(int slot, File teamListConfig);

	/**
	 * Demande � l'API de g�rer l'item de vote (choix de la map)
	 * 
	 * @param slot
	 *            Le slot
	 */
	public void registerVoteItem(int slot);
	
	/**
	 * Met les options de jeu dans un sous-inventaire
	 * @param slot Le slot
	 * @param doVote Si le slot vote y va
	 * @param doTeam Si le slot team y va
	 * @param doKit Si le slot kit y va
	 * @param doAchiev Si le slot achievements y va
	 */
	public void registerGroupItem(int slot, boolean doVote, boolean doTeam, boolean doKit, boolean doAchiev);
	
	/**
	 * Demande � l'API de g�rer l'item VIP (affiche les avantages)
	 * @param slot Le slot
	 */
	public void registerVipItem(int slot);
	
	/**
	 * Demande � l'API de g�rer un item custom
	 * @param slot Le slot
	 * @param factory La factory pour cr�er l'item
	 * @param event L'event de l'item
	 */
	public void registerCustomItem(int slot, ItemStackFactory factory, ItemEvent event);
}
