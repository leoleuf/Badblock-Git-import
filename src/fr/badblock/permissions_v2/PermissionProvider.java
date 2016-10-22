package fr.badblock.permissions_v2;

import java.util.Map;

import fr.badblock.permissions_v2.entities.PermissibleGroup;
import fr.badblock.permissions_v2.entities.PermissiblePlayer;

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
	 * @return Les groupes
	 */
	public Map<String, PermissibleGroup> loadGroups();
	
	/**
	 * Load un joueur
	 * @param name Le nom du joueur
	 * @return Le joueur
	 */
	public PermissiblePlayer loadPlayer(String name);
	
}
