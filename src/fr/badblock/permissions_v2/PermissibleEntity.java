package fr.badblock.permissions_v2;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public abstract class PermissibleEntity {
	/**
	 * Récupère la valeur d'une permission pour l'entité
	 * @param locations Les locations où l'on accepte de récupérer la permission
	 * @param permission La permission
	 * @param allowInheritance Si (par exemple) pour la clé badblock.example, la clé badblock.* peut être utilisée
	 * @param def La valeur de retour par défaut (si l'entité n'a pas la permission)
	 * @return La valeur de la permission
	 */
	public abstract JsonElement getPermissionValue(String[] locations, String permission, boolean allowInheritance, JsonElement def);
	
	public <T> T getPermissionValue(String[] locations, String permission, Class<T> as, boolean allowInheritance, T def){
		JsonElement result = getPermissionValue(locations, permission, allowInheritance, null);
		
		return result == null ? def : new Gson().fromJson(result, as);
	}
	
	public <T> T getPermissionValue(String[] locations, String permission, Class<T> as, boolean allowInheritance){
		return getPermissionValue(locations, permission, as, allowInheritance, null);
	}
	
	public <T> T getPermissionValue(String[] locations, String permission, Class<T> as, T def){
		return getPermissionValue(locations, permission, as, false, def);
	}
	
	public boolean hasPermission(String[] locations, String permission){
		return getPermissionValue(locations, permission, Boolean.class, true, false);
	}
	
	public <T> T getPermissionValue(String permission, Class<T> as, boolean allowInheritance, T def){
		return getPermissionValue(null, permission, as, allowInheritance, def);
	}
	
	public <T> T getPermissionValue(String permission, Class<T> as, boolean allowInheritance){
		return getPermissionValue(permission, as, allowInheritance, null);
	}
	
	public <T> T getPermissionValue(String permission, Class<T> as, T def){
		return getPermissionValue(permission, as, false, def);
	}
	
	public boolean hasPermission(String permission){
		return getPermissionValue(permission, Boolean.class, true, false);
	}
}
