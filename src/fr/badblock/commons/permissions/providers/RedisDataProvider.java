package fr.badblock.commons.permissions.providers;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import fr.badblock.commons.data.PlayerData;
import fr.badblock.commons.permissions.PermissionProvider;
import fr.badblock.commons.permissions.entities.PermissibleGroup;
import fr.badblock.commons.technologies.redis.RedisConnector;
import fr.badblock.commons.technologies.redis.RedisService;
import fr.badblock.commons.utils.Callback;

public class RedisDataProvider implements PermissionProvider {
	private String[]	 locations;
	private RedisService service;
	
	private Map<String, JsonObject> loadedPlayers = new HashMap<String, JsonObject>();
	
	public RedisDataProvider(String[] locations, String host, int port, String password) {
		this.locations = locations;
		this.service   = RedisConnector.getInstance().newService("permissions_fetcher", host, port, password);
	}
	
	@Override
	public String[] getLocations() {
		return locations;
	}
	
	@Override
	public void loadGroups(Callback<Map<String, PermissibleGroup>> callback) {
		service.getAsyncObject("badblock.permissions", callback);
	}

	public void loadPlayer(String name, Callback<JsonObject> callback) {
		service.getAsyncObject("badblock.player." + name.toLowerCase(), new Callback<JsonObject>() {
			@Override
			public void done(JsonObject result, Throwable error) {
				if(error != null){
					loadedPlayers.put(name, result);
				}
				
				callback.done(result, error);
			}
		});
	}
	
	public void sendFullPlayer(String name, JsonObject object){
		service.setAsync("badblock.player." + name, object.toString());
	}
	
	public void sendPlayer(String name, PlayerData data){
		
		JsonObject toSend = new Gson().toJsonTree(data).getAsJsonObject();
		
		if(loadedPlayers.containsKey(name.toLowerCase())){
			toSend = addObjectInObject(loadedPlayers.get(name.toLowerCase()), toSend);
			loadedPlayers.remove( name.toLowerCase() );
		}
		
		sendFullPlayer(name, toSend);
		
	}
	
	private JsonObject addObjectInObject(JsonObject base, JsonObject toAdd){
		for(final Entry<String, JsonElement> entry : toAdd.entrySet()){
			String key = entry.getKey();
			JsonElement element = toAdd.get(key);

			if(!base.has(key) || !element.isJsonObject()){
				base.add(key, element);
			} else {
				addObjectInObject(base.get(key).getAsJsonObject(), element.getAsJsonObject());
			}
		}
		
		return base;
	}
}
