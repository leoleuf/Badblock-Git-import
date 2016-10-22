package fr.badblock.commons.permissions.providers;

import java.util.Map;

import fr.badblock.commons.data.PlayerData;
import fr.badblock.commons.permissions.PermissionProvider;
import fr.badblock.commons.permissions.entities.PermissibleGroup;
import fr.badblock.commons.permissions.entities.PermissiblePlayer;
import fr.badblock.commons.redis.RedisConnector;
import fr.badblock.commons.redis.RedisService;
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
	
	public void loadFullPlayer(String name, Callback<PlayerData> callback) {
		service.getAsyncObject("badblock_player_" + name.toLowerCase(), callback);
	}
}
