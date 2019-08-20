package fr.badblock.api.tech;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public abstract class Connector<T extends Service> {

    private GsonBuilder gsonBuilder = new GsonBuilder();
    private Gson gson = new GsonBuilder().create();
    private Gson exposedGson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    private ConcurrentMap<String, T> services = new ConcurrentHashMap<>();

    /**
     * Register a new service
     *
     * @param service > Service
     * @return service
     */
    public T registerService(T service) {
        getServices().put(service.getName(), service);
        return service;
    }

    /**
     * Unregister an existing service
     *
     * @param service > Service
     * @return service
     */
    public T unregisterService(T service) {
        getServices().remove(service.getName());
        return service;
    }


    public ConcurrentMap<String, T> getServices() {
        return services;
    }
}
