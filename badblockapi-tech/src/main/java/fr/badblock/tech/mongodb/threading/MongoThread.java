package fr.badblock.tech.mongodb.threading;

import fr.badblock.tech.TechThread;
import fr.badblock.tech.mongodb.MongoService;
import fr.badblock.tech.mongodb.methods.MongoMethod;

public class MongoThread extends TechThread<MongoMethod>
{

    private MongoService mongoService;

    public MongoThread(MongoService mongoService, int id)
    {
        super("MongoThread", mongoService.getQueue(), id);
        setMongoService(mongoService);
    }

    @Override
    public void work(MongoMethod mongoMethod) {
        mongoMethod.run(getMongoService());
    }

    @Override
    public String getErrorMessage()
    {
        return "[BadBlock-API] MongoDB - Une erreur c'est produite lors de l'envoie du packet.";
    }

    @Override
    public boolean isServiceAlive()
    {
        return getMongoService().isAlive();
    }

    private MongoService getMongoService() {
        return mongoService;
    }

    private void setMongoService(MongoService mongoService) {
        this.mongoService = mongoService;
    }
}

