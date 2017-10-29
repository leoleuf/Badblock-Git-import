package fr.badblock.gameapi.databases;

import com.google.gson.JsonObject;

import fr.badblock.gameapi.game.GameState;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.utils.general.Callback;

/**
 * Classe permettant de communiquer de maničre simple avec Ladder.
 * 
 * @author LeLanN
 */
public interface LadderSpeaker {
	/**
	 * Demande les permissions ŕ Ladder.
	 */
	public void askForPermissions();

	/**
	 * Execute une commande sur Ladder en tant que joueur.
	 * 
	 * @param player
	 *            Le joueur
	 * @param command
	 *            La commande
	 */
	public void executeCommand(BadblockPlayer player, String command);

	/**
	 * Execute une commande sur Ladder en tant que console.
	 * 
	 * @param command
	 *            La commande
	 */
	public void executeCommand(String command);

	/**
	 * Récupčre les données associées ŕ l'IP d'un joueur
	 * 
	 * @param player
	 *            Le joueur
	 * @param callback
	 *            Appeler lorsque les données sont reçues
	 */
	public void getIpPlayerData(BadblockPlayer player, Callback<JsonObject> callback);

	/**
	 * Récupčre les données d'un joueur
	 * 
	 * @param player
	 *            Le joueur
	 * @param callback
	 *            Appeler lorsque les données sont reçues
	 */
	public void getPlayerData(BadblockPlayer player, Callback<JsonObject> callback);

	/**
	 * Récupčre les données d'un joueur
	 * 
	 * @param player
	 *            Le joueur
	 * @param callback
	 *            Appeler lorsque les données sont reçues
	 */
	public void getPlayerData(String player, Callback<JsonObject> callback);

	/**
	 * Envoit un packet de keepalive ŕ ladder
	 * 
	 * @param state
	 *            La state actuelle
	 * @param current
	 *            Le nombre de joueurs actuel
	 * @param max
	 *            Le nombre maximum de joueurs
	 */
	public void keepAlive(GameState state, int current, int max);

	/**
	 * Envoit une demande de nombre de joueurs
	 * 
	 * @param servers
	 *            Les serveurs (* = tous)
	 * @param count
	 *            Les joueurs
	 */
	public void sendPing(String[] servers, Callback<Integer> count);

	/***
	 * Envoit une proposition (ou annulation) pour revenir ŕ la partie
	 * 
	 * @param name
	 *            Le joueur
	 * @param invited
	 *            Si le joueur peut encore venir
	 */
	public void sendReconnectionInvitation(String name, boolean invited);

	/**
	 * Change les données associées ŕ l'IP d'un joueur
	 * 
	 * @param player
	 *            Le joueur
	 * @param toUpdate
	 *            Les données ŕ update (ne doit pas nécessairement ętre complet)
	 */
	public void updateIpPlayerData(BadblockPlayer player, JsonObject toUpdate);

	/**
	 * Change les données d'un joueur
	 * 
	 * @param player
	 *            Le joueur
	 * @param toUpdate
	 *            Les données ŕ update (ne doit pas nécessairement ętre complet)
	 */
	public void updatePlayerData(BadblockPlayer player, JsonObject toUpdate);

	/**
	 * Change les données d'un joueur
	 * 
	 * @param player
	 *            Le joueur
	 * @param toUpdate
	 *            Les données ŕ update (ne doit pas nécessairement ętre complet)
	 */
	public void updatePlayerData(String player, JsonObject toUpdate);
	
	public void broadcast(String... messages);
	
}
