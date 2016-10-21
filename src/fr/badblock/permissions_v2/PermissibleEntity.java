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
	
	/**
	 * Récupère la valeur d'une permission pour l'entité, convertit en un certain type
	 * @param locations Les locations où l'on accepte de récupérer la permission
	 * @param permission La permission
	 * @param allowInheritance Si (par exemple) pour la clé badblock.example, la clé badblock.* peut être utilisée
	 * @param def La valeur de retour par défaut (si l'entité n'a pas la permission)
	 * @return La valeur de la permission
	 */
	public <T> T getPermissionValue(String[] locations, String permission, Class<T> as, boolean allowInheritance, T def){
		JsonElement result = getPermissionValue(locations, permission, allowInheritance, null);
		
		return result == null ? def : new Gson().fromJson(result, as);
	}
	
	/**
	 * Récupère la valeur d'une permission pour l'entité, convertit en un certain type, avec une valeur de retour par défaut à null
	 * @param locations Les locations où l'on accepte de récupérer la permission
	 * @param permission La permission
	 * @param allowInheritance Si (par exemple) pour la clé badblock.example, la clé badblock.* peut être utilisée
	 * @return La valeur de la permission
	 */
	public <T> T getPermissionValue(String[] locations, String permission, Class<T> as, boolean allowInheritance){
		return getPermissionValue(locations, permission, as, allowInheritance, null);
	}
	
	/**
	 * Récupère la valeur d'une permission pour l'entité, convertit en un certain type, avec un allowInheritance à false
	 * @param locations Les locations où l'on accepte de récupérer la permission
	 * @param permission La permission
	 * @param def La valeur de retour par défaut (si l'entité n'a pas la permission)
	 * @return La valeur de la permission
	 */
	public <T> T getPermissionValue(String[] locations, String permission, Class<T> as, T def){
		return getPermissionValue(locations, permission, as, false, def);
	}
	
	/**
	 * Récupère la valeur d'une permission pour l'entité, convertit en un certain type, avec un allowInheritance à false et sans valeur par défaut (null)
	 * @param locations Les locations où l'on accepte de récupérer la permission
	 * @param permission La permission
	 * @return La valeur de la permission
	 */
	public <T> T getPermissionValue(String[] locations, String permission, Class<T> as){
		return getPermissionValue(locations, permission, as, false, null);
	}
	
	/**
	 * Récupère une permission sous forme booléenne (équivalent à un système de permission style PEX)
	 * @param locations Les locations où l'on accepte de récupérer la permission
	 * @param permission La permission
	 * @return La valeur de la permission (par défaut, null)
	 */
	public boolean hasPermission(String[] locations, String permission){
		return getPermissionValue(locations, permission, Boolean.class, true, false);
	}
	
	/**
	 * Récupère la valeur d'une permission pour l'entité, convertit en un certain type
	 * <br>La location prend la valeur du manager de permission
	 * @param permission La permission
	 * @param allowInheritance Si (par exemple) pour la clé badblock.example, la clé badblock.* peut être utilisée
	 * @param def La valeur de retour par défaut (si l'entité n'a pas la permission)
	 * @return La valeur de la permission
	 */
	public <T> T getPermissionValue(String permission, Class<T> as, boolean allowInheritance, T def){
		return getPermissionValue(null, permission, as, allowInheritance, def);
	}
	
	/**
	 * Récupère la valeur d'une permission pour l'entité, convertit en un certain type, avec une valeur de retour par défaut à null
	 * <br>La location prend la valeur du manager de permission
	 * @param permission La permission
	 * @param allowInheritance Si (par exemple) pour la clé badblock.example, la clé badblock.* peut être utilisée
	 * @return La valeur de la permission
	 */
	public <T> T getPermissionValue(String permission, Class<T> as, boolean allowInheritance){
		return getPermissionValue(permission, as, allowInheritance, null);
	}
	
	/**
	 * Récupère la valeur d'une permission pour l'entité, convertit en un certain type, avec un allowInheritance à false
	 * <br>La location prend la valeur du manager de permission
	 * @param permission La permission
	 * @param def La valeur de retour par défaut (si l'entité n'a pas la permission)
	 * @return La valeur de la permission
	 */
	public <T> T getPermissionValue(String permission, Class<T> as, T def){
		return getPermissionValue(permission, as, false, def);
	}
	
	/**
	 * Récupère la valeur d'une permission pour l'entité, convertit en un certain type, avec un allowInheritance à false et sans valeur par défaut (null)
	 * <br>La location prend la valeur du manager de permission
	 * @param permission La permission
	 * @return La valeur de la permission
	 */
	public <T> T getPermissionValue(String permission, Class<T> as){
		return getPermissionValue(permission, as, false, null);
	}

	/**
	 * Récupère une permission sous forme booléenne (équivalent à un système de permission style PEX)
	 * <br>La location prend la valeur du manager de permission
	 * @param permission La permission
	 * @return La valeur de la permission (par défaut, null)
	 */
	public boolean hasPermission(String permission){
		return getPermissionValue(permission, Boolean.class, true, false);
	}
}
