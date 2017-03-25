package fr.badblock.common.commons.permissions.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.gson.JsonElement;

import fr.badblock.common.commons.permissions.PermissionKey;
import fr.badblock.common.commons.permissions.Permissions;
import lombok.NoArgsConstructor;

/**
 * Représente un joueur
 * @author LeLanN
 */
@NoArgsConstructor
public class PermissiblePlayer extends PermissibleEntity {
	private List<String>             groups      = new ArrayList<>();
	private Map<String, JsonElement> permissions = new HashMap<>();
	
	@Override
	public Optional<JsonElement> getPermissionValue(String[] locations, String permission, boolean allowInheritance) {
		Optional<JsonElement> result = PermissionKey.getPermissionValue(permissions, permission, allowInheritance);
		
		if(result.isPresent())
			return result;
		
		for(PermissibleGroup group : getPlayerGroups()){
			if(!Permissions.permissions.areLocationsCompatible(group, locations))
				continue;
			
			result = group.getPermissionValue(locations, permission, allowInheritance);
			
			if(result.isPresent())
				break;
		}
		
		return result == null ? Optional.ofNullable(null) : result;
	}
	
	/**
	 * Récupère les groupes effectifs (indépendament des locations) du joueur, dans l'ordre de power décroissant
	 * @return Les groupes dans l'ordre de power décroissant
	 */
	public List<PermissibleGroup> getPlayerGroups(){
		Permissions perms = Permissions.permissions;
		
		List<PermissibleGroup> res = groups.parallelStream()
											  .filter(group -> perms.hasGroup(group))
											  .map(group -> Permissions.permissions.getGroupByName(group))
											  .sorted((a, b) -> Integer.compare( b.getGroupPower(), a.getGroupPower() ))
											  .collect(Collectors.toList());;
		
		if(res.isEmpty())
			res.add( perms.getGroupByName( Permissions.defaultGroup) );
		
		return res;
	}
	
	/**
	 * Récupère le groupe du joueur servant pour l'affichage (le plus haut en power & affichable)
	 * @return
	 */
	public PermissibleGroup getDisplayGroup(){
		for(PermissibleGroup group : getPlayerGroups()){
			if(!Permissions.permissions.areLocationsCompatible(group) || !group.isGroupDisplayable())
				continue;
			
			return group;
		}
		
		return Permissions.permissions.getGroupByName( Permissions.defaultGroup );
	}
}
