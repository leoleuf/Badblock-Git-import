package fr.badblock.bukkit.ladder.permissions;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;

import fr.badblock.permissions.PermissiblePlayer;
import fr.badblock.permissions.Permission.Reponse;

public class BukkitPermissions extends PermissibleBase {
	private PermissiblePlayer player;
	
	public BukkitPermissions(Player player, PermissiblePlayer perm) {
		super(player);
		
		this.player = perm;
	}

	@Override
	public boolean hasPermission(Permission permission){
		return hasPermission(permission.getName());
	}
	
	@Override
	public boolean hasPermission(String permission){
		return player.hasPermission(permission);
	}
	
	@Override
	public boolean isPermissionSet(Permission permission){
		return isPermissionSet(permission.getName());
	}
	
	@Override
	public boolean isPermissionSet(String permission){
		return player.getPermissionReponse(permission) != Reponse.UNKNOW;
	}
}
