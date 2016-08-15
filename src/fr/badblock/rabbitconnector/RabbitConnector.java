package fr.badblock.rabbitconnector;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.google.gson.Gson;

import fr.badblock.rabbitconnector.workers.RabbitService;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter public class RabbitConnector  {

	@Getter @Setter private static 	RabbitConnector 								instance		= new RabbitConnector();

	private							Gson											gson			= new Gson();
	private 					   	ConcurrentMap<String, RabbitService>			services		= new ConcurrentHashMap<>();
	private							ConcurrentMap<String, RabbitCredentials>		credentials		= new ConcurrentHashMap<>();
	
	public RabbitCredentials newCredentials(String name, String hostname, int port, String username, String password, String virtualHost) {
		return new RabbitCredentials(name, hostname, port, username, password, virtualHost);
	}
	
	public RabbitService newService(String name, String hostname, int port, String username, String password, String virtualHost) {
		return newService(name, newCredentials(name, hostname, port, username, password, virtualHost));
	}
	
	public RabbitService newService(String name, RabbitCredentials rabbitCredentials) {
		return new RabbitService(name, rabbitCredentials);
	}
	
	public RabbitCredentials getCredentials(String name) {
		return this.getCredentials().get(name);
	}
	
	public Collection<RabbitCredentials> getRabbitCredentials() {
		return this.getCredentials().values();
	}
	
	public RabbitService getService(String name) {
		return this.getServices().get(name);
	}
	
	public Collection<RabbitService> getRabbitServices() {
		return this.getServices().values();
	}
	
	public void removeService(String name) {
		if (this.getService(name) != null) this.getService(name).remove();
	}
	
	public void removeCredentials(String name) {
		if (this.getCredentials(name) != null) this.getCredentials(name).remove();
	}
	
}
