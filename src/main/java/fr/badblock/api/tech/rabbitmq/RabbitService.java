package fr.badblock.api.tech.rabbitmq;

import com.rabbitmq.client.*;
import fr.badblock.api.BadBlockAPI;
import fr.badblock.api.tech.AutoReconnector;
import fr.badblock.api.tech.rabbitmq.listener.RabbitListener;
import fr.badblock.api.tech.rabbitmq.listener.RabbitRequestListener;
import fr.badblock.api.tech.rabbitmq.packet.RabbitPacket;
import fr.badblock.api.tech.rabbitmq.packet.RabbitPacketManager;
import fr.badblock.api.tech.rabbitmq.setting.RabbitSettings;

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
    private BadBlockAPI badBlockAPI;

    private List<RabbitListener> listeners = new ArrayList<>();
    private List<RabbitRequestListener> requests = new ArrayList<>();

    public RabbitService(String name, RabbitSettings settings) {
        super(name, settings);
        //setSettings(settings);
        this.badBlockAPI = BadBlockAPI.getPluginInstance();
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
        badBlockAPI.getLogger().info("[BadBlock-API] RabbitMQ - Packet envoyer a '" + rabbitPacket.getQueue() + "' : " + rabbitPacket.getRabbitPacketMessage().getMessage());
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

                System.out.println("O");
                channel.basicConsume(replyQueueName, true, new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
                        System.out.println("Response: " + properties.getCorrelationId() + " / " + corrId);
                        if (properties.getCorrelationId().equals(corrId)) {
                            response.offer(new String(body, StandardCharsets.UTF_8));
                        }
                    }
                });
                System.out.println("K!");
                rabbitPacket.getCallback().done(response.take(), null);
                System.out.println("!! :P");

                debugPacket(rabbitPacket);
                break;
        }
    }

    public void sendPacket(RabbitPacket rabbitPacket) {
        getPacketManager().sendPacket(rabbitPacket);
    }

    @Override
    public void remove() {
        if (isDead()) {
            badBlockAPI.getLogger().info("[BadBlock-API] RabbitMQ - Le service est deja mort.");
            return;
        }
        long time = System.currentTimeMillis();
        setDead(true); // Set dead
        getTask().cancel(); // Cancel AutoReconnector task
        // Close channel
        try {
            getChannel().close();
        } catch (Exception error) {
            badBlockAPI.getLogger().info("[BadBlock-API] RabbitMQ - Une erreur c'est produite lors de la fermeture de RabbitMQ.");
            badBlockAPI.getLogger().info("[BadBlock-API] RabbitMQ - Nous essayons de fermer la connection");
            error.printStackTrace();
        }
        // Close connection
        try {
            getConnection().close();
        } catch (Exception error) {
            badBlockAPI.getLogger().info("[BadBlock-API] RabbitMQ - Une erreur c'est produite lors de la fermeture de RabbitMQ.");
            error.printStackTrace();
            return;
        }
        RabbitConnector.getInstance().getServices().remove(this.getName());
        badBlockAPI.getLogger().info("[BadBlock-API] RabbitMQ - Base correctement fermer (" + (System.currentTimeMillis() - time) + " ms).");
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
            badBlockAPI.getLogger().info("[BadBlock-API] RabbitMQ - Correctement connecter au service Rabbit (" + (System.currentTimeMillis() - time) + " ms).");
        } catch (Exception error) {
            error.printStackTrace();
            setConnectionFactory(getSettings().toFactory());
            badBlockAPI.getLogger().info("[BadBlock-API] RabbitMQ - Impossible de se connecter a Rabbit (" + error.getMessage() + ")");
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

    @Override
    public RabbitSettings getSettings() {
        return settings;
    }
}
