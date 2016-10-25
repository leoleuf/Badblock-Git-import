package fr.badblock.permissions;

import java.util.List;

import com.google.gson.JsonObject;

import fr.badblock.permissions.Permission.Reponse;

public interface Permissible {
	public void    		 addPermission       (Permission... permission);
	public void    		 addPermission       (List<Permission> permission);
	public void    		 removePermission    (Permission permission);
	
	public boolean 		 hasPermission       (Permission permission);
	public Reponse		 getPermissionReponse(Permission permission);
	
	public Permissible 	 getParent();
	public void          setParent(long end, PermissibleGroup group);
	
	
	public String		 getName();
	public String		 getDisplayName();

	public JsonObject    saveAsJson();
}