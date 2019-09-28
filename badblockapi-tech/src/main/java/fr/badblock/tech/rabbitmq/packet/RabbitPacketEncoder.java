package fr.badblock.tech.rabbitmq.packet;

public enum RabbitPacketEncoder
{

    UTF8("UTF-8");

    private String name;

    RabbitPacketEncoder(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
