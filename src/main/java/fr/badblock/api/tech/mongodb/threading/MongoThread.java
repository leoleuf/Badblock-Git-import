package fr.badblock.api.tech.mongodb.threading;

import fr.badblock.api.tech.TechThread;
import fr.badblock.api.tech.mongodb.MongoService;
import fr.badblock.api.tech.mongodb.methods.MongoMethod;

public class MongoThread extends TechThread<MongoMethod>
{

    private MongoService mongoService;

    public MongoThread(MongoService mongoService, int id)
    {
        super("MongoThread", mongoService.getQueue(), id);
        setMongoService(mongoService);
    }

    @Override
    public void work(MongoMethod mongoMethod) throws Exception
    {
        mongoMethod.run(getMongoService());
    }

    @Override
    public String getErrorMessage()
    {
        return "[MongoConnector] An error occurred while trying to send packet.";
    }

    @Override
    public boolean isServiceAlive()
    {
        return getMongoService().isAlive();
    }

    public MongoService getMongoService() {
        return mongoService;
    }

    public void setMongoService(MongoService mongoService) {
        this.mongoService = mongoService;
    }
}

