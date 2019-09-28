package fr.badblock.api;

import fr.badblock.api.chat.Chat;
import fr.badblock.api.chat.ChatCommand;
import fr.badblock.api.data.rank.RankManager;
import fr.badblock.api.database.PlayerDataManager;
import fr.badblock.api.command.PermCommand;
import fr.badblock.api.command.RankCommand;
import fr.badblock.api.data.player.PlayerManager;
import fr.badblock.api.database.RankDataManager;
import fr.badblock.api.handler.Handler;
import fr.badblock.api.handler.impl.ModuleHandler;
import fr.badblock.api.listener.PlayerEvent;
import fr.badblock.api.module.Module;
import fr.badblock.tech.mongodb.MongoService;
import fr.badblock.tech.mongodb.setting.MongoSettings;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class BadBlockAPI extends JavaPlugin {
    /* Variables declaration */
    private List<Handler> handlers;
    private ModuleHandler moduleHandler;
    private static BadBlockAPI instance;
    private MongoService mongoService;
    private ScheduledExecutorService scheduledExecutorService;

    private PlayerDataManager playerDataManager;
    private RankDataManager rankDataManager;

    private PlayerManager playerManager;
    private RankManager rankManager;

    /* Configuration set-up */
    private String name = getConfig().getString("mongodb.name");
    private String hostname = getConfig().getString("mongodb.hostname");
    private int port = getConfig().getInt("mongodb.port");
    private String username = getConfig().getString("mongodb.username");
    private String password = getConfig().getString("mongodb.password");
    private String database = getConfig().getString("mongodb.database");

    @Override
    public void onEnable() {
        instance = this;
        this.scheduledExecutorService = Executors.newScheduledThreadPool(32);
        moduleHandler = new ModuleHandler(this);
        enableModules();
        loadConfig();
        commandsHandler();
        listenersHandler();
        this.mongoService = new MongoService(name, new MongoSettings(hostname, port, username, password, database, 4));
        rankDataManager = new RankDataManager();
        playerDataManager = new PlayerDataManager();
        playerManager = new PlayerManager(this);
        rankManager = new RankManager(this);
        rankDataManager.getRankList().forEach(rank -> rankManager.loadRank(rank));
    }

    @Override
    public void onDisable() {
        moduleHandler.getModules().forEach(this::disableModule);
    }

    /**
     * Register commands
     **/
    private void commandsHandler() {
        getCommand("wpmchat").setExecutor(new ChatCommand());
        getCommand("rank").setExecutor(new RankCommand(this));
        getCommand("wpmperms").setExecutor(new PermCommand());
    }

    /**
     * Register listeners
     **/

    private void listenersHandler() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new Chat(), this);
        pm.registerEvents(new PlayerEvent(this), this);
    }

    /* Configuration part */
    private void loadConfig() {
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

    public ScheduledExecutorService getScheduledExecutorService() {
        return scheduledExecutorService;
    }

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }

    public RankDataManager getRankDataManager() {
        return rankDataManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public RankManager getRankManager() {
        return rankManager;
    }

    /* MODULE SECTION */

    private void enableModules() {

    }

    /**
     * Enabling Modules
     **/
    private void enableModule(Module module) {
        moduleHandler.addModule(module);
        getModule(module.getModuleName()).enable();
    }

    /**
     * Disabling Modules
     **/
    private void disableModule(Module module) {
        getModule(module.getModuleName()).disable();
        moduleHandler.removeModule(module);
    }

    /**
     * Get Modules
     **/
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

    /**
     * Get MongoService
     **/
    public MongoService getMongoService() {
        return this.mongoService;
    }

}
