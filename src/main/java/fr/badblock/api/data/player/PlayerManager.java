package fr.badblock.api.data.player;

import fr.badblock.api.BadBlockAPI;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerManager {

    private BadBlockAPI badBlockAPI;
    private ConcurrentHashMap<String, PlayerData> cache = new ConcurrentHashMap<>();

    public PlayerManager(BadBlockAPI badBlockAPI) {
        this.badBlockAPI = badBlockAPI;
    }

    /**
     * @param playerName pseudo du joueur voulu
     * @return l'instance de la classe PlayerData enregistré dans le cache du joueur à partir de son pseudo
     */
    public PlayerData getPlayerData(String playerName) {
        return getPlayerData(playerName, false);
    }

    /**
     * @param playerName  pseudo du joueur voulu
     * @param forceRefresh forcer ou non le refresh des données du joueur
     * @return l'intance de la classe PlayerData enregistré dans le cache du joueur à partir de son pseudo
     */
    private PlayerData getPlayerData(String playerName, boolean forceRefresh) {
        if (playerName == null)
            throw new NullPointerException("[BadBlockAPI] PlayerManager - L'objet player n'est pas défini !");

        PlayerData data = cache.get(playerName);
        if (data == null) {
            badBlockAPI.getLogger().info("[BadBlockAPI] PlayerManager - " + playerName + " n'est pas dans le cache !");
        }
        if (forceRefresh) {
            Objects.requireNonNull(data).refreshData();
            return data;
        }

        Objects.requireNonNull(data).refreshIfNeeded();
        return data;
    }

    /**
     * @param uuid uuid du joueur voulu
     * @return l'instance de la classe PlayerData enregistré dans le cache du joueur à partir de son UUID
     */
    public PlayerData getPlayerDataByUUID(UUID uuid) {
        for (PlayerData data : cache.values()) {
            if (data.getPlayerID().equals(uuid))
                return data;
        }

        return null;
    }

    /**
     * Permet de load le joueur à sa connexion et le mettre en cache
     * @param playerName pseudo du joueur voulu
     */
    public void loadPlayer(String playerName) {
        try {
            PlayerData playerData = new PlayerData(playerName, badBlockAPI);
            cache.put(playerName, playerData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Permet de supprimer le cache et d'update les datas du joueur si il est dans le cache
     * @param playerName pseudo du joueur voulu
     */
    public void unloadPlayer(String playerName) {
        if (cache.containsKey(playerName))
            cache.get(playerName).updateData();

        cache.remove(playerName);

    }
}

