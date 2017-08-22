package fr.badblock.permissions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import fr.badblock.permissions.Permission.Reponse;
import lombok.Data;

@Data public class PermissibleGroup implements Permissible {
	private final List<Permission>         permissions;
	private final Map<String, JsonElement> values;
	private       String		           superGroup;
	private	final String		           name;
	private		  String		           displayName;
 	private 	  int			           power;
	private		  boolean				   staff;
 	
	public PermissibleGroup(JsonObject from){
		this.name 		 = from.get("name").getAsString();
		this.displayName = from.get("displayName").getAsString();

 		if(!from.has("power")){
 			   this.power = 0;
 		} else this.power = from.get("power").getAsInt();
 		if(!from.has("staff")){
			   this.staff = false;
		} else this.staff = from.get("staff").getAsBoolean();
		if(from.get("superGroup") == null){
			from.addProperty("superGroup", "default");
		}
		
		values = new HashMap<>();
		
		if(from.has("values")){
			JsonObject vals = from.get("values").getAsJsonObject();
			vals.entrySet().forEach(o -> values.put(o.getKey().toLowerCase(), o.getValue()));
		}
		
		
		if(!from.get("superGroup").isJsonNull())
			this.superGroup  = from.get("superGroup").getAsString();
		else this.superGroup = null;
		
		this.permissions = new ArrayList<>();
		
		if(superGroup != null && superGroup.equalsIgnoreCase(name))
			this.superGroup = null;
		
		JsonArray perms  = from.get("permissions").getAsJsonArray();
		for(JsonElement element : perms){
			if(!element.isJsonNull())
				permissions.add(new Permission(element.getAsString()));
		}
	}

	@Override
	public void addPermission(Permission... permission) {
		addPermission(Arrays.asList(permission));
	}
	
	@Override
	public void addPermission(List<Permission> permission) {
		permissions.forEach(perm -> permission.add(perm));
	}

	@Override
	public void removePermission(Permission permission) {
		permissions.remove(permission);
	}

	@Override
	public boolean hasPermission(Permission permission) {
		return getPermissionReponse(permission) == Reponse.YES;
	}

	@Override
	public Reponse getPermissionReponse(Permission permission) {
		Reponse reponse = Reponse.UNKNOW;
		
		for(final Permission perm : permissions){
			Reponse result = perm.has(permission);
			if(result == Reponse.NO){
				reponse = Reponse.NO;
			} else if(result == Reponse.YES && reponse == Reponse.UNKNOW){
				reponse = Reponse.YES;
			}
		}
		
		if(reponse == Reponse.UNKNOW && !name.equalsIgnoreCase("default")){
			reponse = getParent().getPermissionReponse(permission);
		}
		
		return reponse;
	}
	
	public boolean permissionExist(Permission permission){
		for(final Permission perm : permissions){
			if(perm.equals(permission))
				return true;
		}
		
		return false;
	}
	
	@Override
	public Permissible getParent() {
		return PermissionManager.getInstance().getGroup(superGroup);
	}
	
	@Override
	public JsonObject saveAsJson() {
		JsonObject object = new JsonObject();
		object.addProperty("name",        name);
		object.addProperty("displayName", displayName);
		object.addProperty("superGroup",  superGroup);
 		object.addProperty("power",  	  power);
 		object.addProperty("staff",  	  staff);
		
		JsonArray array   = new JsonArray();

		for(Permission permission : permissions){
			array.add(new JsonPrimitive(permission.toString()));
		}
		
		JsonObject obj   = new JsonObject();
		values.forEach((key, val) -> obj.add(key, val));
 		object.add("values",  	  obj);
		
		object.add("permissions", array);
		
		return object;
	}
	
	public JsonElement getValue(String key){
		key = key.toLowerCase();
		
		JsonElement el = values.get(key);
		
		if(el == null && superGroup != null && !superGroup.equalsIgnoreCase("default")){
			PermissibleGroup group = PermissionManager.getInstance().getGroup(superGroup);
		
			if(group != null)
				return group.getValue(key);
		}
		
		return el;
	}

	@Override
	public void setParent(long end, PermissibleGroup group) {
		this.superGroup = group.getName().toLowerCase();
	}
	
}
