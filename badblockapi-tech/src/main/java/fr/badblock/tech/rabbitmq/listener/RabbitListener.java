package fr.badblock.tech.rabbitmq.listener;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import fr.badblock.tech.logger.LogType;
import fr.badblock.tech.logger.Logger;
import fr.badblock.tech.rabbitmq.RabbitService;

public abstract class RabbitListener {

    private final RabbitService rabbitService;
    private final String name;
    private final RabbitListenerType type;
    private final boolean debug;
    private Consumer consumer;
    private Logger logger;

    protected RabbitListener(RabbitService rabbitService, String name, RabbitListenerType type, boolean debug) {
        this.rabbitService = rabbitService;
        this.name = name;
        this.type = type;
        this.debug = debug;
        this.logger = new Logger();
    }

    public void load() {
        try {
            if (!getRabbitService().isAlive()) {
                return;
            }
            Channel channel = getRabbitService().getChannel();
            String finalQueueName = getName();
            switch (getType()) {
                case MESSAGE_BROKER:
                    channel.queueDeclare(getName(), false, false, false, null);
                    break;
                case SUBSCRIBER:
                    channel.exchangeDeclare(getName(), "fanout");
                    finalQueueName = channel.queueDeclare().getQueue();
                    channel.queueBind(finalQueueName, getName(), "");
                    break;
                default:
                    logger.log(LogType.WARNING, "[BadBlock-API] RabbitMQ - Type du listener inconnue.");
            }
            setConsumer(new RabbitListenerConsumer(channel, this));
            channel.basicConsume(finalQueueName, true, getConsumer());
            logger.log(LogType.INFO, "[BadBlock-API] RabbitMQ - Enregistrement d'un listener " + getName() + " (" + getClass().getSimpleName() + ").");
        } catch (Exception error) {
            logger.log(LogType.ERROR, "[BadBlock-API] RabbitMQ -  Erreur pendant le bind des listener.");
            error.printStackTrace();
        }
    }

    abstract void onPacketReceiving(String body);

    RabbitService getRabbitService() {
        return rabbitService;
    }

    public String getName() {
        return name;
    }

    private RabbitListenerType getType() {
        return type;
    }

    private Consumer getConsumer() {
        return consumer;
    }

    private void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }

    boolean isDebug() {
        return debug;
    }
}
