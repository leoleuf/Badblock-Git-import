package fr.badblock.tech.rabbitmq.setting;

import com.rabbitmq.client.ConnectionFactory;
import fr.badblock.tech.Settings;

import java.util.Random;

public class RabbitSettings extends Settings {

    private String[] hostnames;
    private int port;
    private String username;
    private String virtualHost;
    private String password;
    private boolean automaticRecovery;
    private int connectionTimeout;
    private int requestedHeartbeat;

    public RabbitSettings(String[] hostnames, int port, String username, String virtualHost, String password, boolean automaticRecovery, int connectionTimeout, int requestedHeartbeat) {
        this.hostnames = hostnames;
        this.port = port;
        this.username = username;
        this.virtualHost = virtualHost;
        this.password = password;
        this.automaticRecovery = automaticRecovery;
        this.connectionTimeout = connectionTimeout;
        this.requestedHeartbeat = requestedHeartbeat;
    }

    public ConnectionFactory toFactory() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        Random random = new Random();
        connectionFactory.setHost(getHostnames()[random.nextInt(getHostnames().length)]);
        connectionFactory.setPort(getPort());
        connectionFactory.setUsername(getUsername());
        connectionFactory.setVirtualHost(getVirtualHost());
        connectionFactory.setPassword(getPassword());
        connectionFactory.setAutomaticRecoveryEnabled(isAutomaticRecovery());
        connectionFactory.setConnectionTimeout(getConnectionTimeout());
        connectionFactory.setRequestedHeartbeat(getRequestedHeartbeat());
        return connectionFactory;
    }

    private String[] getHostnames() {
        return hostnames;
    }

    private int getPort() {
        return port;
    }

    private String getUsername() {
        return username;
    }

    private String getVirtualHost() {
        return virtualHost;
    }

    private String getPassword() {
        return password;
    }

    private boolean isAutomaticRecovery() {
        return automaticRecovery;
    }

    private int getConnectionTimeout() {
        return connectionTimeout;
    }

    private int getRequestedHeartbeat() {
        return requestedHeartbeat;
    }

    public int getWorkerThreads() {
        return 4;
    }
}
