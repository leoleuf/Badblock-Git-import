package fr.badblock.commons.mongodb;

import lombok.Getter;
import lombok.Setter;

/**
 * A MongoCredentials object who will allow 
 * @author root
 *
 */
@Getter @Setter public class MongoCredentials {
	
	private String					name;
	private	String					hostname;
	private	int						port;
	private	String					password;
	
	public MongoCredentials(String name, String hostname, int port, String password) {
		this.setName(name);
		this.setHostname(hostname);
		this.setPort(port);
		this.setPassword(password);
		MongoConnector.getInstance().getCredentials().put(this.getName(), this);
		System.out.println("[MongoConnector] Registered new credentials! (" + name + ")");
	}
	
	public void remove() {
		System.out.println("[MongoConnector] Unregistered credentials (" + this.getName() + ")");
		MongoConnector.getInstance().getCredentials().remove(this.getName());
		for (MongoService MongoService : MongoConnector.getInstance().getServices().values())
			if (MongoService.getCredentials().equals(this)) MongoService.remove();
	}

}
