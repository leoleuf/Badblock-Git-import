package fr.badblock.api.data.player;

import java.util.Date;

public class PlayerBean {


    private String playerName;
    private String uuid;
    private String nickName;
    private int coins;
    private Date lastLogin;
    private Date firstLogin;
    private String ip;
    private int rankId;
    private String permissionsJson;
    private boolean online;
    private String normalName;

    public PlayerBean(String playerName, String uuid, String nickName, int coins, Date lastLogin, Date firstLogin, String ip, int rankId, String permissionsJson, boolean online, String normalName) {
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
        this.normalName = normalName;
    }

    public String getUuid() {
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

    public Date getLastLogin() {
        return this.lastLogin;
    }

    public Date getFirstLogin() {
        return this.firstLogin;
    }

    public String getIp() {
        return this.ip;
    }

    public int getRankId() {
        return this.rankId;
    }

    public void setUuid(String uuid) {
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

    void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public void setFirstLogin(Date firstLogin) {
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

    public String getNormalName() {
        return normalName;
    }

    public void setNormalName(String normalName) {
        this.normalName = normalName;
    }
}
