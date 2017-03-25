package fr.badblock.common.commons.permissions;

import java.util.Map;

import fr.badblock.common.commons.permissions.entities.PermissibleGroup;
import fr.badblock.common.commons.utils.Callback;
import lombok.Getter;

public class Permissions {
	public static final Permissions permissions  = new Permissions();
	public static final String      defaultGroup = "default";
	
	@Getter
	private PermissionProvider permissionProvider;
	@Getter
	private Map<String, PermissibleGroup> groups;
	
	/**
	 * Change le provider de permissions
	 * @param provider Le provider
	 */
	public void changePermissionProvider(PermissionProvider provider){
		this.permissionProvider = provider;
		
		provider.loadGroups(new Callback<Map<String,PermissibleGroup>>() {
			@Override
			public void done(Map<String, PermissibleGroup> result, Throwable error) {
				if(error != null){
					error.printStackTrace();
					return;
				}
				
				groups = result;
			}
		});
	}
	
	/**
	 * Vérifie si le groupe existe
	 * @param group Si le groupe existe
	 * @return Si le groupe existe
	 */
	public boolean hasGroup(String group){
		return groups.containsKey( group.toLowerCase() );
	}
	
	/**
	 * Récupère un groupe en fonction de son nom
	 * @param group Le groupe
	 * @return Le nom du groupe
	 */
	public PermissibleGroup getGroupByName(String group){
		return groups.get( group.toLowerCase() );
	}
	
	/**
	 * Vérifie si le groupe donné est le groupe par défaut
	 * @param group Le nom du groupe
	 * @return Si le groupe est le groupe par défaut
	 */
	public boolean isGroupDefault(String group){
		return group == null || group.isEmpty() || group.equalsIgnoreCase(defaultGroup) || !hasGroup(group);
	}
	
	/**
	 * Vérifie si le groupe donné est le groupe par défaut
	 * @param group Le nom du groupe
	 * @return Si le groupe est le groupe par défaut
	 */
	public boolean isGroupDefault(PermissibleGroup group){
		return group == null || isGroupDefault(group.getInternalName());
	}
	
	/**
	 * Vérifie si deux locations sont compatibles
	 * @param locations1 La première
	 * @param locations2 La seconde
	 * @return Si les deux locations sont compatibles
	 */
	public boolean areLocationsCompatible(String[] locations1, String[] locations2){
		
		for(String contains : locations1){
			if(contains.equals("_") || contains.equals("*"))
				return true;
			
			for(String comp : locations2)
				if(contains.equalsIgnoreCase(comp))
					return true;
		}
		
		return false;
		
	}
	
	/**
	 * Vérifie si les deux locations sont compatibles
	 * @param group La première (sous forme de groupe)
	 * @param locations La seconde
	 * @return Si les deux locations sont compatibles
	 */
	public boolean areLocationsCompatible(PermissibleGroup group, String[] locations){
		return areLocationsCompatible(group.getDefinitionDomain(), locations);
	}
	
	/**
	 * Vérifie si le groupe est compatible avec l'instance des permissions
	 * @param group Le groupe
	 * @return Si le groupe est compatible
	 */
	public boolean areLocationsCompatible(PermissibleGroup group){
		return areLocationsCompatible(group.getDefinitionDomain(), getPermissionProvider().getLocations());
	}
}
