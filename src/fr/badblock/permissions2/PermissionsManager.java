package fr.badblock.permissions2;

import java.util.Map;

import lombok.Getter;

public class PermissionsManager
{
	@Getter
	private static PermissionsManager manager;

	public static void createPermissionManager(PermissionsProvider provider, String place)
	{
		if(manager != null)
			new IllegalStateException("Permission manager already created!");
		
		manager = new PermissionsManager(provider, place);
	}
	
	@Getter
	private String currentPlace;
	private PermissionsProvider provider;
	private Map<String, Permissible> groups;
	
	private PermissionsManager(PermissionsProvider provider, String place)
	{
		this.provider = provider;
		this.currentPlace = place;
		this.groups = provider.getGroups();
	}
	
	public void reloadGroups()
	{
		this.groups = provider.getGroups();
	}
	
	public Permissible getGroupPermissible(String name)
	{
		return groups.get(name);
	}
	
	public Permissible getPlayerPermissible(String name)
	{
		return provider.getPlayer(name);
	}
}
