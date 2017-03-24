package fr.badblock.rabbitconnector;

import java.util.ArrayList;
import java.util.List;

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
	private	String[]				hostnames;
	private	int						port;
	private	String					username;
	private	String					password;
	private	String					virtualHost;
	private List<ConnectionFactory>	connectionFactories;

	public RabbitCredentials(String name, String[] hostnames, int port, String username, String password, String virtualHost) {
		this.setName(name);
		this.setHostnames(hostnames);
		this.setPort(port);
		this.setUsername(username);
		this.setPassword(password);
		this.setVirtualHost(virtualHost);
		this.flushChanges();
		RabbitConnector.getInstance().getCredentials().put(this.getName(), this);
		System.out.println("[RabbitConnector] Registered new credentials! (" + name + ")");
	}

	public void flushChanges() {
		this.setConnectionFactories(buildFactories());
	}

	private List<ConnectionFactory> buildFactories() {
		ArrayList<ConnectionFactory> factories = new ArrayList<>();
		for (String hostname : hostnames) {
			ConnectionFactory connectionFactory = new ConnectionFactory();
			connectionFactory.setUsername(getUsername());
			connectionFactory.setPassword(getPassword());
			connectionFactory.setVirtualHost(getVirtualHost());
			connectionFactory.setHost(hostname);
			connectionFactory.setPort(getPort());
			connectionFactory.setAutomaticRecoveryEnabled(true);
			connectionFactory.setConnectionTimeout(60000);
			connectionFactory.setRequestedHeartbeat(60);
			factories.add(connectionFactory);
		}
		return factories;
	}

	public void remove() {
		System.out.println("[RabbitConnector] Unregistered credentials (" + this.getName() + ")");
		RabbitConnector.getInstance().getCredentials().remove(this.getName());
		for (RabbitService rabbitService : RabbitConnector.getInstance().getServices().values())
			if (rabbitService.getCredentials().equals(this)) rabbitService.remove();
	}

}
