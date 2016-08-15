package fr.badblock.api.permissions;

import org.bukkit.entity.Player;

public class NullPermissions extends AbstractPermissions {
	@Override
	public String getGroup(Player base){
		return "";
	}

	@Override
	public String getPrefix(Player base) {
		return "";
	}

	@Override
	public String getSuffix(Player base) {
		return "";
	}
	
	@Override
	public boolean hasPermission(Player base, String node){
		return base.hasPermission(node);
	}
}
