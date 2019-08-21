package fr.badblock.api;

import fr.badblock.api.handler.Handler;
import fr.badblock.api.handler.impl.ModuleHandler;
import fr.badblock.api.module.Module;
import fr.badblock.api.tech.mongodb.MongoService;
import fr.badblock.api.tech.mongodb.setting.MongoSettings;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class BadBlockAPI extends JavaPlugin {

    private List<Handler> handlers;
    private ModuleHandler moduleHandler;
    private static BadBlockAPI instance;
    private MongoService mongoService;
    private ScheduledExecutorService scheduledExecutorService;

    @Override
    public void onEnable() {
        instance = this;
        this.mongoService = new MongoService("mongodb", new MongoSettings("", 0, "", "", "", 4));
        this.scheduledExecutorService = Executors.newScheduledThreadPool(32);
        moduleHandler = new ModuleHandler(this);
        enableModules();
    }

    @Override
    public void onDisable() {
        moduleHandler.getModules().forEach(this::disableModule);
    }


    public static BadBlockAPI getPluginInstance() {
        return instance;
    }

    public ScheduledExecutorService getScheduledExecutorService(){
        return scheduledExecutorService;
    }

    /* MODULE SECTION */

    private void enableModules() {

    }

    private void enableModule(Module module) {
        moduleHandler.addModule(module);
        getModule(module.getModuleName()).enable();
    }

    private void disableModule(Module module) {
        getModule(module.getModuleName()).disable();
        moduleHandler.removeModule(module);
    }

    private Module getModule(String module) {
        return ((ModuleHandler) getHandler("modules")).getModule(module);
    }

    /* HANDLER SECTION */
    private Handler getHandler(String handlerName) {
        return handlers.stream()
                .filter(handler -> handler.getHandlerName().equalsIgnoreCase(handlerName))
                .findFirst()
                .orElse(null);
    }

    public MongoService getMongoService(){
        return this.mongoService;
    }

}
