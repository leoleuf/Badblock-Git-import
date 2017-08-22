package fr.badblock.rabbitconnector;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import fr.badblock.commons.technologies.rabbitmq.RabbitMessage;
import fr.badblock.commons.technologies.rabbitmq.RabbitPacketType;
import fr.badblock.commons.utils.Encodage;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter public class RabbitService {

	private		String						name;
	private		RabbitCredentials			credentials;
	private		Connection					connection;
	private		Channel						channel;
	private		boolean						isDead;
	private		List<Thread>				threads;
	private		Queue<RabbitPacket>			queue;
	private		Thread						threzd		= Thread.currentThread();

	public RabbitService(String name, RabbitCredentials credentials) {
		this.setCredentials(credentials);
		this.setName(name);
		this.setQueue(new ConcurrentLinkedDeque<>());
		this.setThreads(new ArrayList<>());
		for (int i = 0; i < 16; i++) {
			Thread thread = new Thread("BadBlockCommon/RabbitService/" + name + "/Thread-" + i) {
				@Override
				public void run() {
					while (true) {
						while (!queue.isEmpty()) {
							RabbitPacket rabbitPacket = queue.poll();
							if (rabbitPacket == null || rabbitPacket.getRabbitMessage() == null) continue;
							if (rabbitPacket.getRabbitMessage().getMessage() == null) continue;
							if (rabbitPacket.getRabbitMessage().getMessage().isEmpty()) continue;
							done(rabbitPacket);
						}
						synchronized (this) {
							try {
								this.wait();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}
			};
			thread.start();
			this.getThreads().add(thread);
		}
		RabbitConnector.getInstance().getServices().put(this.getName(), this);
		System.out.println("[RabbitConnector] Registered new service (" + name + ")");
		try {
			if (this.getConnection() == null || !this.getConnection().isOpen()) {
				ConnectionFactory connectionFactory = this.getCredentials().getConnectionFactories().get(new SecureRandom().nextInt(this.getCredentials().getConnectionFactories().size()));
				this.setConnection(connectionFactory.newConnection());
			}
			if (this.getConnection() != null && (this.getChannel() == null || !this.getChannel().isOpen())) {
				ConnectionFactory connectionFactory = this.getCredentials().getConnectionFactories().get(new SecureRandom().nextInt(this.getCredentials().getConnectionFactories().size()));
				this.setConnection(connectionFactory.newConnection());
				this.setChannel(this.getConnection().createChannel());
			}
		}catch(Exception exception) {
			System.out.println("[RabbitConnector] Error during the connection: " + exception.getMessage() + ")");
		}
	}

	public void sendSyncPacket(final String queueName, final String body, final Encodage encodage, final RabbitPacketType type, final long ttl, final boolean debug) {
		RabbitPacket rabbitPacket = new RabbitPacket(queueName, encodage, type, debug, new RabbitMessage(ttl, body));
		done(rabbitPacket);
	}

	public void sendAsyncPacket(final String queueName, final String body, final Encodage encodage, final RabbitPacketType type, final long ttl, final boolean debug) {
		RabbitPacket rabbitPacket = new RabbitPacket(queueName, encodage, type, debug, new RabbitMessage(ttl, body));
		queue.add(rabbitPacket);
		threads.forEach(thread -> {
			synchronized (thread) {
				thread.notify();
			}
		});
	}

	private void done(RabbitPacket rabbitPacket) {
		if (rabbitPacket == null || rabbitPacket.getRabbitMessage() == null) return;
		if (rabbitPacket.getRabbitMessage().getMessage() == null) return;
		if (rabbitPacket.getRabbitMessage().getMessage().isEmpty()) return;
		try {
			if (this.getConnection() == null || !this.getConnection().isOpen()) {
				ConnectionFactory connectionFactory = this.getCredentials().getConnectionFactories().get(new SecureRandom().nextInt(this.getCredentials().getConnectionFactories().size()));
				this.setConnection(connectionFactory.newConnection());
			}
			if (this.getConnection() != null && (this.getChannel() == null || !this.getChannel().isOpen())) {
				ConnectionFactory connectionFactory = this.getCredentials().getConnectionFactories().get(new SecureRandom().nextInt(this.getCredentials().getConnectionFactories().size()));
				this.setConnection(connectionFactory.newConnection());
				this.setChannel(this.getConnection().createChannel());
			}
			if (this.getConnection() != null && this.getConnection().isOpen() && this.getChannel() != null && this.getChannel().isOpen()) {
				String message = rabbitPacket.getRabbitMessage().toJson();
				if (rabbitPacket.getType().equals(RabbitPacketType.MESSAGE_BROKER)) {
					this.getChannel().queueDeclare(rabbitPacket.getQueueName(), false, false, false, null);
					this.getChannel().basicPublish("", rabbitPacket.getQueueName(), null, message.getBytes(rabbitPacket.getEncodage().getName()));
					if (rabbitPacket.isDebug()) System.out.println("[RabbitConnector] Packet sended to '" + rabbitPacket.getQueueName() + "' : " + rabbitPacket.getRabbitMessage().getMessage());
					return;
				}
				if (rabbitPacket.getType().equals(RabbitPacketType.PUBLISHER)) {
					this.getChannel().exchangeDeclare(rabbitPacket.getQueueName(), "fanout");
					this.getChannel().basicPublish(rabbitPacket.getQueueName(), "", null, message.getBytes(rabbitPacket.getEncodage().getName()));
					if (rabbitPacket.isDebug()) System.out.println("[RabbitConnector] Packet sended to '" + rabbitPacket.getQueueName() + "' : " + rabbitPacket.getRabbitMessage().getMessage());
					return;
				}
			}
		}catch(Exception error) {
			error.printStackTrace();
		}
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
