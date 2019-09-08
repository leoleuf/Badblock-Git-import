package fr.badblock.tech.rabbitmq.listener;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import fr.badblock.tech.logger.LogType;
import fr.badblock.tech.logger.Logger;
import fr.badblock.tech.rabbitmq.packet.RabbitPacketMessage;

import java.nio.charset.StandardCharsets;

public class RabbitListenerConsumer extends DefaultConsumer {

    private RabbitListener rabbitListener;
    private Logger logger;

    RabbitListenerConsumer(Channel channel, RabbitListener rabbitListener) {
        super(channel);
        this.setRabbitListener(rabbitListener);
        this.logger = new Logger();
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
                logger.log(LogType.INFO, "[BadBlock-API] RabbitMQ - Packet recu de " + getRabbitListener().getName() + ": " + rabbitMessage.getMessage());
            }

            if (rabbitMessage.isAlive()) {
                getRabbitListener().onPacketReceiving(rabbitMessage.getMessage());
            } else {
                logger.log(LogType.ERROR, "[BadBlock-API] RabbitMQ - Erreur lors de la reception d'un packet de " + getRabbitListener().getName() + ": EXPIRER!");
                logger.log(LogType.ERROR, "[BadBlock-API] RabbitMQ - " + rabbitMessage.getMessage());
            }

        } catch (Exception error) {
            logger.log(LogType.ERROR, "[BadBlock-API] RabbitMQ - Erreur lors de la reception du handle.");
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
