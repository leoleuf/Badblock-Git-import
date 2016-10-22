package fr.badblock.permissions_v2;

import java.util.Map;

import fr.badblock.permissions_v2.entities.PermissibleGroup;
import fr.badblock.permissions_v2.entities.PermissiblePlayer;
import fr.badblock.utils.Callback;

/**
 * Représente un outil permettant de load les permissions
 * @author LeLanN
 */
public interface PermissionProvider {
	/**
	 * Les locations gérées par le provider
	 * @return Les locations
	 */
	public String[] getLocations();
	
	/**
	 * Load l'ensemble des groupes
	 * @param callback Le callback pour récupérer
	 */
	public void loadGroups(Callback<Map<String, PermissibleGroup>> callback);
	
	/**
	 * Load un joueur
	 * @param name Le nom du joueur
	 * @param callback Le callback pour récupérer
	 */
	public void loadPlayer(String name, Callback<PermissiblePlayer> callback);
}
