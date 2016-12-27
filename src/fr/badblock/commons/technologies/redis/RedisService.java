package fr.badblock.commons.technologies.redis;

import java.lang.reflect.ParameterizedType;

import fr.badblock.commons.utils.Callback;
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

	public <T> void getSyncObject(String key, Callback<T> object) {
		getSyncString(key, new Callback<String>() {
			@Override
			public void done(String result, Throwable error) {
				object.done(RedisConnector.getInstance().getGson().fromJson(result, getGenericClass(object)), null);
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
	
	public <T> void getAsyncObject(String key, Callback<T> object) {
		new Thread() {
			@Override
			public void run() {
				getSyncObject(key, object);
			}
		}.start();
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
	
	@SuppressWarnings("unchecked")
	private <T> Class<T> getGenericClass(Callback<T> object) {
		return (Class<T>) ((ParameterizedType) object.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
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
