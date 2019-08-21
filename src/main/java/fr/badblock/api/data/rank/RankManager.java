package fr.badblock.api.data.rank;

import fr.badblock.api.BadBlockAPI;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class RankManager {

    private BadBlockAPI badBlockAPI;
    public ConcurrentHashMap<Long, RankData> cache = new ConcurrentHashMap<>();

    public RankManager(BadBlockAPI badBlockAPI){
        this.badBlockAPI = badBlockAPI;
    }

    public RankData getRankData(long rankId){
        return getRankData(rankId, false);
    }

    private RankData getRankData(long rankId, boolean forceRefresh){
        if(rankId < 0){
            throw new NullPointerException("[BadBlockAPI] RankManager - L'objet rankId ne peut être négatif !");
        }
        RankData data = cache.get(rankId);
        if (data == null) {
            badBlockAPI.getLogger().info("[BadBlockAPI] RankManager - " + rankId + " n'est pas dans le cache !");
        }
        if (forceRefresh) {
            Objects.requireNonNull(data).refreshData();
            return data;
        }

        Objects.requireNonNull(data).refreshIfNeeded();
        return data;
    }

    public RankData getRankDataByName(String name) {
        for (RankData data : cache.values()) {
            if (data.getRankName().equals(name))
                return data;
        }

        return null;
    }

    public void loadRank(long rankId) {
        try {
            RankData rankData = new RankData(rankId, badBlockAPI);
            cache.put(rankId, rankData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unloadRank(long rankId) {
        if (cache.containsKey(rankId))
            cache.get(rankId).updateData();

        cache.remove(rankId);

    }
}
