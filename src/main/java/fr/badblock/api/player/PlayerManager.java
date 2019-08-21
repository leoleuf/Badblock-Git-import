package fr.badblock.api.player;

import fr.badblock.api.BadBlockAPI;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerManager {

    private BadBlockAPI badBlockAPI;
    private ConcurrentHashMap<String, PlayerData> cache = new ConcurrentHashMap<>();

    public PlayerManager(BadBlockAPI badBlockAPI) {
        this.badBlockAPI = badBlockAPI;
    }

    public PlayerData getPlayerData(String playerName) {
        return getPlayerData(playerName, false);
    }

    private PlayerData getPlayerData(String playerName, boolean forceRefresh) {
        if (playerName == null)
            throw new NullPointerException("Le paramètres du player n'est pas défini !");

        PlayerData data = cache.get(playerName);

        if (forceRefresh) {
            data.refreshData();
            return data;
        }

        data.refreshIfNeeded();
        if (data == null)
            badBlockAPI.getLogger().info("[BadBlockAPI] PlayerManager - " + playerName + " n'est pas dans le cache !");
        return data;
    }

    public PlayerData getPlayerDataByUUID(UUID uuid) {
        for (PlayerData data : cache.values()) {
            if (data.getPlayerID().equals(uuid))
                return data;
        }

        return null;
    }

    public void loadPlayer(String playerName) {
        try {
            PlayerData playerData = new PlayerData(playerName, badBlockAPI);
            cache.put(playerName, playerData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unloadPlayer(String playerName) {
        if (cache.containsKey(playerName))
            cache.get(playerName).updateData();

        cache.remove(playerName);

    }
}

