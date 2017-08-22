package fr.badblock.commons.technologies.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import fr.badblock.commons.utils.Encodage;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter public class RabbitService {

	private		String						name;
	private		RabbitCredentials			credentials;
	private		Connection					connection;
	private		Channel						channel;
	private		boolean						isDead;

	public RabbitService(String name, RabbitCredentials credentials) {
		this.setCredentials(credentials);
		this.setName(name);
		RabbitConnector.getInstance().getServices().put(this.getName(), this);
		System.out.println("[RabbitConnector] Registered new service (" + name + ")");
		try {
			if (this.getConnection() == null || !this.getConnection().isOpen()) this.setConnection(this.getCredentials().getConnectionFactory().newConnection());
			if (this.getConnection() != null && (this.getChannel() == null || !this.getChannel().isOpen())) this.setChannel(this.getConnection().createChannel());
		}catch(Exception exception) {
			System.out.println("[RabbitConnector] Error during the connection: " + exception.getMessage() + ")");
		}
	}

	public void sendSyncPacket(String queueName, String body, Encodage encodage, RabbitPacketType type, final long ttl, boolean debug) {
		if (this.isDead()) {
			if (debug)
				System.out.println("Trying to send a packet but it was dead!");
			return;
		}
		try {
			RabbitCredentials credentials = this.getCredentials();
			ConnectionFactory connectionFactory = credentials.getConnectionFactory();
			if (this.getConnection() == null || !this.getConnection().isOpen()) this.setConnection(connectionFactory.newConnection());
			if (this.getConnection() != null && (this.getChannel() == null || !this.getChannel().isOpen())) this.setChannel(this.getConnection().createChannel());
			if (this.getConnection() != null && this.getConnection().isOpen() && this.getChannel() != null && this.getChannel().isOpen()) {
				String message = new RabbitMessage(ttl, body).toJson();
				if (type.equals(RabbitPacketType.MESSAGE_BROKER)) {
					this.getChannel().queueDeclare(queueName, false, false, false, null);
					this.getChannel().basicPublish("", queueName, null, message.getBytes(encodage.getName()));
					if (debug) System.out.println("[RabbitConnector] Packet sended to '" + queueName + "' : " + body);
					return;
				}
				if (type.equals(RabbitPacketType.PUBLISHER)) {
					this.getChannel().exchangeDeclare(queueName, "fanout");
					this.getChannel().basicPublish(queueName, "", null, message.getBytes(encodage.getName()));
					if (debug) System.out.println("[RabbitConnector] Packet sended to '" + queueName + "' : " + body);
					return;
				}
			}
		}catch(Exception exception) {
			System.out.println("[RabbitConnector] Error during a packet sending to '" + queueName +"' : " + body);
			exception.printStackTrace();
		}
	}

	public void sendPacket(final String queueName, final String body, final Encodage encodage, final RabbitPacketType type, final long ttl, final boolean debug) {
		new Thread() {
			@Override
			public void run() {
				sendSyncPacket(queueName, body, encodage, type, ttl, debug);
			}
		}.start();
	}

	public void remove() {
		System.out.println("[RabbitConnector] Unregistered service! (" + this.getName() + ")");
		try {
			if (this.getConnection() != null && this.getConnection().isOpen()) this.getConnection().close();
			if (this.getChannel() != null && this.getChannel().isOpen()) this.getChannel().close();
		}catch(Exception exception) {
			System.out.println("[RabbitConnector] Error during the disconnection: " + exception.getMessage() + ")");
		}
		RabbitConnector.getInstance().getServices().remove(this.getName());
		this.setDead(true);
	}

}
