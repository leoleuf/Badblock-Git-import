package fr.badblock.commons.mongodb;

import com.mongodb.MongoClient;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter public class MongoService {

	private		String						name;
	private		MongoCredentials			credentials;
	private		MongoClient					mongoClient;
	private		boolean						isDead;

	public MongoService(String name, MongoCredentials credentials) {
		this.setCredentials(credentials);
		this.setName(name);
		MongoConnector.getInstance().getServices().put(this.getName(), this);
		System.out.println("[MongoConnector] Registered new service (" + name + ")");
	}

	

	public void remove() {
		System.out.println("[MongoConnector] Unregistered service! (" + this.getName() + ")");
		try {
		}catch(Exception exception) {
			System.out.println("[MongoConnector] Error during the disconnection: " + exception.getMessage() + ")");
		}
		MongoConnector.getInstance().getServices().remove(this.getName());
		this.setDead(true);
	}

}
