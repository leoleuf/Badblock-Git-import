package fr.badblock.api.data.player;

import fr.badblock.api.BadBlockAPI;
import fr.badblock.api.database.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

public class PlayerData {

    private BadBlockAPI badBlockAPI;

    private long lastRefresh;
    private String playerName;
    private boolean loaded;
    private PlayerBean playerBean;
    private PlayerDataManager playerDataManager;

    PlayerData(String playerName, BadBlockAPI badBlockAPI) {
        this.playerName = playerName;
        this.badBlockAPI = badBlockAPI;
        this.playerDataManager = new PlayerDataManager();
        playerBean = new PlayerBean(playerName,
                null,
                null,
                0,
                null,
                null,
                null,
                0);
        refreshData();
    }

    /**
     * @return l'objet Player du joueur
     */
    public Player getPlayer() {
        refreshIfNeeded();
        return (Player) Bukkit.getOfflinePlayer(playerName);
    }

    /**
     * @return l'uuid du joueur
     */
    public UUID getPlayerID() {
        refreshIfNeeded();
        return getPlayer().getUniqueId();
    }

    /**
     * @return le nickname du joueur si il en a un
     * sinon il return son vrai pseudo
     */
    public String getDisplayName() {
        return hasNickname() ? getCustomName() : getEffectiveName();
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

    /**
     * Permet de mettre la dernière connexion du joueur sur la date actuel
     */
    public void setLastLogin() {
        refreshData();
        playerBean.setLastLogin(new Timestamp(System.currentTimeMillis()));
        updateData();
    }

    /**
     * @return l'adresse IP du joueur
     */
    public String getIP(){
        refreshIfNeeded();
        return playerBean.getIp();
    }

    /**
     * @param IP permet de définir l'adresse IP du joueur
     */
    public void setIP(String IP){
        refreshData();
        playerBean.setIp(IP);
        updateData();
    }

    /**
     * @return l'ID du rank du joueur qui permet de get le Rank
     */
    public Long getRankID(){
        refreshIfNeeded();
        return playerBean.getRankId();
    }

    /**
     * @param rankID set le rank du joueur en fonction de l'id du rank voulu
     */
    public void setRankID(long rankID){
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
