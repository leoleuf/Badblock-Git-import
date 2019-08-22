package fr.badblock.api.data.rank;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import fr.badblock.api.BadBlockAPI;
import fr.badblock.api.database.RankDataManager;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class RankData {

    private BadBlockAPI badBlockAPI;
    private long lastRefresh;
    private long rankId;
    private boolean loaded;
    private RankBean rankBean;
    private RankDataManager rankDataManager;

    RankData(long rankId, BadBlockAPI badBlockAPI) {
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

    public long getRankID() {
        refreshIfNeeded();
        return rankBean.getRankId();
    }

    public String getRankName() {
        refreshIfNeeded();
        return rankBean.getRankName();
    }

    public int getRankPower() {
        refreshIfNeeded();
        return rankBean.getPower();
    }

    public String getRankTag() {
        refreshIfNeeded();
        return rankBean.getTag();
    }

    public String getRankPrefix() {
        refreshIfNeeded();
        return rankBean.getPrefix();
    }

    public String getRankSuffix() {
        refreshIfNeeded();
        return rankBean.getSuffix();
    }

    public String getPermissionsJson() {
        refreshIfNeeded();
        return rankBean.getPermissionsJson();
    }

    public List<String> getPermissions() {
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        return new Gson().fromJson(getPermissionsJson(), type);
    }

    public void addPermissions(String permissions) {
        List<String> perm = getPermissions();
        perm.add(permissions);
        setPermissions(perm);
    }

    public void addPermissions(List<String> permissions) {
        List<String> perm = getPermissions();
        perm.addAll(permissions);

        setPermissions(perm);
    }

    public boolean hasPermissions(String permissions) {
        return getPermissions().contains(permissions);
    }

    public void setPermissions(List<String> permissions) {
        refreshData();
        rankBean.setPermissionsJson(new Gson().toJson(permissions));
        updateData();
    }

    public void removePermission(String perm) {
        List<String> perms = getPermissions();
        perms.remove(perm);
        setPermissions(perms);
    }

    public void removePermission(List<String> perm) {
        List<String> perms = getPermissions();
        perms.forEach(perm::remove);
        setPermissions(perms);
    }

    public void setRankID(long rankId) {
        refreshData();
        rankBean.setRankId(rankId);
        updateData();
    }

    public void setRankName(String rankName) {
        refreshData();
        rankBean.setRankName(rankName);
        updateData();
    }

    public void setPower(int power) {
        refreshData();
        rankBean.setPower(power);
        updateData();
    }

    public void setTag(String tag) {
        refreshData();
        rankBean.setTag(tag);
        updateData();
    }

    public void setPrefix(String prefix) {
        refreshData();
        rankBean.setPrefix(prefix);
        updateData();
    }

    public void setSuffix(String suffix) {
        refreshData();
        rankBean.setSuffix(suffix);
        updateData();
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
