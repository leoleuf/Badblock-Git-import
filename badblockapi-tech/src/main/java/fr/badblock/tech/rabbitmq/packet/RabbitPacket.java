package fr.badblock.tech.rabbitmq.packet;


import fr.badblock.tech.rabbitmq.Callback;

public class RabbitPacket {

    private RabbitPacketMessage rabbitPacketMessage;
    private String queue;
    private boolean debug;
    private RabbitPacketEncoder encoder;
    private RabbitPacketType type;
    private Callback<String> callback;

    public RabbitPacket(RabbitPacketMessage rabbitPacketMessage, String queue, boolean debug, RabbitPacketEncoder rabbitPacketEncoder, RabbitPacketType rabbitPacketType, Callback<String> callback) {
        this.rabbitPacketMessage = rabbitPacketMessage;
        this.queue = queue;
        this.debug = debug;
        encoder = rabbitPacketEncoder;
        type = rabbitPacketType;
        this.callback = callback;
    }

    public RabbitPacketMessage getRabbitPacketMessage() {
        return rabbitPacketMessage;
    }

    public String getQueue() {
        return queue;
    }

    public boolean isDebug() {
        return debug;
    }

    public RabbitPacketEncoder getEncoder() {
        return encoder;
    }

    public RabbitPacketType getType() {
        return type;
    }

    public Callback<String> getCallback() {
        return callback;
    }
}
