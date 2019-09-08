package fr.badblock.tech.mongodb.methods;

import fr.badblock.tech.mongodb.MongoService;

public abstract class MongoMethod {

    private MongoService mongoService;

    public abstract void run(MongoService mongoService2);

}

