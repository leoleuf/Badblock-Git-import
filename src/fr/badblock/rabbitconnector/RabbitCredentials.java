package fr.badblock.rabbitconnector;

import com.google.gson.Gson;
import com.rabbitmq.client.ConnectionFactory;

import fr.badblock.rabbitconnector.workers.RabbitService;
import lombok.Getter;
import lombok.Setter;

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
		this.setConnectionFactory(buildFactory());
		RabbitConnector.getInstance().getCredentials().put(this.getName(), this);
		System.out.println("[RabbitConnector] Registered new credentials! (" + name + ")");
	}
	
	public ConnectionFactory buildFactory() {
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setUsername(getUsername());
		connectionFactory.setPassword(getPassword());
		connectionFactory.setVirtualHost(getVirtualHost());
		connectionFactory.setHost(getHostname());
		connectionFactory.setPort(getPort());
		connectionFactory.setAutomaticRecoveryEnabled(true);
		connectionFactory.setConnectionTimeout(30000);
		connectionFactory.setRequestedHeartbeat(4);
		return connectionFactory;
	}
	
	public void remove() {
		System.out.println("[RabbitConnector] Unregistered credentials (" + name + ")");
		RabbitConnector.getInstance().getCredentials().remove(this.getName());
		for (RabbitService rabbitService : RabbitConnector.getInstance().getServices().values())
			if (rabbitService.getCredentials().equals(this)) rabbitService.remove();
	}

}
