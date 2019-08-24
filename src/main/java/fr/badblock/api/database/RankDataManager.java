package fr.badblock.api.database;

import com.mongodb.*;
import fr.badblock.api.BadBlockAPI;
import fr.badblock.api.data.rank.RankBean;

import java.util.ArrayList;
import java.util.Objects;

public class RankDataManager {

    private DBCollection ranks;
    private String collection = BadBlockAPI.getPluginInstance().getConfig().getString("mongodb.rankmanagercol");

    public RankDataManager() {
        this.ranks = BadBlockAPI.getPluginInstance().getMongoService().db().getCollection(collection);
    }

    /**
     * Get Rank by ID
     **/
    public RankBean getRank(int rankId, RankBean rankBean) {
        try {
            DBObject dbObject = new BasicDBObject("rankId", rankId);
            DBObject found = ranks.findOne(dbObject);
            if (found != null) {
                int rankID = (int) found.get("rankId");
                String rankName = (String) found.get("rankName");
                int rankPower = (int) found.get("rankPower");
                String rankTag = (String) found.get("rankTag");
                String rankPrefix = (String) found.get("rankPrefix");
                String rankSuffix = (String) found.get("rankSuffix");
                String rankPermissions = (String) found.get("rankPermissions");
                boolean rankIsStaff = (boolean) found.get("rankIsStaff");
                rankBean = new RankBean(rankID, rankName, rankPower, rankTag, rankPrefix, rankSuffix, rankPermissions, rankIsStaff);
                return rankBean;
            } else {
                this.createRank(rankBean);
                return this.getRank(rankId, rankBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get Rank List
     **/
    public ArrayList<Integer> getRankList() {
        ArrayList<Integer> rankList = new ArrayList<>();
        DBCursor cursor = ranks.find();
        while (cursor.hasNext()) {
            DBObject dbObject = cursor.next();
            int id = (int) dbObject.get("rankId");
            rankList.add(id);
        }
        return rankList;
    }

    /**
     * Create Rank
     **/
    public void createRank(RankBean rankBean) {
        try {
            int rankID = rankBean.getRankId();
            String rankName = rankBean.getRankName();
            int rankPower = rankBean.getPower();
            String rankTag = rankBean.getTag();
            String rankPrefix = rankBean.getPrefix();
            String rankSuffix = rankBean.getSuffix();
            String rankPermissions = rankBean.getPermissionsJson();
            boolean rankIsStaff = rankBean.isStaff();

            DBObject obj = new BasicDBObject("rankId", rankID);
            obj.put("rankName", rankName);
            obj.put("rankPower", rankPower);
            obj.put("rankTag", rankTag);
            obj.put("rankPrefix", rankPrefix);
            obj.put("rankSuffix", rankSuffix);
            obj.put("rankPermissions", rankPermissions);
            obj.put("rankIsStaff", rankIsStaff);

            ranks.insert(obj);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Update Rank
     **/
    public void updateRank(RankBean rankBean) {
        try {
            int rankID = rankBean.getRankId();
            String rankName = rankBean.getRankName();
            int rankPower = rankBean.getPower();
            String rankTag = rankBean.getTag();
            String rankPrefix = rankBean.getPrefix();
            String rankSuffix = rankBean.getSuffix();
            String rankPermissions = rankBean.getPermissionsJson();
            boolean rankIsStaff = rankBean.isStaff();

            DBObject obj = new BasicDBObject("rankId", rankID);
            DBObject found = ranks.findOne(obj);
            if (found == null) {
                this.createRank(rankBean);
                updateRank(rankBean);
            }
            obj.put("rankName", rankName);
            obj.put("rankPower", rankPower);
            obj.put("rankTag", rankTag);
            obj.put("rankPrefix", rankPrefix);
            obj.put("rankSuffix", rankSuffix);
            obj.put("rankPermissions", rankPermissions);
            obj.put("rankIsStaff", rankIsStaff);

            ranks.update(Objects.requireNonNull(found), obj);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
