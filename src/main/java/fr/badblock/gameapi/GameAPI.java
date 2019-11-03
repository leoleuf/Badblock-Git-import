package fr.badblock.gameapi;

import fr.badblock.gameapi.listeners.GameListeners;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class GameAPI {

    private static GameAPI instance;
    private JavaPlugin plugin;
    private Game game;

    public GameAPI(JavaPlugin plugin)
    {
        instance = this;
        this.plugin = plugin;
        registerListeners();
    }

    public static GameAPI getInstance()
    {
        return instance;
    }

    public JavaPlugin getPlugin()
    {
        return plugin;
    }

    public Game getGame()
    {
        return game;
    }

    private void registerListeners()
    {
        PluginManager plugin = this.plugin.getServer().getPluginManager();
        plugin.registerEvents(new GameListeners(this), this.plugin);
    }

    // ToDo: Add many checks
    public void registerGame(Game game)
    {
        if (this.game != null) return;
        this.game = game;
        plugin.getLogger().log(Level.INFO, "Registered game " + game.getName() + " successfully !");
    }

}

