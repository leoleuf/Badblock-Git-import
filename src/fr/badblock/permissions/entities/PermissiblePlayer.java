package fr.badblock.permissions.entities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.gson.JsonElement;

import fr.badblock.permissions.PermissionKey;
import fr.badblock.permissions.Permissions;
import lombok.NoArgsConstructor;

/**
 * Représente un joueur
 * @author LeLanN
 */
@NoArgsConstructor
public class PermissiblePlayer extends PermissibleEntity {
	private Map<String, Long>        groups      = new HashMap<>();
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
	 * Vérifie si les groupes temporaires du joueur sont terminés, auquel cas ils seront enlevés
	 */
	public void checkEndedGroups(){
		Set<Entry<String, Long>> toRemoveGroups = groups.entrySet().parallelStream().filter(entry -> entry.getValue() < System.currentTimeMillis() && entry.getValue() > 0).collect(Collectors.toSet());
	
		for(Entry<String, Long> toRemove : toRemoveGroups)
			groups.remove(toRemove.getKey());
	}
	
	/**
	 * Récupère les groupes effectifs (indépendament des locations) du joueur, dans l'ordre de power décroissant
	 * @return Les groupes dans l'ordre de power décroissant
	 */
	public List<PermissibleGroup> getPlayerGroups(){
		checkEndedGroups();
		
		Permissions perms = Permissions.permissions;
		
		List<PermissibleGroup> res = groups.keySet().parallelStream()
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
