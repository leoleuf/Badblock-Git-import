package fr.badblock.redis;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter public class RedisService {

	private		String						name;
	private		RedisCredentials			credentials;
	private		boolean						isDead;

	public RedisService(String name, RedisCredentials credentials) {
		this.setCredentials(credentials);
		this.setName(name);
		RedisConnector.getInstance().getServices().put(this.getName(), this);
		System.out.println("[RedisConnector] Registered new service (" + name + ")");
	}

	public void setAsync(String key, String value) {
		set(key, value);
	}
	
	public void setAsync(String key, Object value) {
		set(key, value);
	}

	public void set(String key, String value) {
	}
	
	public void set(String key, Object value) {
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
