package fr.badblock.api.tech.mongodb;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import fr.badblock.api.BadBlockAPI;
import fr.badblock.api.tech.AutoReconnector;
import fr.badblock.api.tech.TechThread;
import fr.badblock.api.tech.mongodb.methods.MongoMethod;
import fr.badblock.api.tech.mongodb.setting.MongoSettings;
import fr.badblock.api.tech.mongodb.threading.MongoThread;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

public class MongoService extends AutoReconnector {

    private String name;
    private MongoSettings settings;
    private MongoClient mongoClient;
    private boolean isDead;
    private DB db;
    private Random random;
    private List<MongoThread> threads;
    private Queue<MongoMethod> queue;

    MongoService(String name, MongoSettings settings) {
        super(name, settings);
        this.setSettings(settings);
        this.setName(name);
        this.setRandom(new Random());
        this.setThreads(new ArrayList<>());
        this.setQueue(new ConcurrentLinkedDeque<>());
        // Connect
        this.reconnect();
        // Load threads
        for (int i = 0; i < getSettings().getWorkerThreads(); i++) {
            getThreads().add(new MongoThread(this, i));
        }
    }

    private List<MongoThread> getThreads() {
        return threads;
    }

    public void useAsyncMongo(MongoMethod mongoMethod) {
        getQueue().add(mongoMethod);
        dislogeQueue();
    }

    private void dislogeQueue() {
        Optional<MongoThread> availableThread = getAvailableThread();
        if (isUnreachable(availableThread)) {
            return;
        }
        MongoThread thread = availableThread.get();
        thread.stirHimself();
    }

    private boolean isUnreachable(Optional<?> optional) {
        return optional == null || !optional.isPresent();
    }

    private Optional<MongoThread> getAvailableThread() {
        return threads.stream().filter(TechThread::canHandlePacket).findAny();
    }

    private DB db() {
        return this.getDb();
    }

    public MongoClient client() {
        return this.getMongoClient();
    }

    public boolean isAlive() {
        return !isDead();
    }

    public void remove() {
        if (isDead()) {
            BadBlockAPI.getPluginInstance().getLogger().info("[MongoConnector] The service is already dead.");
            return;
        }
        long time = System.currentTimeMillis();
        setDead(true); // Set dead
        getTask().cancel(); // Cancel AutoReconnector task

        // Close channel
        try {
            db().getMongoClient().close();
        } catch (Exception error) {
            BadBlockAPI.getPluginInstance().getLogger().info("[MongoConnector] Something gone wrong while trying to close Mongo.");
            error.printStackTrace();
            return;
        }

        MongoConnector.getInstance().getServices().remove(this.getName());
        BadBlockAPI.getPluginInstance().getLogger().info( "[MongoConnector] Mongo service disconnected (" + (System.currentTimeMillis() - time) + " ms).");
    }

    @Override
    public boolean isConnected() {
        // Disgusting method, TODO find something better
        if (db() == null)
            return false;
        try {
            // test
            getDb().collectionExists("test");
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void reconnect() {
        if (isDead()) {
            return;
        }
        if (isConnected()) {
            return;
        }
        try {
            long time = System.currentTimeMillis();
            setMongoClient(getSettings().toFactory());
            setDb(getMongoClient().getDB(settings.getDatabase()));
            BadBlockAPI.getPluginInstance().getLogger().info("[MongoConnector] Successfully (re)connected to MongoDB service (" + (System.currentTimeMillis() - time) + " ms).");
        } catch (Exception error) {
            error.printStackTrace();
            BadBlockAPI.getPluginInstance().getLogger().info("[MongoConnector] Unable to connect to MongoDB service (" + error.getMessage() + ").");
        }
    }

    private void setRandom(Random random) {
        this.random = random;
    }

    private void setThreads(List<MongoThread> threads) {
        this.threads = threads;
    }

    public Queue<MongoMethod> getQueue() {
        return queue;
    }
    public MongoSettings getSettings() {
        return settings;
    }

    private void setQueue(Queue<MongoMethod> queue) {
        this.queue = queue;
    }

    private DB getDb() {
        return db;
    }

    private MongoClient getMongoClient() {
        return mongoClient;
    }

    private boolean isDead() {
        return isDead;
    }

    private void setMongoClient(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    private void setDb(DB db) {
        this.db = db;
    }

    public Random getRandom() {
        return random;
    }
}

