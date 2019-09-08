package fr.badblock.api.data.rank;

import fr.badblock.api.BadBlockAPI;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import fr.badblock.api.database.RankDataManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RankData {

    private BadBlockAPI badBlockAPI;
    private long lastRefresh;
    private int rankId;
    private boolean loaded;
    private RankBean rankBean;
    private RankDataManager rankDataManager;

    RankData(int rankId, BadBlockAPI badBlockAPI) {
        this.badBlockAPI = badBlockAPI;
        this.rankId = rankId;
        this.rankDataManager = new RankDataManager();
        rankBean = new RankBean(rankId,
                null,
                0,
                null,
                null,
                null,
                null,
                false);
        refreshData();
    }

    public int getRankID() {
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

    public boolean isStaff() {
        refreshIfNeeded();
        return rankBean.isStaff();
    }

    public void setStaff(boolean stats) {
        refreshData();
        rankBean.setStaff(stats);
        updateData();
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

    /**
     * @return la map des permissions qui a été transcodé en json et stocké
     */
    private String getPermissionsJson() {
        refreshIfNeeded();
        return rankBean.getPermissionsJson();
    }

    /**
     * @return La map des permissions<Serveur, List<Permissions>> stocké en json
     */
    private Map<String, List<String>> transcodePermission() {
        return new Gson().fromJson(getPermissionsJson(), new TypeToken<Map<String, List<String>>>() {
        }.getType());

    }

    /**
     * @return sois une nouvelle hashmap si le joueur n'as aucune permission, soit la liste des permissions du joueur
     */
    public Map<String, List<String>> getPermissions() {
        Map<String, List<String>> perms = transcodePermission();
        if (perms == null) {
            return new HashMap<>();
        } else {
            return perms;
        }
    }

    /**
     * Cette fonction ajoute une permission sur un serveur précis avec une perm précise
     *
     * @param place       est le serveur ou add la perm
     * @param permissions est la permission a ajouté
     */
    public void addPermissions(String place, String permissions) {
        Map<String, List<String>> perm = getPermissions();
        if (perm.keySet().contains(place)) {
            perm.forEach((key, value) -> {
                String placeKey = key;
                List<String> permValue = value;
                permValue.add(permissions);
                perm.replace(placeKey, permValue);
            });
        } else {
            List<String> perms = new ArrayList<>();
            perms.add(permissions);
            perm.put(place, perms);
        }
        setPermissions(perm);
    }

    /**
     * Cette fonction ajoute une permission sur un serveur précis avec une liste de permission
     *
     * @param place       est le serveur ou add la perm
     * @param permissions est une liste de permission a ajouté
     */
    public void addPermissions(String place, List<String> permissions) {
        Map<String, List<String>> perm = getPermissions();
        if (perm.keySet().contains(place)) {
            perm.forEach((key, value) -> {
                String placeKey = key;
                List<String> permValue = value;
                permValue.addAll(permissions);
                perm.replace(placeKey, permValue);
            });
        } else {
            List<String> perms = new ArrayList<>(permissions);
            perm.put(place, perms);
        }
        setPermissions(perm);
    }

    /**
     * Cette fonction retire une permission sur un serveur précis avec une perm précise
     *
     * @param place       est le serveur ou retirer la perm
     * @param permissions est la permission a ajouté
     */
    public void removePermission(String place, String permissions) {
        Map<String, List<String>> perm = getPermissions();
        if (perm.keySet().contains(place)) {
            perm.forEach((key, value) -> {
                String placeKey = key;
                List<String> permValue = value;
                permValue.remove(permissions);
                perm.replace(placeKey, permValue);
            });
        }
        setPermissions(perm);
    }

    /**
     * Cette fonction retire une permission sur un serveur précis avec une list de perm
     *
     * @param place       est le serveur ou retirer la perm
     * @param permissions est la liste permission a retiré
     */
    public void removePermission(String place, List<String> permissions) {
        Map<String, List<String>> perm = getPermissions();
        if (perm.keySet().contains(place)) {
            perm.forEach((key, value) -> {
                String placeKey = key;
                List<String> permValue = value;
                permValue.addAll(permissions);
                perm.replace(placeKey, permValue);
            });
        } else {
            List<String> perms = new ArrayList<>(permissions);
            perm.put(place, perms);
        }
        setPermissions(perm);
    }


    /**
     * @param place       est le serveur ou la permission est
     * @param permissions est la permission à check
     * @return si le joueur a la permission choisie sur le serveur définie.
     */
    public boolean hasPermissions(String place, String permissions) {
        Map<String, List<String>> perm = getPermissions();
        if (perm.keySet().contains(place)) {
            return perm.get(place).stream().anyMatch(permList -> permList.contains(permissions));
        } else {
            return false;
        }
    }

    /**
     * Cette fonction transcode les permissions en json pour être sauvegarder dans la base de donnée
     *
     * @param permissions est la map défini par Map<Serveur de la perm, List<Listes des permissions a ajouté>>
     */
    public void setPermissions(Map<String, List<String>> permissions) {
        refreshData();
        rankBean.setPermissionsJson(new Gson().toJson(permissions));
        updateData();
    }

    public void setRankID(int rankId) {
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

    public boolean refreshData() {
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
