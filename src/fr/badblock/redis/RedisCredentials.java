package fr.badblock.redis;

import lombok.Getter;
import lombok.Setter;

/**
 * A RedisCredentials object who will allow 
 * @author root
 *
 */
@Getter @Setter public class RedisCredentials {
	
	private String					name;
	private	String					hostname;
	private	int						port;
	private	String					password;
	
	public RedisCredentials(String name, String hostname, int port, String password) {
		this.setName(name);
		this.setHostname(hostname);
		this.setPort(port);
		this.setPassword(password);
		RedisConnector.getInstance().getCredentials().put(this.getName(), this);
		System.out.println("[RedisConnector] Registered new credentials! (" + name + ")");
	}
	
	public void remove() {
		System.out.println("[RedisConnector] Unregistered credentials (" + this.getName() + ")");
		RedisConnector.getInstance().getCredentials().remove(this.getName());
		for (RedisService redisService : RedisConnector.getInstance().getServices().values())
			if (redisService.getCredentials().equals(this)) redisService.remove();
	}

}
