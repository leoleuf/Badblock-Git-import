package fr.badblock.tech.rabbitmq.listener;

import com.rabbitmq.client.*;
import fr.badblock.tech.logger.LogType;
import fr.badblock.tech.logger.Logger;
import fr.badblock.tech.rabbitmq.RabbitService;
import fr.badblock.tech.rabbitmq.packet.RabbitPacketMessage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;

public abstract class RabbitRequestListener {

    private final RabbitService rabbitService;
    private final String name;
    private final boolean debug;
    private Consumer consumer;
    private Logger logger;

    protected RabbitRequestListener(RabbitService rabbitService, String name, boolean debug) {
        this.rabbitService = rabbitService;
        this.name = name;
        this.debug = debug;
        this.logger = new Logger();
    }

    public void load() {
        new Thread("RabbitMQ/RequestListener/" + name + "/" + UUID.randomUUID().toString()) {
            @Override
            public void run() {
                Channel channel = null;
                try {
                    if (!getRabbitService().isAlive()) {
                        return;
                    }
                    channel = getRabbitService().getConnection().createChannel();

                    final Channel finalChannel = channel;

                    channel.queueDeclare(name, false, false, false, null);

                    channel.basicQos(1);

                    Consumer consumer = new DefaultConsumer(channel) {
                        @Override
                        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                            AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                                    .Builder()
                                    .correlationId(properties.getCorrelationId())
                                    .build();

                            String response = null;

                            try {
                                String r2 = new String(body, StandardCharsets.UTF_8);

                                if (getRabbitService().isDead()) {
                                    return;
                                }
                                logger.log(LogType.INFO, "Request listener : E - HANDLE DELIVERY");
                                logger.log(LogType.INFO, "Request listener : F - HANDLE DELIVERY: " + r2);
                                RabbitPacketMessage rabbitMessage = RabbitPacketMessage.fromJson(r2);
                                if (rabbitMessage.isAlive()) {
                                    logger.log(LogType.INFO, "Request listener : G - HANDLE DELIVERY");
                                    if (isDebug()) {
                                        logger.log(LogType.INFO, "Request listener : H - HANDLE DELIVERY");
                                        logger.log(LogType.INFO, "[BadBlock-API] RabbitMQ -  Received packet from " + getName() + ": " + rabbitMessage.getMessage());
                                    }

                                    logger.log(LogType.INFO, "Request listener : I - HANDLE DELIVERY 1");
                                    String msg = rabbitMessage.getMessage();
                                    logger.log(LogType.INFO, "Request listener : I - HANDLE DELIVERY 2 s");
                                    response = RabbitRequestListener.this.reply(msg);
                                    logger.log(LogType.INFO, "Request listener : Reply: " + response);

                                    logger.log(LogType.INFO, "Request listener : I - HANDLE DELIVERY 3");
                                } else if (isDebug()) {
                                    logger.log(LogType.INFO, "Request listener : J - HANDLE DELIVERY");
                                    logger.log(LogType.ERROR, "[BadBlock-API] RabbitMQ -  Error during a received packet from " + getName() + ": EXPIRED!");
                                    logger.log(LogType.ERROR, "[BadBlock-API] RabbitMQ -  " + rabbitMessage.getMessage());
                                }
                            } catch (Exception error) {
                                logger.log(LogType.ERROR, "Request listener : ERR-1");
                                logger.log(LogType.ERROR, "[BadBlock-API] RabbitMQ -  Error during the handle delivery.");
                                error.printStackTrace();
                            } finally {
                                logger.log(LogType.SUCCESS, "Request listener : FINALLY");
                                finalChannel.basicPublish("", properties.getReplyTo(), replyProps, Objects.requireNonNull(response).getBytes(StandardCharsets.UTF_8));
                                finalChannel.basicAck(envelope.getDeliveryTag(), false);
                                // RabbitMq consumer worker thread notifies the RPC server owner thread
                                synchronized (this) {
                                    this.notify();
                                }
                            }
                            logger.log(LogType.INFO, "Request listener : K - HANDLE DELIVERY");

                            // RabbitMq consumer worker thread notifies the RPC server owner thread
                            synchronized (this) {
                                this.notify();
                            }

                        }
                    };
                    logger.log(LogType.INFO, "Request listener : L - HANDLE DELIVERY");

                    channel.basicConsume(name, false, consumer);
                    logger.log(LogType.INFO, "Request listener : M - HANDLE DELIVERY");
                    while (true) {
                        logger.log(LogType.INFO, "Request listener : N - HANDLE DELIVERY");
                        synchronized (consumer) {
                            try {
                                consumer.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.log(LogType.ERROR, "Request listener : ERR-0");
                    logger.log(LogType.ERROR, "[BadBlock-API] RabbitMQ -  Error during a request listener bind.");
                    e.printStackTrace();
                } finally {
                    logger.log(LogType.SUCCESS, "Request listener : CLOSE");
                    if (channel != null)
                        try {
                            channel.close();
                        } catch (Exception ignored) {
                        }
                }
            }
        }.start();
    }

    private String reply(String body) {
        return body;
    }

    private RabbitService getRabbitService() {
        return rabbitService;
    }

    private boolean isDebug() {
        return debug;
    }

}

