package fr.badblock.permissions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import fr.badblock.permissions.Permission.Reponse;
import lombok.Data;

@Data public class PermissiblePlayer implements Permissible {
	private final List<Permission>  permissions;

	private       String		    superGroup;
	private		  Map<String, Long>	alternateGroups;
	private		  long			    groupEnd;
	private	final String		    name;
	
	public PermissiblePlayer(String name, JsonObject from){
		this.name 		 = name;
		this.permissions = new ArrayList<>();

		reload(from);
	}
	
	public PermissiblePlayer(String name){
		this.name 		 = name;
		this.permissions = new ArrayList<>();
		
		this.groupEnd    = -1;
		this.superGroup  = "default";
	}
	
	public void reload(JsonObject from){
		if(!from.has("group"))
			from.addProperty("group", "default");
		if(!from.has("end"))
			from.addProperty("end", -1);
		if(!from.has("alternateGroups"))
			from.add("alternateGroups", new JsonObject());
		
		this.superGroup  	 = from.get("group").getAsString();
		this.groupEnd    	 = from.get("end").getAsLong();
		this.alternateGroups = new ConcurrentHashMap<>();
		
		for(Entry<String, JsonElement> entry : from.get("alternateGroups").getAsJsonObject().entrySet()){
			long end = entry.getValue().getAsLong();
			
			if(end > 0 && System.currentTimeMillis() >= end){
				continue;
			} else {
				alternateGroups.put(entry.getKey(), end);
			}
		}
		
		checkTimedout();
		
		permissions.clear();
		
		if(!from.has("permissions")) return;
		
		JsonArray perms  = from.get("permissions").getAsJsonArray();
		for(JsonElement element : perms){
			permissions.add(new Permission(element.getAsString()));
		}
		System.out.println("Foreach as json permissions (" + name + " - " + perms.toString() + " - reload(JsonObject))");
	}
	
	@Override
	public void addPermission(Permission permission) {
		permissions.add(permission);
	}

	@Override
	public void removePermission(Permission permission) {
		permissions.remove(permission);
	}
	
	public boolean permissionExist(Permission permission){
		for(final Permission perm : permissions){
			if(perm.equals(permission))
				return true;
		}
		
		return false;
	}

	@Override
	public boolean hasPermission(Permission permission) {
		return getPermissionReponse(permission) == Reponse.YES;
	}
	
	public boolean hasPermission(String permission){
		if(permission == null)
 			return true;
		return hasPermission(new Permission(permission));
	}

	public JsonElement getValue(String key){
		if(getParent() != null){
			JsonElement answer = ((PermissibleGroup) getParent()).getValue(key);
			
			if(answer != null)
				return answer;
		}
		
		for(String alternate : alternateGroups.keySet()){
			PermissibleGroup group = PermissionManager.getInstance().getGroup(alternate);
			
			JsonElement answer = group == null ? null : group.getValue(key);
			
			if(answer != null)
				return answer;
		}
		
		return null;
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
		
		if(reponse == Reponse.UNKNOW){
			reponse = getParent().getPermissionReponse(permission);
			
			for(String group : alternateGroups.keySet()){
				if(reponse != Reponse.UNKNOW) break;
				
				reponse = PermissionManager.getInstance().getGroup(group).getPermissionReponse(permission);
			}
		}
		
		return reponse;
	}
	
	public Reponse getPermissionReponse(String permission){
		return getPermissionReponse(new Permission(permission));
	}
	
	@Override
	public Permissible getParent() {
		checkTimedout();
		return PermissionManager.getInstance().getGroup(superGroup);
	}

	@Override
	public String getDisplayName() {
		return getParent().getDisplayName() + name;
	}
	
	public void checkTimedout(){
		if(groupEnd > 0 && System.currentTimeMillis() >= groupEnd){
			groupEnd   = -1;
			superGroup = "default";
		}
	}

	@Override
	public JsonObject saveAsJson() {
		checkTimedout();
		JsonObject object = new JsonObject();
		object.addProperty("group", superGroup);
		object.addProperty("end",   groupEnd);
		
		JsonArray array   = new JsonArray();

		for(Permission permission : permissions){
			array.add(new JsonPrimitive(permission.toString()));
		}
		System.out.println("Save as json permissions (" + name + " - " + array.toString() + " - saveAsJson())");
		
		object.add("permissions", array);
	
		JsonObject others = new JsonObject();
		
		for(Entry<String, Long> entry : alternateGroups.entrySet()){
			others.addProperty(entry.getKey(), entry.getValue());
		}
		
		object.add("alternateGroups", others);
		
		return object;
	}

	@Override
	public void setParent(long end, PermissibleGroup group) {
		if(alternateGroups.containsKey(group.getName().toLowerCase())) {
			alternateGroups.remove(group.getName().toLowerCase());
		}
		
		this.superGroup = group.getName().toLowerCase();
		this.groupEnd   = end;
		sort();
	}
	
	public void addParent(long end, PermissibleGroup group){
		if(superGroup == null || superGroup.equalsIgnoreCase("default")){
			setParent(end, group);
			return;
		}
		
		alternateGroups.put(group.getName().toLowerCase(), end);
		sort();
	}
	
	private void sort(){
		List<String> groups = new ArrayList<>(alternateGroups.keySet());
		
		groups.add(superGroup);
		Optional<String> first = groups.stream().sorted((a, b) -> Integer.compare(PermissionManager.getInstance().getGroup(b).getPower(), PermissionManager.getInstance().getGroup(a).getPower())).findFirst();
	
		if(!first.isPresent())
			return;
		
		String get = first.get();
		
		if(get == null || get.equalsIgnoreCase("default") || get.equalsIgnoreCase(superGroup))
			return;
		
		long previousEnd = groupEnd;
		String previous = superGroup;
		
		groupEnd = alternateGroups.get(get);
		superGroup = get;
		
		alternateGroups.remove(get);
		alternateGroups.put(previous, previousEnd);
	}
	
	public void removeParent(Permissible group){
		String n = group.getName().toLowerCase();
		
		if(superGroup.equals(n)){
			superGroup = "default"; groupEnd = -1;
		}
		
		if(alternateGroups.containsKey(n))
			alternateGroups.remove(n);
	}
	
	public void removeAll(){
		alternateGroups.clear();
		permissions.clear();
		
		setParent(-1, PermissionManager.getInstance().getGroup("default"));
		System.out.println("Remove all permissions ? " + name);
	}
}
