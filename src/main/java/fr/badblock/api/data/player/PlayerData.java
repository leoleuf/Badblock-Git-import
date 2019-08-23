package fr.badblock.api.data.player;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fr.badblock.api.BadBlockAPI;
import fr.badblock.api.data.rank.RankManager;
import fr.badblock.api.database.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PlayerData {

    private BadBlockAPI badBlockAPI;

    private long lastRefresh;
    private String playerName;
    private boolean loaded;
    private PlayerBean playerBean;
    private PlayerDataManager playerDataManager;
    private PermissionAttachment attachment;

    protected PlayerData(String playerName, BadBlockAPI badBlockAPI) {
        this.playerName = playerName.toLowerCase();
        this.badBlockAPI = badBlockAPI;
        this.playerDataManager = new PlayerDataManager();
        this.playerBean = new PlayerBean(playerName.toLowerCase(),
                null,
                null,
                0,
                new Date(System.currentTimeMillis()),
                new Date(System.currentTimeMillis()),
                null,
                1,
                null,
                false);
        refreshData();
    }

    /**
     * @return l'objet Player du joueur
     */
    public Player getPlayer() {
        refreshIfNeeded();
        return Bukkit.getPlayer(playerName);
    }

    /**
     * @return l'uuid du joueur
     */
    public String getPlayerID() {
        refreshIfNeeded();
        return playerBean.getUuid();
    }

    /**
     * @return le nickname du joueur si il en a un
     * sinon il return son vrai pseudo
     */
    public String getDisplayName() {
        return hasNickname() ? getCustomName() : getEffectiveName();
    }

    public void setName(String name){
        refreshData();
        playerBean.setPlayerName(name);
        updateData();
    }

    public void setPlayerID(String uuid){
        refreshData();
        playerBean.setUuid(uuid);
        updateData();
    }

    /**
     * @return le vrai pseudo du joueur
     */
    public String getEffectiveName() {
        refreshIfNeeded();
        return playerBean.getPlayerName();
    }

    /**
     * @return le nickname du joueur
     */
    public String getCustomName() {
        refreshIfNeeded();
        return playerBean.getNickName();
    }

    /**
     * @param nickName permet de mettre un pseudo custom au joueur
     */
    public void setNickName(String nickName) {
        refreshData();
        playerBean.setNickName(nickName);
        updateData();
    }

    /**
     * @return le résultat de si le joueur est nickname ou non
     */
    public boolean hasNickname() {
        refreshIfNeeded();
        return this.getCustomName() != null && !this.getCustomName().equals("null");
    }

    /**
     * @return la date de la première connexion du joueur
     */
    public Date getFirstLogin() {
        refreshIfNeeded();
        return playerBean.getFirstLogin();
    }

    /**
     * @return la date de la dernière connexion du joueur
     */
    public Date getLastLogin() {
        refreshIfNeeded();
        return playerBean.getLastLogin();
    }

    public boolean isOnline(){
        refreshIfNeeded();
        return playerBean.isOnline();
    }

    public void setOnline(boolean stats){
        refreshData();
        playerBean.setOnline(stats);
        updateData();
    }

    /**
     * Permet de mettre la dernière connexion du joueur sur la date actuel
     */
    public void setLastLogin() {
        refreshData();
        playerBean.setLastLogin(new Date(System.currentTimeMillis()));
        updateData();
    }

    /**
     * @return l'adresse IP du joueur
     */
    public String getIP() {
        refreshIfNeeded();
        return playerBean.getIp();
    }

    /**
     * @param IP permet de définir l'adresse IP du joueur
     */
    public void setIP(String IP) {
        refreshData();
        playerBean.setIp(IP);
        updateData();
    }

    /**
     * @return l'ID du rank du joueur qui permet de get le Rank
     */
    public int getRankID() {
        refreshIfNeeded();
        return playerBean.getRankId();
    }

    /**
     * @param rankID set le rank du joueur en fonction de l'id du rank voulu
     */
    public void setRankID(int rankID) {
        refreshData();
        playerBean.setRankId(rankID);
        updateData();
    }

    /**
     * @param amount permet d'ajouter des coins au joueur
     */
    public void giveCoins(long amount) {
        refreshData();
        int result = (int) (playerBean.getCoins() + amount);
        playerBean.setCoins(result);
        updateData();
    }

    /**
     * @param amount permet de retirer des coins au joueur
     */
    public void removeCoins(long amount) {
        giveCoins(-amount);
    }

    /**
     * @param amount permet de définir les coins du joueur
     */
    public void setCoins(long amount) {
        refreshData();
        playerBean.setCoins((int) amount);
        updateData();
    }

    /**
     * @return le nombre de coins que le joueur possède
     */
    public long getCoins() {
        refreshIfNeeded();
        return playerBean.getCoins();
    }

    public String getPermissionsJson() {
        refreshIfNeeded();
        return playerBean.getPermissionsJson();
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
        return getPermissions().contains(permissions) || getPermissions().contains("*");
    }

    public void setBukkitPermissions() {
        attachment = getPlayer().addAttachment(badBlockAPI);
        RankManager rankManager = badBlockAPI.getRankManager();
        if (rankManager.getRankData(getRankID()).getPermissions() != null) {
            rankManager.getRankData(getRankID()).getPermissions().forEach(perm -> {
                attachment.setPermission(perm, true);
            });
        }
        getPermissions().forEach(perm -> {
            attachment.setPermission(perm, true);
        });
    }

    public void removePermission(String perm) {
        List<String> perms = getPermissions();
        perms.remove(perm);
        setPermissions(perms);
        attachment.unsetPermission(perm);
    }

    public void removePermission(List<String> perm) {
        List<String> perms = getPermissions();
        perms.forEach(perm::remove);
        setPermissions(perms);
        perm.forEach(permission -> attachment.unsetPermission(permission));
    }

    public void setPermissions(List<String> permissions) {
        refreshData();
        playerBean.setPermissionsJson(new Gson().toJson(permissions));
        setBukkitPermissions();
        updateData();
    }

    /**
     * @return si le cache du joueur a bien été mis à jour
     */
    boolean refreshData() {
        lastRefresh = System.currentTimeMillis();
        try {
            playerBean = playerDataManager.getPlayer(playerName, playerBean);
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
        if (playerBean != null && loaded) {
            try {
                playerDataManager.updatePlayer(playerBean);
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

    /**
     * @return le dernier refresh en date
     */
    public long getLastRefresh() {
        return lastRefresh;
    }
}
