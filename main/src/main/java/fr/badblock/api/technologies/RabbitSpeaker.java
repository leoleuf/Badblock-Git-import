package fr.badblock.api.technologies;

import fr.badblock.tech.rabbitmq.RabbitConnector;
import fr.badblock.tech.rabbitmq.RabbitService;
import fr.badblock.tech.rabbitmq.packet.RabbitPacket;
import fr.badblock.tech.rabbitmq.packet.RabbitPacketEncoder;
import fr.badblock.tech.rabbitmq.packet.RabbitPacketMessage;
import fr.badblock.tech.rabbitmq.packet.RabbitPacketType;

import java.io.IOException;

public class RabbitSpeaker {

    private static RabbitConnector rabbitConnector = RabbitConnector.getInstance();


    private RabbitService rabbitService;

    /*public RabbitSpeaker(RabbitMQConfig rabbitMQConfig) throws IOException {
        this.setRabbitService(getRabbitConnector().newService("default", rabbitMQConfig.getRabbitPort(), rabbitMQConfig.getRabbitUsername(), rabbitMQConfig.getRabbitPassword(), rabbitMQConfig.getRabbitVirtualHost(), rabbitMQConfig.getRabbitHostname()));
    }*/

    public static RabbitConnector getRabbitConnector() {
        return rabbitConnector;
    }

    public static void setRabbitConnector(RabbitConnector rabbitConnector) {
        RabbitSpeaker.rabbitConnector = rabbitConnector;
    }

    public void sendAsyncUTF8Message(String queueName, String content, long ttl, boolean debug) {
        System.getSecurityManager().checkPermission(new RuntimePermission("badblockDatabase"));

        this.getRabbitService().sendAsyncPacket(new RabbitPacket(new RabbitPacketMessage(ttl, content), queueName, debug, RabbitPacketEncoder.UTF8, RabbitPacketType.MESSAGE_BROKER, null));
    }

    public void sendAsyncUTF8Publisher(String queueName, String content, long ttl, boolean debug) {
        System.getSecurityManager().checkPermission(new RuntimePermission("badblockDatabase"));


        this.getRabbitService().sendAsyncPacket(new RabbitPacket(new RabbitPacketMessage(ttl, content), queueName, debug, RabbitPacketEncoder.UTF8, RabbitPacketType.PUBLISHER, null));
    }

    public void sendSyncUTF8Message(String queueName, String content, long ttl, boolean debug) {
        System.getSecurityManager().checkPermission(new RuntimePermission("badblockDatabase"));

        try {
            this.getRabbitService().sendSyncPacket(new RabbitPacket(new RabbitPacketMessage(ttl, content), queueName, debug, RabbitPacketEncoder.UTF8, RabbitPacketType.MESSAGE_BROKER, null));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendSyncUTF8Publisher(String queueName, String content, long ttl, boolean debug) {
        System.getSecurityManager().checkPermission(new RuntimePermission("badblockDatabase"));

        try {
            this.getRabbitService().sendSyncPacket(new RabbitPacket(new RabbitPacketMessage(ttl, content), queueName, debug, RabbitPacketEncoder.UTF8, RabbitPacketType.PUBLISHER, null));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cut() {
        System.getSecurityManager().checkPermission(new RuntimePermission("badblockDatabase"));

        try {
            this.getRabbitService().getChannel().close();
            this.getRabbitService().getConnection().close();
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    /*
    public void listen(RabbitListener rabbitListener) {
        this.getRabbitService().addListener(new RabbitListener() {
        })
        new RabbitListener(this.getRabbitService(), rabbitListener.getName(), rabbitListener.isDebug(), RabbitListenerType.get(rabbitListener.getType().name())) {
            public void onPacketReceiving(String body) {
                rabbitListener.onPacketReceiving(body);
            }
        };
    }*/

    public RabbitService getRabbitService() {
        return rabbitService;
    }

    public void setRabbitService(RabbitService rabbitService) {
        this.rabbitService = rabbitService;
    }
}
