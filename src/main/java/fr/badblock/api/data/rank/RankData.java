package fr.badblock.api.data.rank;

import com.google.gson.JsonArray;
import fr.badblock.api.BadBlockAPI;
import fr.badblock.api.database.RankDataManager;


public class RankData {

    private BadBlockAPI badBlockAPI;
    private long lastRefresh;
    private long rankId;
    private boolean loaded;
    private RankBean rankBean;
    private RankDataManager rankDataManager;

    RankData(long rankId, BadBlockAPI badBlockAPI){
        this.badBlockAPI = badBlockAPI;
        this.rankId = rankId;
        this.rankDataManager = new RankDataManager();
        rankBean = new RankBean(rankId,
                null,
                0,
                null,
                null,
                null,
                null);
        refreshData();
    }

    public long getRankID(){
        refreshIfNeeded();
        return rankBean.getRankId();
    }

    public String getRankName(){
        refreshIfNeeded();
        return rankBean.getRankName();
    }

    public int getRankPower(){
        refreshIfNeeded();
        return rankBean.getPower();
    }

    public String getRankTag(){
        refreshIfNeeded();
        return rankBean.getTag();
    }

    public String getRankPrefix(){
        refreshIfNeeded();
        return rankBean.getPrefix();
    }

    public String getRankSuffix(){
        refreshIfNeeded();
        return rankBean.getSuffix();
    }

    public JsonArray getRankPermissions(){
        refreshIfNeeded();
        return rankBean.getPermissions();
    }

    boolean refreshData() {
        lastRefresh = System.currentTimeMillis();
        try {
            rankBean = rankDataManager.getRank(rankId, rankBean);
            loaded = true;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Permet d'update les données du joueur dans la base de donnée MongoDB
     */
    void updateData() {
        if (rankBean != null && loaded) {
            try {
                rankDataManager.updateRank(rankBean);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Permet de rafraichir le cache si le temps est expièr
     */
    void refreshIfNeeded() {
        if (getLastRefresh() + 1000 * 60 < System.currentTimeMillis()) {
            refreshData();
        }
    }

    public long getLastRefresh() {
        return lastRefresh;
    }
}
