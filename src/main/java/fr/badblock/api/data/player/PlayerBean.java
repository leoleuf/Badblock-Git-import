package fr.badblock.api.data.player;

import java.sql.Timestamp;
import java.util.UUID;

public class PlayerBean {


    private String playerName;
    private UUID uuid;
    private String nickName;
    private int coins;
    private Timestamp lastLogin;
    private Timestamp firstLogin;
    private String ip;
    private int rankId;
    private String permissionsJson;
    private boolean online;

    public PlayerBean(String playerName, UUID uuid, String nickName, int coins, Timestamp lastLogin, Timestamp firstLogin, String ip, int rankId, String permissionsJson, boolean online) {
        this.playerName = playerName;
        this.uuid = uuid;
        this.nickName = nickName;
        this.coins = coins;
        this.lastLogin = lastLogin;
        this.firstLogin = firstLogin;
        this.ip = ip;
        this.rankId = rankId;
        this.permissionsJson = permissionsJson;
        this.online = online;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public String getNickName() {
        return this.nickName;
    }

    public int getCoins() {
        return this.coins;
    }

    public Timestamp getLastLogin() {
        return this.lastLogin;
    }

    public Timestamp getFirstLogin() {
        return this.firstLogin;
    }

    public String getIp() {
        return this.ip;
    }

    public int getRankId() {
        return this.rankId;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setPlayerName(String name) {
        this.playerName = name;
    }

    void setNickName(String nickName) {
        this.nickName = nickName;
    }

    void setCoins(int coins) {
        this.coins = coins;
    }

    void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }

    public void setFirstLogin(Timestamp firstLogin) {
        this.firstLogin = firstLogin;
    }

    void setIp(String ip) {
        this.ip = ip;
    }

    void setRankId(int groupId) {
        this.rankId = groupId;
    }


    public String getPermissionsJson() {
        return permissionsJson;
    }

    public void setPermissionsJson(String permissionsJson) {
        this.permissionsJson = permissionsJson;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
}
