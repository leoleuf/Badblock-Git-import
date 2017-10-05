package fr.badblock.permissions2;

import java.util.Map;

public interface PermissionsProvider
{
	public Map<String, Permissible> getGroups();
	
	public Permissible getPlayer(String name);
}
