package fr.badblock.api.tech.rabbitmq.listener;

import com.rabbitmq.client.*;
import fr.badblock.api.BadBlockAPI;
import fr.badblock.api.tech.rabbitmq.RabbitService;
import fr.badblock.api.tech.rabbitmq.packet.RabbitPacketMessage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;

public abstract class RabbitRequestListener {

    private final RabbitService rabbitService;
    private final String name;
    private final boolean debug;
    private Consumer consumer;
    private BadBlockAPI badBlockAPI;

    protected RabbitRequestListener(RabbitService rabbitService, String name, boolean debug) {
        this.rabbitService = rabbitService;
        this.name = name;
        this.debug = debug;
        this.badBlockAPI = BadBlockAPI.getPluginInstance();
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
                                System.out.println("Request listener : E - HANDLE DELIVERY");
                                System.out.println("Request listener : F - HANDLE DELIVERY: " + r2);
                                RabbitPacketMessage rabbitMessage = RabbitPacketMessage.fromJson(r2);
                                if (rabbitMessage.isAlive()) {
                                    System.out.println("Request listener : G - HANDLE DELIVERY");
                                    if (isDebug()) {
                                        System.out.println("Request listener : H - HANDLE DELIVERY");
                                        badBlockAPI.getLogger().info("[BadBlock-API] RabbitMQ -  Received packet from " + getName() + ": " + rabbitMessage.getMessage());
                                    }

                                    System.out.println("Request listener : I - HANDLE DELIVERY 1");
                                    String msg = rabbitMessage.getMessage();
                                    System.out.println("Request listener : I - HANDLE DELIVERY 2 s");
                                    response = RabbitRequestListener.this.reply(msg);
                                    System.out.println("Request listener : Reply: " + response);

                                    System.out.println("Request listener : I - HANDLE DELIVERY 3");
                                } else if (isDebug()) {
                                    System.out.println("Request listener : J - HANDLE DELIVERY");
                                    badBlockAPI.getLogger().info("[BadBlock-API] RabbitMQ -  Error during a received packet from " + getName() + ": EXPIRED!");
                                    badBlockAPI.getLogger().info("[BadBlock-API] RabbitMQ -  " + rabbitMessage.getMessage());
                                }
                            } catch (Exception error) {
                                System.out.println("Request listener : ERR-1");
                                badBlockAPI.getLogger().info("[BadBlock-API] RabbitMQ -  Error during the handle delivery.");
                                error.printStackTrace();
                            } finally {
                                System.out.println("Request listener : FINALLY");
                                finalChannel.basicPublish("", properties.getReplyTo(), replyProps, Objects.requireNonNull(response).getBytes(StandardCharsets.UTF_8));
                                finalChannel.basicAck(envelope.getDeliveryTag(), false);
                                // RabbitMq consumer worker thread notifies the RPC server owner thread
                                synchronized (this) {
                                    this.notify();
                                }
                            }
                            System.out.println("Request listener : K - HANDLE DELIVERY");

                            // RabbitMq consumer worker thread notifies the RPC server owner thread
                            synchronized (this) {
                                this.notify();
                            }

                        }
                    };
                    System.out.println("Request listener : L - HANDLE DELIVERY");

                    channel.basicConsume(name, false, consumer);
                    System.out.println("Request listener : M - HANDLE DELIVERY");
                    while (true) {
                        System.out.println("Request listener : N - HANDLE DELIVERY");
                        synchronized (consumer) {
                            try {
                                consumer.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Request listener : ERR-0");
                    badBlockAPI.getLogger().info("[BadBlock-API] RabbitMQ -  Error during a request listener bind.");
                    e.printStackTrace();
                } finally {
                    System.out.println("Request listener : CLOSE");
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

