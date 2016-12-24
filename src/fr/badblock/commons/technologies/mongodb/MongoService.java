package fr.badblock.commons.technologies.mongodb;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter public class MongoService {

	private		String						name;
	private		MongoCredentials			credentials;
	private		MongoClient					mongoClient;
	private		boolean						isDead;
	private     DB							db;

	public MongoService(String name, MongoCredentials credentials) {
		this.setCredentials(credentials);
		this.setName(name);
		// Connect
		List<ServerAddress> seeds = new ArrayList<ServerAddress>();
		credentials.getHostnames().forEach(hostname -> {
			try {
				seeds.add(new ServerAddress(hostname));
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		});
		List<MongoCredential> credential = new ArrayList<MongoCredential>();
		credential.add(
				MongoCredential.createMongoCRCredential(
						credentials.getUsername(),
						credentials.getDatabase(),
						credentials.getPassword().toCharArray()
						)
				);
		mongoClient = new MongoClient(seeds, credential);
		db = client().getDB(this.getCredentials().getDatabase());
		MongoConnector.getInstance().getServices().put(this.getName(), this);
		System.out.println("[MongoConnector] Registered new service (" + name + ")");
	}

	public DB db() {
		return this.getDb();
	}

	public MongoClient client() {
		return this.getMongoClient();
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
