package fr.badblock.tech.rabbitmq;

import fr.badblock.tech.Connector;
import fr.badblock.tech.rabbitmq.setting.RabbitSettings;

public class RabbitConnector extends Connector<RabbitService> {

    // Singleton instance of RabbitConnector
    private static RabbitConnector instance = new RabbitConnector();

    public static RabbitConnector getInstance() {
        return instance;
    }

    /**
     * Create setting and be back with a RabbitSettings object which is useful for some operations, like using it for different services
     *
     * @param hostnames   > hostnames, we highly recommend DNS
     * @param port        > RabbitMQ Cluster port, 5762 by default
     * @param username    > username of that account
     * @param virtualHost > virtual host where the account will be connected to
     * @param password    > the password of that account
     * @return a RabbitSettings object
     */
    public RabbitSettings createSettings(String[] hostnames, int port, String username, String virtualHost, String password, boolean automaticRecovery, int connectionTimeout, int requestedHeartbeat) {
        return new RabbitSettings(hostnames, port, username, virtualHost, password, automaticRecovery, connectionTimeout, requestedHeartbeat);
    }

}

