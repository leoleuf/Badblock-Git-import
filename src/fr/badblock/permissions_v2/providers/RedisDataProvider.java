package fr.badblock.permissions_v2.providers;

import java.util.Map;

import fr.badblock.permissions_v2.PermissionProvider;
import fr.badblock.permissions_v2.entities.PermissibleGroup;
import fr.badblock.permissions_v2.entities.PermissiblePlayer;
import fr.badblock.redis.RedisConnector;
import fr.badblock.redis.RedisService;
import fr.badblock.utils.Callback;

public class RedisDataProvider implements PermissionProvider {
	private String[]	 locations;
	private RedisService service;
	
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
		service.getAsyncObject("badblock_permissions", callback);
	}

	@Override
	public void loadPlayer(String name, Callback<PermissiblePlayer> callback) {
		service.getAsyncObject("badblock_player_" + name.toLowerCase(), callback);
	}

}
