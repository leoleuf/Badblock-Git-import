package fr.badblock.api.tech.rabbitmq.threading;

import fr.badblock.api.tech.TechThread;
import fr.badblock.api.tech.rabbitmq.RabbitService;
import fr.badblock.api.tech.rabbitmq.packet.RabbitPacket;
import fr.badblock.api.tech.rabbitmq.packet.RabbitPacketManager;

public class RabbitThread extends TechThread<RabbitPacket> {

    private RabbitPacketManager packetManager;

    public RabbitThread(RabbitPacketManager packetManager, int id) {
        super("RabbitThread", packetManager.getQueue(), id);
        setPacketManager(packetManager);
    }

    @Override
    public void work(RabbitPacket rabbitPacket) throws Exception {
        RabbitService rabbitService = getPacketManager().getRabbitService();
        rabbitService.sendSyncPacket(rabbitPacket);
    }

    @Override
    public String getErrorMessage() {
        return "[BadBlockAPI] RabbitMQ - Une erreur c'est produite lors de l'envoie du packet.";
    }

    @Override
    public boolean isServiceAlive() {
        return getPacketManager().isAlive();
    }

    private RabbitPacketManager getPacketManager() {
        return packetManager;
    }

    private void setPacketManager(RabbitPacketManager packetManager) {
        this.packetManager = packetManager;
    }
}
