package fr.badblock.tech.mongodb;

import fr.badblock.tech.Connector;
import fr.badblock.tech.mongodb.setting.MongoSettings;

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
     * @param hostname définie l'hostname à utiliser
     * @param port définie le port de mongodb
     * @param username définie l'username de la base de donnée
     * @param database définie la base de donnée MongoDB à choisir
     * @param password définie le mot de passe de la base de donnée
     * @param workerThreads définie le nombre de threads sur le quelle mongodb travail
     * @return l'objet MongoSettings
     */
    public MongoSettings createSettings(String hostname, int port, String username, String database, String password, int workerThreads) {
        return new MongoSettings(hostname, port, username, database, password, workerThreads);
    }
    /**
     * Adding a new service
     *
     * @param name > nom du service
     * @param MongoCredentials > credentials
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

