package fr.badblock.api.tech.rabbitmq.packet;

import fr.badblock.api.tech.TechThread;
import fr.badblock.api.tech.rabbitmq.RabbitService;
import fr.badblock.api.tech.rabbitmq.threading.RabbitThread;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class RabbitPacketManager {

    private static Map<RabbitService, RabbitPacketManager> instances = new ConcurrentHashMap<>();

    private List<RabbitThread> threads = new ArrayList<>();
    private Queue<RabbitPacket> queue = new ConcurrentLinkedDeque<>();
    private RabbitService rabbitService;


    private RabbitPacketManager(RabbitService rabbitService) {
        setRabbitService(rabbitService);
        for (int i = 0; i < rabbitService.getSettings().getWorkerThreads(); i++) {
            getThreads().add(new RabbitThread(this, i));
        }
    }

    public void sendPacket(RabbitPacket rabbitPacket) {
        getQueue().add(rabbitPacket);
        dislogeQueue();
    }

    private void dislogeQueue() {
        Optional<RabbitThread> availableThread = getAvailableThread();
        if (isUnreachable(availableThread)) {
            return;
        }
        RabbitThread thread = availableThread.get();
        thread.stirHimself();
    }

    private boolean isUnreachable(Optional<?> optional) {
        return optional == null || !optional.isPresent();
    }

    private Optional<RabbitThread> getAvailableThread() {
        return threads.stream().filter(TechThread::canHandlePacket).findAny();
    }

    public boolean isAlive() {
        return !getRabbitService().isDead();
    }

    public static RabbitPacketManager getInstance(RabbitService rabbitService) {
        RabbitPacketManager packetManager = instances.get(rabbitService);
        if (packetManager == null) {
            packetManager = new RabbitPacketManager(rabbitService);
            instances.put(rabbitService, packetManager);
        }
        return packetManager;
    }

    public RabbitService getRabbitService() {
        return rabbitService;
    }

    private void setRabbitService(RabbitService rabbitService) {
        this.rabbitService = rabbitService;
    }

    public Queue<RabbitPacket> getQueue() {
        return queue;
    }

    private List<RabbitThread> getThreads(){
        return threads;
    }
}
