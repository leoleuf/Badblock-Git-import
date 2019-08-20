package fr.badblock.api.tech.mongodb.setting;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import fr.badblock.api.tech.Settings;

import java.util.Random;


public class MongoSettings extends Settings {

    private String[] hostnames;
    private int port;
    private String username;
    private String password;
    private String database;
    private int workerThreads;

    public MongoSettings(String[] hostnames, int port, String username, String password, String database, int workerThreads) {
        this.hostnames = hostnames;
        this.port = port;
        this.username = username;
        this.password = password;
        this.database = database;
        this.workerThreads = workerThreads;

    }

    @Override
    public MongoClient toFactory() {
        try {
            String[] hostnames = getHostnames();
            int hostnameId = new Random().nextInt(hostnames.length);
            System.out.println("mongodb://" + getUsername() + ":" + getPassword() + "@" + hostnames[hostnameId] + ":" + getPort() + "/" + getDatabase());
            MongoClient mongo = new MongoClient(
                    new MongoClientURI("mongodb://" + getUsername() + ":" + getPassword() + "@" + hostnames[hostnameId] + ":" + getPort() + "/" + getDatabase()));
            return mongo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String[] getHostnames() {
        return hostnames;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDatabase() {
        return database;
    }

    public int getWorkerThreads() {
        return workerThreads;
    }

    public int getPort() {
        return port;
    }
}
