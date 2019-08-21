package fr.badblock.api.tech.mongodb.setting;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import fr.badblock.api.tech.Settings;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MongoSettings extends Settings {

    private String hostname;
    private int port;
    private String username;
    private String password;
    private String database;
    private int workerThreads;

    public MongoSettings(String hostname, int port, String username, String password, String database, int workerThreads) {
        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.password = password;
        this.database = database;
        this.workerThreads = workerThreads;

    }

    @Override
    public MongoClient toFactory() {
        try {
            ServerAddress addr = new ServerAddress(getHostname(), getPort());
            List<MongoCredential> credentialList = new ArrayList<>();
            credentialList.add(MongoCredential.createCredential(getUsername(), getDatabase(), getPassword().toCharArray()));
            return new MongoClient(addr, credentialList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getHostname() {
        return hostname;
    }

    private String getUsername() {
        return username;
    }

    private String getPassword() {
        return password;
    }

    public String getDatabase() {
        return database;
    }

    public int getWorkerThreads() {
        return workerThreads;
    }

    private int getPort() {
        return port;
    }
}
