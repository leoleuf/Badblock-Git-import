package fr.badblock.gameapi;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class Game<T extends GamePlayer> {

    // Game name
    protected String name;
    // Game description
    protected String description;
    // Game cache with all players
    private List<UUID> cache = new ArrayList<>();
    // Map with all players registered by uuid with their data type
    private Map<UUID, T> players = new HashMap<>();
    // Current state
    private GameState state = GameState.WAITING;
    // Game listeners
    protected AbstractGameHandler handler;
    // Required players
    protected int requiredPlayers;
    // Current task
    private BukkitTask task;

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public List<UUID> getCache()
    {
        return cache;
    }

    public Map<UUID, T> getPlayers()
    {
        return players;
    }

    public boolean isState(GameState state)
    {
        return this.state.equals(state);
    }

    public void setState(GameState state)
    {
        this.state = state;
    }

    public AbstractGameHandler getHandler() {
        return handler;
    }

    public int getRequiredPlayers() {
        return requiredPlayers;
    }

    public BukkitTask getTask()
    {
        return task;
    }

    public void setTask(BukkitTask task)
    {
        this.task = task;
    }

    // Send a message to all players
    public void sendMessage(String message)
    {
        cache.forEach(uuid -> Bukkit.getPlayer(uuid).sendMessage(message));
    }

    // Change the level to all players
    public void setLevel(int level)
    {
        cache.forEach(uuid -> Bukkit.getPlayer(uuid).setLevel(level));
    }

}
