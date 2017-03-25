package fr.badblock.common.commons.technologies.redis;

import java.lang.reflect.Type;

import com.google.gson.Gson;

import fr.badblock.common.commons.utils.Callback;
import lombok.Getter;
import lombok.Setter;
import redis.clients.jedis.Jedis;

@Getter @Setter public class RedisService {

	private		String						name;
	private		RedisCredentials			credentials;
	private		Jedis						jedis;
	private		boolean						isDead;

	public RedisService(String name, RedisCredentials credentials) {
		this.setCredentials(credentials);
		this.setName(name);
		RedisConnector.getInstance().getServices().put(this.getName(), this);
		this.setJedis(new Jedis(credentials.getHostname(), credentials.getPort()));
		this.getJedis().auth(credentials.getPassword());
		this.getJedis().select(credentials.getDatabase());
		System.out.println("[RedisConnector] Registered new service (" + name + ")");
	}

	public void setAsync(String key, String value) {
		setAsync(key, (Object) value);
	}

	public void setAsync(String key, Object value) {
		new Thread() {
			@Override
			public void run() {
				set(key, value);
			}
		}.start();
	}
	
	public void set(String key, String value) {
		if (!check()) return;
		this.getJedis().set(key, value);
	}

	public void set(String key, Object value) {
		set(key, RedisConnector.getInstance().getGson().toJson(value));
	}

	public <T> void getSyncObject(String key, Class<T> clazz, Callback<T> object, boolean onlyExpose) {
		getSyncString(key, new Callback<String>() {
			@Override
			public void done(String result, Throwable error) {
				Gson gson = onlyExpose ? RedisConnector.getInstance().getExposeGson() : RedisConnector.getInstance().getGson();
				object.done(gson.fromJson(result, clazz), null);
			}
		});
	}

	public <T> void getSyncObject(String key, Type type, Callback<T> object, boolean onlyExpose) {
		getSyncString(key, new Callback<String>() {
			@Override
			public void done(String result, Throwable error) {
				Gson gson = onlyExpose ? RedisConnector.getInstance().getExposeGson() : RedisConnector.getInstance().getGson();
				object.done(gson.fromJson(result, type), null);
			}
		});
	}

	public void getSyncString(String key, Callback<String> object) {
		if (!check()) return;
		object.done(this.getJedis().get(key), null);
	}
	
	public void getAsyncString(String key, Callback<String> object) {
		new Thread() {
			@Override
			public void run() {
				getSyncString(key, object);
			}
		}.start();
	}
	
	public <T> void getAsyncObject(String key, Class<T> clazz, Callback<T> object, boolean onlyExpose) {
		new Thread() {
			@Override
			public void run() {
				getSyncObject(key, clazz, object, onlyExpose);
			}
		}.start();
	}
	
	public <T> void getAsyncObject(String key, Type type, Callback<T> object, boolean onlyExpose) {
		new Thread() {
			@Override
			public void run() {
				getSyncObject(key, type, object, onlyExpose);
			}
		}.start();
	}

	public void delete(String... keys) {
		this.getJedis().del(keys);
	}
	
	private boolean check() {
		if (!this.getJedis().isConnected()) {
			this.setJedis(new Jedis(credentials.getHostname(), credentials.getPort()));
			this.getJedis().auth(credentials.getPassword());
			if (!this.getJedis().isConnected()) {
				System.out.println("[RedisConnector] Unable to connect to Redis (" + name + ")");
				return false;
			}
		}
		return true;
	}

	public void remove() {
		System.out.println("[RedisConnector] Unregistered service! (" + this.getName() + ")");
		try {
		}catch(Exception exception) {
			System.out.println("[RedisConnector] Error during the disconnection: " + exception.getMessage() + ")");
		}
		RedisConnector.getInstance().getServices().remove(this.getName());
		this.setDead(true);
	}

}
