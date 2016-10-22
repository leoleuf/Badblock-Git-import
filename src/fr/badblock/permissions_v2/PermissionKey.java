package fr.badblock.permissions_v2;

import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import com.google.gson.JsonElement;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Représente une clé de permission
 * @author LeLanN
 */
@Getter
@EqualsAndHashCode
public class PermissionKey {
	private String  permission;
	private boolean all;
	
	public PermissionKey(String permission){
 		if(permission == null){
 			this.all		= false;
 			this.permission	= "";
 			
 			return;
 		}
 		
		if(permission.endsWith("*")){
			all = true;
			permission = permission.substring(0, permission.length() - 1);
		} else all = false;
		
		if(permission.endsWith("."))
			permission = permission.substring(0, permission.length() - 1);

		this.permission = permission.toLowerCase();
	}
	
	/**
	 * Vérifie si les deux permissions sont compatibles
	 * @param permission La permission
	 * @param allowInheritance Si (par exemple) pour la clé badblock.example, la clé badblock.* peut être utilisée
	 * @return Si elles sont compatibles
	 */
	public boolean isPermissionCompatibleWith(PermissionKey permission, boolean allowInheritance){
		if(getPermission().equalsIgnoreCase( permission.getPermission() )){
			return isAll() == permission.isAll(); // badblock.* & badblock non compatible ici
		}
		
		if(getPermission().startsWith( permission.getPermission() )){
			return isAll() && allowInheritance;
		}
		
		return false;
	}
	
	@Override
	public String toString(){
		return permission + (all ? (permission.isEmpty() ? "*" : ".*") : "");
	}
	
	/**
	 * Récupère (par rapport à la permission donnée) la valeur dont la key correspond le plus
	 * @param permissions Les permissions
	 * @param permission La permission recherchée
	 * @param allowInheritance Si (par exemple) pour la clé badblock.example, la clé badblock.* peut être utilisée
	 * @return Un optional de l'élément trouvé
	 */
	public static Optional<JsonElement> getPermissionValue(Map<String, JsonElement> permissions, String permission, boolean allowInheritance){
		PermissionKey oPerm = new PermissionKey(permission);
		
		Optional< Entry<String, JsonElement> > result = permissions.entrySet().parallelStream().filter(entry -> {
			return new PermissionKey( entry.getKey() ).isPermissionCompatibleWith(oPerm, allowInheritance);
		}).sorted( new CompatiblePermissionComparator(oPerm) ).findFirst();
		
		return result.isPresent() ? Optional.of( result.get().getValue() ) : Optional.ofNullable( (JsonElement) null );
	}
	
	@AllArgsConstructor
	private static class CompatiblePermissionComparator implements Comparator< Entry<String, JsonElement> > {
		private PermissionKey original;
		
		@Override
		public int compare(Entry<String, JsonElement> a, Entry<String, JsonElement> b) {
			PermissionKey first  = new PermissionKey( a.getKey() );
			PermissionKey second = new PermissionKey( b.getKey() );
			
			String fPerm = first.getPermission();
			String sPerm = second.getPermission();
			
			return fPerm.equals(original.getPermission()) || sPerm.startsWith(fPerm) ? 1 : -1;
		}
		
	}
}
