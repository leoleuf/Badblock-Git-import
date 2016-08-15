package fr.badblock.permissions;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import lombok.Getter;

public class PermissionManager {
	@Getter private static PermissionManager 	  instance;
	
	protected final Map<String, PermissibleGroup> groups;
	
	public PermissionManager(JsonArray array){
		instance = this;
		groups   = new HashMap<>();

		for(int i=0;i<array.size();i++){
			JsonObject       object = array.get(i).getAsJsonObject();
			PermissibleGroup group  = new PermissibleGroup(object);
			
			groups.put(group.getName().toLowerCase(), group);
		}
		
		if(!groups.containsKey("default")){
			createGroup("default", "default");
		}
	}
	
	public Collection<String> getGroupsName() {
	    return Collections.unmodifiableCollection(this.groups.keySet());
	}
	
	public Collection<PermissibleGroup> getGroups() {
	    return Collections.unmodifiableCollection(this.groups.values());
	}
	
	public PermissibleGroup getGroup(String group){
		if(group == null || !groups.containsKey(group.toLowerCase()))
			return groups.get("default");
		return groups.get(group.toLowerCase());
	}
	
	public PermissibleGroup createGroup(String name, String parent){
		JsonObject object = new JsonObject();
		object.addProperty("name"       , name);
		object.addProperty("displayName", name);
		object.addProperty("superGroup" , parent);
		
		object.add("permissions"        , new JsonArray());
	
		PermissibleGroup group = new PermissibleGroup(object);
		groups.put(name.toLowerCase(), group);
		
		return group;
	}
	
	public void removeGroup(String name){
		groups.remove(name.toLowerCase());
	}
	
	public JsonArray saveAsJson(){
		JsonArray result = new JsonArray();
		for(PermissibleGroup group : groups.values())
			result.add(group.saveAsJson());
		
		return result;
	}
	
	public PermissiblePlayer createPlayer(String name, JsonObject object){
		JsonObject permissions = new JsonObject();
		if(object.has("permissions"))
			permissions = object.get("permissions").getAsJsonObject();

		return new PermissiblePlayer(name, permissions);
	}
}
