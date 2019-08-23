package fr.badblock.api.database;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import fr.badblock.api.BadBlockAPI;
import fr.badblock.api.data.player.PlayerBean;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

public class PlayerDataManager {

    private DBCollection players;
    private String collection = BadBlockAPI.getPluginInstance().getConfig().getString("mongodb.datamanagercol");

    public PlayerDataManager() {
        this.players = BadBlockAPI.getPluginInstance().getMongoService().db().getCollection(collection);
    }

    /**
     * Get BeanPlayer and all the data
     **/
    public PlayerBean getPlayer(String playerName, PlayerBean playerBean) {
        try {
            DBObject dbObject = new BasicDBObject("name", playerName);
            DBObject found = players.findOne(dbObject);
            if (found != null) {
                String name = (String) found.get("name");
                UUID uuid = (UUID) found.get("uniqueId");
                String nickName = (String) found.get("nickname");
                int coins = (int) found.get("coins");
                Timestamp lastLogin = (Timestamp) found.get("lastLogin");
                Timestamp firstLogin = (Timestamp) found.get("firstLogin");
                String ip = (String) found.get("lastIp");
                int rankId = (int) found.get("rankId");
                String permissionJson = (String) found.get("permissions");
                boolean online = (boolean) found.get("online");
                return new PlayerBean(name, uuid, nickName, coins, lastLogin, firstLogin, ip, rankId, permissionJson, online);
            } else {
                this.createPlayer(new PlayerBean(playerName.toLowerCase(),
                        null,
                        playerName.toLowerCase(),
                        0,
                        new Timestamp(System.currentTimeMillis()),
                        new Timestamp(System.currentTimeMillis()),
                        null,
                        1,
                        null,
                        false));
                return this.getPlayer(playerName, playerBean);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    /**
     * Create player and data
     **/
    private void createPlayer(PlayerBean playerBean) {
        try {
            System.out.println(playerBean.getPlayerName());
            String name = playerBean.getPlayerName();
            UUID uuid = playerBean.getUuid();
            String nickName = playerBean.getNickName();
            int coins = playerBean.getCoins();
            Timestamp lastLogin = playerBean.getLastLogin();
            Timestamp firstLogin = playerBean.getFirstLogin();
            String ip = playerBean.getIp();
            int rankId = playerBean.getRankId();

            DBObject obj = new BasicDBObject("name", name);
            obj.put("name", name);
            obj.put("uniqueId", uuid);
            obj.put("nickname", nickName);
            obj.put("coins", coins);
            obj.put("lastLogin", lastLogin);
            obj.put("firstLogin", firstLogin);
            obj.put("lastIp", ip);
            obj.put("rankId", rankId);
            obj.put("permissions", playerBean.getPermissionsJson());
            obj.put("online", playerBean.isOnline());

            players.insert(obj);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Update player and his data
     **/
    public void updatePlayer(PlayerBean playerBean) {
        try {
            String name = playerBean.getPlayerName();
            UUID uuid = playerBean.getUuid();
            String nickName = playerBean.getNickName();
            int coins = playerBean.getCoins();
            Timestamp lastLogin = playerBean.getLastLogin();
            Timestamp firstLogin = playerBean.getFirstLogin();
            String ip = playerBean.getIp();
            int rankId = playerBean.getRankId();

            DBObject obj = new BasicDBObject("name", name);
            DBObject found = players.findOne(obj);
            if (found == null) {
                this.createPlayer(playerBean);
                updatePlayer(playerBean);
            }
            obj.put("name", name);
            obj.put("uniqueId", uuid);
            obj.put("nickname", nickName);
            obj.put("coins", coins);
            obj.put("lastLogin", lastLogin);
            obj.put("firstLogin", firstLogin);
            obj.put("lastIp", ip);
            obj.put("rankId", rankId);
            obj.put("permissions", playerBean.getPermissionsJson());
            obj.put("online", playerBean.isOnline());
            players.update(Objects.requireNonNull(found), obj);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
