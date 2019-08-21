package fr.badblock.api.tech.rabbitmq.listener;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import fr.badblock.api.BadBlockAPI;
import fr.badblock.api.tech.rabbitmq.packet.RabbitPacketMessage;

import java.nio.charset.StandardCharsets;

public class RabbitListenerConsumer extends DefaultConsumer {

    private RabbitListener rabbitListener;
    private BadBlockAPI badBlockAPI;

    RabbitListenerConsumer(Channel channel, RabbitListener rabbitListener) {
        super(channel);
        this.setRabbitListener(rabbitListener);
        this.badBlockAPI = BadBlockAPI.getPluginInstance();
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
        String message = new String(body, StandardCharsets.UTF_8);

        if (getRabbitListener().getRabbitService().isDead()) {
            return;
        }

        try {
            RabbitPacketMessage rabbitMessage = RabbitPacketMessage.fromJson(message);

            if (getRabbitListener().isDebug()) {
                badBlockAPI.getLogger().info("[BadBlock-API] RabbitMQ - Packet recu de " + getRabbitListener().getName() + ": " + rabbitMessage.getMessage());
            }

            if (rabbitMessage.isAlive()) {
                getRabbitListener().onPacketReceiving(rabbitMessage.getMessage());
            } else {
                badBlockAPI.getLogger().info("[BadBlock-API] RabbitMQ - Erreur lors de la reception d'un packet de " + getRabbitListener().getName() + ": EXPIRER!");
                badBlockAPI.getLogger().info("[BadBlock-API] RabbitMQ - " + rabbitMessage.getMessage());
            }

        } catch (Exception error) {
            badBlockAPI.getLogger().info("[BadBlock-API] RabbitMQ - Erreur lors de la reception du handle.");
            error.printStackTrace();
        }
    }

    private RabbitListener getRabbitListener() {
        return rabbitListener;
    }

    private void setRabbitListener(RabbitListener rabbitListener) {
        this.rabbitListener = rabbitListener;
    }
}
