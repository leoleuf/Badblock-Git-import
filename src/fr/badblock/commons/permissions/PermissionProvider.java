package fr.badblock.commons.permissions;

import java.util.Map;

import fr.badblock.commons.permissions.entities.PermissibleGroup;
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
}
