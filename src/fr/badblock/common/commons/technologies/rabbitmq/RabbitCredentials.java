package fr.badblock.common.commons.technologies.rabbitmq;

import com.rabbitmq.client.ConnectionFactory;

import lombok.Getter;
import lombok.Setter;

/**
 * A RabbitCredentials object who will allow 
 * @author root
 *
 */
@Getter @Setter public class RabbitCredentials {
	
	private String					name;
	private	String					hostname;
	private	int						port;
	private	String					username;
	private	String					password;
	private	String					virtualHost;
	private ConnectionFactory		connectionFactory;
	
	public RabbitCredentials(String name, String hostname, int port, String username, String password, String virtualHost) {
		this.setName(name);
		this.setHostname(hostname);
		this.setPort(port);
		this.setUsername(username);
		this.setPassword(password);
		this.setVirtualHost(virtualHost);
		this.flushChanges();
		RabbitConnector.getInstance().getCredentials().put(this.getName(), this);
		System.out.println("[RabbitConnector] Registered new credentials! (" + name + ")");
	}
	
	public void flushChanges() {
		this.setConnectionFactory(buildFactory());
	}
	
	private ConnectionFactory buildFactory() {
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setUsername(getUsername());
		connectionFactory.setPassword(getPassword());
		connectionFactory.setVirtualHost(getVirtualHost());
		connectionFactory.setHost(getHostname());
		connectionFactory.setPort(getPort());
		connectionFactory.setAutomaticRecoveryEnabled(true);
		connectionFactory.setConnectionTimeout(60000);
		connectionFactory.setRequestedHeartbeat(60);
		return connectionFactory;
	}
	
	public void remove() {
		System.out.println("[RabbitConnector] Unregistered credentials (" + this.getName() + ")");
		RabbitConnector.getInstance().getCredentials().remove(this.getName());
		for (RabbitService rabbitService : RabbitConnector.getInstance().getServices().values())
			if (rabbitService.getCredentials().equals(this)) rabbitService.remove();
	}

}
