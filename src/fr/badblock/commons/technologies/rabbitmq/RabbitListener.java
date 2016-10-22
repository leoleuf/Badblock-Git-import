package fr.badblock.commons.technologies.rabbitmq;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import lombok.Data;

@Data public abstract class RabbitListener {

	private RabbitService			rabbitService;
	private String					queueName;
	private boolean					debug;
	private RabbitListenerType		type;
	Consumer						consumer;

	public RabbitListener (final RabbitService rabbitService, final String queueName, final boolean debug, final RabbitListenerType type) {
		this.setRabbitService(rabbitService);
		this.setQueueName(queueName);
		this.setDebug(debug);
		this.setType(type);
		new Thread() {
			@Override
			public void run() {
				while (true) {
					if (rabbitService.isDead()) return;
					while (rabbitService.getConnection() == null || !rabbitService.getConnection().isOpen() || rabbitService.getChannel() == null || !rabbitService.getChannel().isOpen()) {
						if (rabbitService.isDead()) return;
						try {
							if (rabbitService.getConnection() == null || !rabbitService.getConnection().isOpen()) rabbitService.setConnection(rabbitService.getCredentials().getConnectionFactory().newConnection());
							if (rabbitService.getConnection() != null && (rabbitService.getChannel() == null || !rabbitService.getChannel().isOpen())) rabbitService.setChannel(rabbitService.getConnection().createChannel());
							setConsumer(null);
						}catch(Exception error) {
							System.out.println("[RabbitConnector] Error during trying to connect (" + error.getMessage() + ").");
						}
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					try {
						if (getConsumer() == null) {
							Channel channel = rabbitService.getChannel();
							String finalQueueName = queueName;
							if (type.equals(RabbitListenerType.MESSAGE_BROKER))
								channel.queueDeclare(queueName, false, false, false, null);
							else if (type.equals(RabbitListenerType.SUBSCRIBER)) {
								channel.exchangeDeclare(queueName, "fanout");
								String tempQueueName = channel.queueDeclare().getQueue();
								finalQueueName = tempQueueName;
								channel.queueBind(tempQueueName, queueName, "");
							}
							setConsumer(new DefaultConsumer(channel) {
								@Override
								public void handleDelivery(String consumerTag, Envelope envelope,
										AMQP.BasicProperties properties, byte[] body) throws IOException {
									String message = new String(body, "UTF-8");
									if (rabbitService.isDead()) return;
									try {
										RabbitMessage rabbitMessage = RabbitMessage.fromJson(message);
										if (!rabbitMessage.isExpired()) {
											if (debug) 
												System.out.println("[RabbitConnector] Packet received from " + queueName + ": " + rabbitMessage.getMessage());
											onPacketReceiving(rabbitMessage.getMessage());
										}else if (debug) System.out.println("[RabbitConnector] Error during a receiving of a packet from " + queueName + ": EXPIRED!");
									}catch(Exception error) {
										System.out.println("[RabbitConnector] Error during the handle delivery.");
										error.printStackTrace();
									}
								}
							});
							channel.basicConsume(finalQueueName, true, getConsumer());
							System.out.println("[RabbitConnector] Loaded listener from " + queueName + " (" + getClass().getSimpleName() + ").");
						}
					}catch(Exception error) {
						System.out.println("[RabbitConnector] Error during a listener bind.");
						error.printStackTrace();
					}
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	public abstract void onPacketReceiving(String body);

}
