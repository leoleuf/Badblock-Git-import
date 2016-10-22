package fr.badblock.permissions_v2.entities;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.google.gson.JsonElement;

import fr.badblock.permissions_v2.PermissionKey;
import fr.badblock.permissions_v2.Permissions;
import lombok.NoArgsConstructor;

/**
 * Représente un groupe
 * @author LeLanN
 */
@NoArgsConstructor
public class PermissibleGroup extends PermissibleEntity {
	private String					 internal_name			   = "default";
	private String 					 inherit_permissions_from  = "default";
	private String[]			     defined_on				   = new String[] { "_" };
	private Map<String, JsonElement> permissions			   = new HashMap<>();
	
	@Override
	public Optional<JsonElement> getPermissionValue(String[] locations, String permission, boolean allowInheritance) {
		Optional<JsonElement> result = PermissionKey.getPermissionValue(permissions, permission, allowInheritance);
		
		if(result.isPresent())
			return result;
		
		return isDefault() ? Permissions.permissions.getGroupByName(getInheritance()).getPermissionValue(locations, permission, allowInheritance) : Optional.ofNullable(null);
	}		

	/**
	 * Récupère le nom interne du groupe (ne sera pas affiché au joueur)
	 * @return Le nom
	 */
	public String getInternalName(){
		return internal_name;
	}
	
	/**
	 * Récupère le groupe parent
	 * @return Le groupe parent
	 */
	public String getInheritance(){
		return inherit_permissions_from;
	}
	
	/**
	 * Vérifie si le groupe est celui par défaut
	 * @return Si il est le groupe par défaut
	 */
	public boolean isDefault(){
		return Permissions.permissions.isGroupDefault( this );
	}
	
	/**
	 * Récupère les locations pour lequel le groupe est définit
	 * @return Les locations
	 */
	public String[] getDefinitionDomain(){
		return defined_on;
	}
	
	/**
	 * Récupère le power du groupe
	 * @return Le power
	 */
	public int getGroupPower(){
		return getPermissionValue(null, "group_power", Integer.class, false, 0);
	}
	
	/**
	 * Récupère le power du groupe
	 * @return Le power
	 */
	public boolean isGroupDisplayable(){
		return getPermissionValue(null, "group_displayable", Boolean.class, false, false);
	}
}
