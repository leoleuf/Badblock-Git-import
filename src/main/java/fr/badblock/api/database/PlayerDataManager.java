package fr.badblock.api.database;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import fr.badblock.api.BadBlockAPI;
import fr.badblock.api.player.PlayerBean;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

public class PlayerDataManager {

    private DBCollection players;
    private String collection = BadBlockAPI.getPluginInstance().getConfig().getString("mongodb.pdatamanagercol");

    public PlayerDataManager() {
        this.players = BadBlockAPI.getPluginInstance().getMongoService().db().getCollection(collection);
    }

    public PlayerBean getPlayer(String playerName, PlayerBean playerBean) {
        try {
            DBObject dbObject = new BasicDBObject("playerName", playerName);
            DBObject found = players.findOne(dbObject);
            if (found != null) {
                String name = (String) found.get("playerName");
                UUID uuid = (UUID) found.get("playerUUID");
                String nickName = (String) found.get("nickName");
                int coins = (int) found.get("coins");
                Timestamp lastLogin = (Timestamp) found.get("lastLogin");
                Timestamp firstLogin = (Timestamp) found.get("firstLogin");
                String ip = (String) found.get("ip");
                long rankId = (Long) found.get("rankId");
                playerBean = new PlayerBean(name, uuid, nickName, coins, lastLogin, firstLogin, ip, rankId);
                return playerBean;
            } else {
                this.createPlayer(playerBean);
                return this.getPlayer(playerName, playerBean);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    private void createPlayer(PlayerBean playerBean) {
        try {
            String name = playerBean.getPlayerName();
            UUID uuid = playerBean.getUuid();
            String nickName = playerBean.getNickName();
            int coins = playerBean.getCoins();
            Timestamp lastLogin = playerBean.getLastLogin();
            Timestamp firstLogin = playerBean.getFirstLogin();
            String ip = playerBean.getIp();
            long rankId = playerBean.getRankId();

            DBObject obj = new BasicDBObject("playerName", name);
            obj.put("playerUUID", uuid);
            obj.put("nickName", nickName);
            obj.put("coins", coins);
            obj.put("lastLogin", lastLogin);
            obj.put("firstLogin", firstLogin);
            obj.put("ip", ip);
            obj.put("rankId", rankId);

            players.insert(obj);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updatePlayer(PlayerBean playerBean) {
        try {
            String name = playerBean.getPlayerName();
            UUID uuid = playerBean.getUuid();
            String nickName = playerBean.getNickName();
            int coins = playerBean.getCoins();
            Timestamp lastLogin = playerBean.getLastLogin();
            Timestamp firstLogin = playerBean.getFirstLogin();
            String ip = playerBean.getIp();
            long rankId = playerBean.getRankId();

            DBObject obj = new BasicDBObject("playerName", name);
            DBObject found = players.findOne(obj);
            if (found == null) {
                this.createPlayer(playerBean);
                updatePlayer(playerBean);
            }
            obj.put("playerUUID", uuid);
            obj.put("nickName", nickName);
            obj.put("coins", coins);
            obj.put("lastLogin", lastLogin);
            obj.put("firstLogin", firstLogin);
            obj.put("ip", ip);
            obj.put("rankId", rankId);
            players.update(Objects.requireNonNull(found), obj);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
