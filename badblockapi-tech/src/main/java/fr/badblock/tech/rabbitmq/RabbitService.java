package fr.badblock.tech.rabbitmq;

import com.rabbitmq.client.*;
import fr.badblock.tech.AutoReconnector;
import fr.badblock.tech.logger.LogType;
import fr.badblock.tech.logger.Logger;
import fr.badblock.tech.rabbitmq.listener.RabbitListener;
import fr.badblock.tech.rabbitmq.listener.RabbitRequestListener;
import fr.badblock.tech.rabbitmq.packet.RabbitPacket;
import fr.badblock.tech.rabbitmq.packet.RabbitPacketManager;
import fr.badblock.tech.rabbitmq.setting.RabbitSettings;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class RabbitService extends AutoReconnector {

    private ConnectionFactory connectionFactory;
    private Connection connection;
    private Channel channel;
    private RabbitSettings settings;
    private boolean dead;
    private Logger logger;

    private List<RabbitListener> listeners = new ArrayList<>();
    private List<RabbitRequestListener> requests = new ArrayList<>();

    public RabbitService(String name, RabbitSettings settings) {
        super(name, settings);
        setSettings(settings);
        this.logger = new Logger();
        reconnect();
    }

    public RabbitService addListener(RabbitListener listener) {
        listener.load();
        listeners.add(listener);
        return this;
    }

    public RabbitService addRequestListener(RabbitRequestListener listener) {
        listener.load();
        requests.add(listener);
        return this;
    }

    private void debugPacket(RabbitPacket rabbitPacket) {
        if (!rabbitPacket.isDebug()) {
            return;
        }
        logger.log(LogType.INFO, "[BadBlock-API] RabbitMQ - Packet envoyer a '" + rabbitPacket.getQueue() + "' : " + rabbitPacket.getRabbitPacketMessage().getMessage());
    }

    public void sendSyncPacket(RabbitPacket rabbitPacket) throws Exception {
        Channel channel = getChannel();
        if (rabbitPacket == null) {
            return;
        }
        if (rabbitPacket.getRabbitPacketMessage() == null) {
            return;
        }
        String message = rabbitPacket.getRabbitPacketMessage().toJson();
        switch (rabbitPacket.getType()) {
            case MESSAGE_BROKER:
                channel.queueDeclare(rabbitPacket.getQueue(), false, false, false, null);
                channel.basicPublish("", rabbitPacket.getQueue(), null, message.getBytes(rabbitPacket.getEncoder().getName()));
                debugPacket(rabbitPacket);
                break;
            case PUBLISHER:
                channel.exchangeDeclare(rabbitPacket.getQueue(), "fanout");
                channel.basicPublish(rabbitPacket.getQueue(), "", null, message.getBytes(rabbitPacket.getEncoder().getName()));
                debugPacket(rabbitPacket);
                break;
            case REMOTE_PROCEDURE_CALL:
                if (rabbitPacket.getCallback() == null) {
                    break;
                }
                String replyQueueName = channel.queueDeclare().getQueue();
                final String corrId = UUID.randomUUID().toString();
                AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                        .correlationId(corrId)
                        .replyTo(replyQueueName)
                        .build();
                channel.basicPublish("", rabbitPacket.getQueue(), properties, message.getBytes(rabbitPacket.getEncoder().getName()));

                final BlockingQueue<String> response = new ArrayBlockingQueue<>(1);

                logger.log(LogType.INFO, "O");
                channel.basicConsume(replyQueueName, true, new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
                        logger.log(LogType.INFO, "Response: " + properties.getCorrelationId() + " / " + corrId);
                        if (properties.getCorrelationId().equals(corrId)) {
                            response.offer(new String(body, StandardCharsets.UTF_8));
                        }
                    }
                });
                logger.log(LogType.INFO, "K!");
                rabbitPacket.getCallback().done(response.take(), null);
                logger.log(LogType.INFO, "!! :P");

                debugPacket(rabbitPacket);
                break;
        }
    }

    public void sendAsyncPacket(RabbitPacket rabbitPacket) {
        getPacketManager().sendPacket(rabbitPacket);
    }

    @Override
    public void remove() {
        if (isDead()) {
            logger.log(LogType.WARNING, "[BadBlock-API] RabbitMQ - Le service est deja mort.");
            return;
        }
        long time = System.currentTimeMillis();
        setDead(true); // Set dead
        getTask().cancel(); // Cancel AutoReconnector task
        // Close channel
        try {
            getChannel().close();
        } catch (Exception error) {
            logger.log(LogType.ERROR, "[BadBlock-API] RabbitMQ - Une erreur c'est produite lors de la fermeture de RabbitMQ.");
            logger.log(LogType.ERROR, "[BadBlock-API] RabbitMQ - Nous essayons de fermer la connection");
            error.printStackTrace();
        }
        // Close connection
        try {
            getConnection().close();
        } catch (Exception error) {
            logger.log(LogType.ERROR, "[BadBlock-API] RabbitMQ - Une erreur c'est produite lors de la fermeture de RabbitMQ.");
            error.printStackTrace();
            return;
        }
        RabbitConnector.getInstance().getServices().remove(this.getName());
        logger.log(LogType.SUCCESS, "[BadBlock-API] RabbitMQ - Base correctement fermer (" + (System.currentTimeMillis() - time) + " ms).");
    }

    public boolean isAlive() {
        return !isDead();
    }

    @Override
    public boolean isConnected() {
        return getConnection() != null && getConnection().isOpen() &&
                getChannel() != null && getChannel().isOpen();
    }

    @Override
    public void reconnect() {
        if (isDead()) {
            return;
        }
        if (isConnected()) {
            return;
        }
        try {
            long time = System.currentTimeMillis();
            setConnectionFactory(getSettings().toFactory());
            // Create connection
            if (getConnection() == null || !getConnection().isOpen())
                setConnection(getConnectionFactory().newConnection());
            // Create channel
            if (getChannel() == null || !getChannel().isOpen())
                setChannel(getConnection().createChannel());
            // Reload listeners
            listeners.forEach(RabbitListener::load);
            // Reload request listeners
            requests.forEach(RabbitRequestListener::load);
            logger.log(LogType.INFO, "[BadBlock-API] RabbitMQ - Correctement connecter au service Rabbit (" + (System.currentTimeMillis() - time) + " ms).");
        } catch (Exception error) {
            error.printStackTrace();
            setConnectionFactory(getSettings().toFactory());
            logger.log(LogType.ERROR, "[BadBlock-API] RabbitMQ - Impossible de se connecter a Rabbit (" + error.getMessage() + ")");
        }
    }

    private RabbitPacketManager getPacketManager() {
        return RabbitPacketManager.getInstance(this);
    }

    public Channel getChannel() {
        return channel;
    }

    private void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Connection getConnection() {
        return connection;
    }

    private ConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    private void setConnectionFactory(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    private void setConnection(Connection connection) {
        this.connection = connection;
    }

    public RabbitSettings getSettings() {
        return settings;
    }

    public void setSettings(RabbitSettings settings) {
        this.settings = settings;
    }
}
