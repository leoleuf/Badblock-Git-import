package fr.badblock.redis;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.google.gson.Gson;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Main class where all interactions with Redis technology should start by this, 
 * for creating different services, with credentials and by the way getting different 
 * services to apply some useful things, like using the key/value storage
 * @author xMalware
 */
@Data public class RedisConnector  {

	// RedisConnector singleton instance
	@Getter@Setter private static 	RedisConnector 								instance		= new RedisConnector();

	// Private fields
	private							Gson										gson			= new Gson(); // A Gson object, who serve an header of these messages
	private 					   	ConcurrentMap<String, RedisService>			services		= new ConcurrentHashMap<>(); // Services list
	private							ConcurrentMap<String, RedisCredentials>		credentials		= new ConcurrentHashMap<>(); // Credentials list
	
	/**
	 * Create credentials and be back with a RedisCredentials object who is useful for some operations, like using it in different services
	 * @param name 		  > the name of credentials instance
	 * @param hostname    > hostname, we higly recommend DNS
	 * @param port 		  > Redis Cluster port, 65525 by default
	 * @param password	  > the password of that account
	 * @return a RedisCredentials object
	 */
	public RedisCredentials newCredentials(String name, String hostname, int port, String password) {
		return new RedisCredentials(name, hostname, port, password);
	}
	
	/**
	 * Adding a new service by using credentials (with fields)
	 * @param name 		  > the name of the service instance
	 * @param hostname    > hostname, we higly recommend DNS
	 * @param port 		  > Redis Cluster port, 65525 by default
	 * @param password	  > the password of that account
	 * @return a RedisService ready to work
	 */
	public RedisService newService(String name, String hostname, int port, String password) {
		return newService(name, newCredentials(name, hostname, port, password));
	}
	
	/**
	 * Adding a new service by using credentials (with the given RedisCredentials object)
	 * @param name 		  		> the name of the service instance
	 * @param RedisCredentials > the credentials, who will be used by the service
	 * @return a RedisService ready to work
	 */
	public RedisService newService(String name, RedisCredentials RedisCredentials) {
		return new RedisService(name, RedisCredentials);
	}
	
	/**
	 * Getting credentials with a credentials marked name
	 * @param name > the name who will serve to fetch the requested credentials
	 * @return a RedisCredentials object
	 */
	public RedisCredentials getCredentials(String name) {
		return this.getCredentials().get(name);
	}
	
	/**
	 * Getting all registered Redis credentials
	 * @return a Collection of RedisCredentials objects
	 */
	public Collection<RedisCredentials> getRedisCredentials() {
		return this.getCredentials().values();
	}
	
	/**
	 * Getting a service with a service marked name
	 * @param name > the name who will serve to fetch the requested service
	 * @return a RedisService ready to work
	 */
	public RedisService getService(String name) {
		return this.getServices().get(name);
	}
	
	
	/**
	 * Getting all registered Redis service
	 * @return a Collection of RedisService objects
	 */
	public Collection<RedisService> getRedisServices() {
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
