package fr.badblock.api;

import fr.badblock.api.handler.Handler;
import fr.badblock.api.handler.impl.ModuleHandler;
import fr.badblock.api.module.Module;
import fr.badblock.api.tech.mongodb.MongoService;
import fr.badblock.api.tech.mongodb.setting.MongoSettings;
import org.bukkit.configuration.file.FileConfiguration;
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

    /* Configuration set-up */
    String name = getConfig().getString("mongodb.name");
    String hostname = getConfig().getString("mongodb.hostname");
    int port = getConfig().getInt("mongodb.port");
    String username = getConfig().getString("mongodb.username");
    String password = getConfig().getString("mongodb.password");
    String database = getConfig().getString("mongodb.database");

    @Override
    public void onEnable() {
        instance = this;
        this.mongoService = new MongoService(name, new MongoSettings(hostname, port, username, password, database, 4));
        this.scheduledExecutorService = Executors.newScheduledThreadPool(32);
        moduleHandler = new ModuleHandler(this);
        enableModules();
    }

    @Override
    public void onDisable() {
        moduleHandler.getModules().forEach(this::disableModule);
    }

    /* Configuration part */
    public void loadConfig(){
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
    }

    /* Get configuration File */
    @Override
    public FileConfiguration getConfig() {
        return super.getConfig();
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
