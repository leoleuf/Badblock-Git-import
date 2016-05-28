package fr.badblock.gameapi.players.kits;

import com.google.gson.JsonObject;

import fr.badblock.gameapi.players.BadblockPlayer;

/**
 * Une interface repr�sentant simplement un manager de contenu du Kit (uniquement les items, en th�orie).<br>
 * Doit �tre fournit par le jeu (onEnable) via {@link fr.badblock.gameapi.GameAPI#setKitContentManager(PlayerKitContentManager)}.
 * 
 * @author LeLanN
 */
public interface PlayerKitContentManager {
	/**
	 * Permet de donner � un joueur un kit
	 * @param content Le contenu
	 * @param player Le joueur
	 */
	public void give(JsonObject content, BadblockPlayer player);
	
	/**
	 * Permet de r�cup�re des donn�es depuis l'inventaire d'un joueur
	 * @param player Le joueur
	 * @return Les donn�es cr��es
	 */
	public JsonObject createFromInventory(BadblockPlayer player);
}
