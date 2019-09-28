package fr.badblock.tech.mongodb;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import fr.badblock.tech.AutoReconnector;
import fr.badblock.tech.TechThread;
import fr.badblock.tech.logger.LogType;
import fr.badblock.tech.logger.Logger;
import fr.badblock.tech.mongodb.methods.MongoMethod;
import fr.badblock.tech.mongodb.setting.MongoSettings;
import fr.badblock.tech.mongodb.threading.MongoThread;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

public class MongoService extends AutoReconnector {

    private MongoClient mongoClient;
    private MongoSettings settings;
    private boolean isDead;
    private DB db;
    private Random random;
    private List<MongoThread> threads;
    private Queue<MongoMethod> queue;
    private Logger logger;

    public MongoService(String name, MongoSettings settings) {
        super(name, settings);
        this.setSettings(settings);
        this.setName(name);
        this.setRandom(new Random());
        this.setThreads(new ArrayList<>());
        this.setQueue(new ConcurrentLinkedDeque<>());
        this.logger = new Logger();
        // Connect
        this.reconnect();
        // Load threads
        for (int i = 0; i < getSettings().getWorkerThreads(); i++) {
            getThreads().add(new MongoThread(this, 1));
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

    public DB db() {
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
            logger.log(LogType.WARNING, "[BadBlock-API] MongoDB - Le service est deja mort.");
            return;
        }
        long time = System.currentTimeMillis();
        setDead(true); // Set dead
        getTask().cancel(); // Cancel AutoReconnector task

        // Close channel
        try {
            db().getMongoClient().close();
        } catch (Exception error) {
            logger.log(LogType.ERROR, "[BadBlock-API] MongoDB - Quelque chose c'est mal passe lors de la fermeture de Mongo.");
            error.printStackTrace();
            return;
        }

        MongoConnector.getInstance().getServices().remove(this.getName());
        logger.log(LogType.SUCCESS, "[BadBlock-API] MongoDB - Fermeture effectue (" + (System.currentTimeMillis() - time) + " ms).");
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
            setDb(getMongoClient().getDB(getSettings().getDatabase()));
            logger.log(LogType.SUCCESS, "[BadBlock-API] MongoDB - Reconnexion a la base de donne effectue avec succes (" + (System.currentTimeMillis() - time) + " ms).");
        } catch (Exception error) {
            error.printStackTrace();
            logger.log(LogType.ERROR, "[BadBlock-API] MongoDB - Impossible de se connecter a la base de donnees (" + error.getMessage() + ").");
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

    public void setSettings(MongoSettings settings) {
        this.settings = settings;
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

    private void setMongoClient(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    private void setDb(DB db) {
        this.db = db;
    }

    public Random getRandom() {
        return random;
    }

    @Override
    public void setDead(boolean dead) {
        isDead = dead;
    }

}

