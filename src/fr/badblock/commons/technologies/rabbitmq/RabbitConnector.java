package fr.badblock.commons.technologies.rabbitmq;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.google.gson.Gson;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Main class where all interactions with RabbitMQ technology should start by this, 
 * for creating different services, with credentials and by the way getting different 
 * services to apply some useful things, as listen a queue, an exchange or build and send a queued message
 * @author xMalware
 */
@Data public class RabbitConnector  {

	// RabbitConnector singleton instance
	@Getter@Setter private static 	RabbitConnector 								instance		= new RabbitConnector();

	// Private fields
	private							Gson											gson			= new Gson(); // A Gson object, who serve an header of these messages
	private 					   	ConcurrentMap<String, RabbitService>			services		= new ConcurrentHashMap<>(); // Services list
	private							ConcurrentMap<String, RabbitCredentials>		credentials		= new ConcurrentHashMap<>(); // Credentials list
	
	/**
	 * Create credentials and be back with a RabbitCredentials object who is useful for some operations, like using it in different services
	 * @param name 		  > the name of credentials instance
	 * @param hostname    > hostname, we higly recommend DNS
	 * @param port 		  > RabbitMQ Cluster port, 5762 by default
	 * @param username    > username of that account
	 * @param password	  > the password of that account
	 * @param virtualHost > virtual host where the account will be connected to
	 * @return a RabbitCredentials object
	 */
	public RabbitCredentials newCredentials(String name, String hostname, int port, String username, String password, String virtualHost) {
		return new RabbitCredentials(name, hostname, port, username, password, virtualHost);
	}
	
	/**
	 * Adding a new service by using credentials (with fields)
	 * @param name 		  > the name of the service instance
	 * @param hostname    > hostname, we higly recommend DNS
	 * @param port 		  > RabbitMQ Cluster port, 5762 by default
	 * @param username    > username of that account
	 * @param password	  > the password of that account
	 * @param virtualHost > virtual host where the account will be connected to
	 * @return a RabbitService ready to work
	 */
	public RabbitService newService(String name, String hostname, int port, String username, String password, String virtualHost) {
		return newService(name, newCredentials(name, hostname, port, username, password, virtualHost));
	}
	
	/**
	 * Adding a new service by using credentials (with the given RabbitCredentials object)
	 * @param name 		  		> the name of the service instance
	 * @param rabbitCredentials > the credentials, who will be used by the service
	 * @return a RabbitService ready to work
	 */
	public RabbitService newService(String name, RabbitCredentials rabbitCredentials) {
		return new RabbitService(name, rabbitCredentials);
	}
	
	/**
	 * Getting credentials with a credentials marked name
	 * @param name > the name who will serve to fetch the requested credentials
	 * @return a RabbitCredentials object
	 */
	public RabbitCredentials getCredentials(String name) {
		return this.getCredentials().get(name);
	}
	
	/**
	 * Getting all registered rabbit credentials
	 * @return a Collection of RabbitCredentials objects
	 */
	public Collection<RabbitCredentials> getRabbitCredentials() {
		return this.getCredentials().values();
	}
	
	/**
	 * Getting a service with a service marked name
	 * @param name > the name who will serve to fetch the requested service
	 * @return a RabbitService ready to work
	 */
	public RabbitService getService(String name) {
		return this.getServices().get(name);
	}
	
	
	/**
	 * Getting all registered rabbit service
	 * @return a Collection of RabbitService objects
	 */
	public Collection<RabbitService> getRabbitServices() {
		return this.getServices().values();
	}
	
	/**
	 * Removing a service
	 * @param name > service name
	 */
	public void removeService(String name) {
		if (this.getService(name) != null) this.getService(name).remove();
	}
	
	/**
	 * Removing credentials
	 * @param name > credentials marked name
	 */
	public void removeCredentials(String name) {
		if (this.getCredentials(name) != null) this.getCredentials(name).remove();
	}
	
}
