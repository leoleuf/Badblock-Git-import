package fr.badblock.gameapi.databases;

import com.google.gson.JsonObject;

import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.utils.general.Callback;

/**
 * Classe permettant de communiquer de mani�re simple avec Redis.
 * 
 * @author xMalware
 */
public interface RedisSpeaker {
	
	/**
	 * Demande les permissions � Redis.
	 */
	public void askForPermissions();

	/**
	 * R�cup�re les donn�es associ�es � l'IP d'un joueur
	 * 
	 * @param player
	 *            Le joueur
	 * @param callback
	 *            Appeler lorsque les donn�es sont re�ues
	 */
	public void getIpPlayerData(BadblockPlayer player, Callback<JsonObject> callback);

	/**
	 * R�cup�re les donn�es d'un joueur
	 * 
	 * @param player
	 *            Le joueur
	 * @param callback
	 *            Appeler lorsque les donn�es sont re�ues
	 */
	public void getPlayerData(BadblockPlayer player, Callback<JsonObject> callback);

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
	 * Envoit une proposition (ou annulation) pour revenir � la partie
	 * 
	 * @param name
	 *            Le joueur
	 * @param invited
	 *            Si le joueur peut encore venir
	 */
	public void sendReconnectionInvitation(String name, boolean invited);

	/**
	 * Change les donn�es associ�es � l'IP d'un joueur
	 * 
	 * @param player
	 *            Le joueur
	 * @param toUpdate
	 *            Les donn�es � update (ne doit pas n�cessairement �tre complet)
	 */
	public void updateIpPlayerData(BadblockPlayer player, JsonObject toUpdate);

	/**
	 * Change les donn�es d'un joueur
	 * 
	 * @param player
	 *            Le joueur
	 * @param toUpdate
	 *            Les donn�es � update (ne doit pas n�cessairement �tre complet)
	 */
	public void updatePlayerData(BadblockPlayer player, JsonObject toUpdate);

}
