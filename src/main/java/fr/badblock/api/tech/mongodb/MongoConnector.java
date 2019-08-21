package fr.badblock.api.tech.mongodb;

import fr.badblock.api.tech.Connector;
import fr.badblock.api.tech.mongodb.setting.MongoSettings;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MongoConnector extends Connector<MongoService> {


    // MongoConnector singleton instance
    private static MongoConnector instance = new MongoConnector();

    // Private fields
    private ConcurrentMap<String, MongoService> services = new ConcurrentHashMap<>();


    static MongoConnector getInstance() {
        return instance;
    }

    /**
     * Create credentials and be back with a MongoCredentials object which is useful for some operations, like using it in different services
     *
     * @param name     > the name of credentials instance
     * @param hostname > hostname, we higly recommend DNS
     * @param port     > Mongo Cluster port, 27017 by default
     * @param password > the password of that account
     * @return a MongoCredentials object
     */
    public MongoSettings createSettings(String[] hostnames, int port, String username, String database, String password, int workerThreads) {
        return new MongoSettings(hostnames, port, username, database, password, workerThreads);
    }

    /**
     * Adding a new service
     *
     * @param name          > name of the service
     * @param MongoSettings > credentials
     * @return a MongoService object ready to work
     */
    public MongoService createService(String name, MongoSettings MongoCredentials) {
        return new MongoService(name, MongoCredentials);
    }

    /**
     * Register a new service
     *
     * @param mongoService > MongoDB service
     */
    public MongoService registerService(MongoService mongoService) {
        services.put(mongoService.getName(), mongoService);
        return mongoService;
    }

    /**
     * Unregister an existing service
     *
     * @param mongoService > MongoDB service
     */
    public MongoService unregisterService(MongoService mongoService) {
        services.remove(mongoService.getName());
        return mongoService;
    }

}

