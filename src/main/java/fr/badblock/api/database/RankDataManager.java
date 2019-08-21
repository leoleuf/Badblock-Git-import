package fr.badblock.api.database;

import com.google.gson.JsonArray;
import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import fr.badblock.api.BadBlockAPI;
import fr.badblock.api.data.rank.RankBean;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RankDataManager {

    private DBCollection ranks;

    public RankDataManager() {
        this.ranks = BadBlockAPI.getPluginInstance().getMongoService().db().getCollection("rank");
    }

    public RankBean getRank(long rankId, RankBean rankBean) {
        try {
            DBObject dbObject = new BasicDBObject("rankId", rankId);
            DBObject found = ranks.findOne(dbObject);
            if (found != null) {
                long rankID = (long) found.get("rankdId");
                String rankName = (String) found.get("rankName");
                int rankPower = (int) found.get("rankPower");
                String rankTag = (String) found.get("rankTag");
                String rankPrefix = (String) found.get("rankPrefix");
                String rankSuffix = (String) found.get("rankSuffix");
                JsonArray rankPermissions = (JsonArray) found.get("rankPermissions");
                rankBean = new RankBean(rankID, rankName, rankPower, rankTag, rankPrefix, rankSuffix, rankPermissions);
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

    public ArrayList<Long> getRankList() {
        ArrayList<Long> rankList = new ArrayList<>();
        DBCursor cursor = ranks.find();
        while (cursor.hasNext()) {
            DBObject dbObject = cursor.next();
            long id = (Long) dbObject.get("rankId");
            rankList.add(id);
        }
        return rankList;
    }

    public long getAvaibleRankID() {
        BasicDBObject basicDBObject = new BasicDBObject();
        BasicDBObject basicDBObject1 = new BasicDBObject("_id", "1");
        DBCursor dbCursor = ranks.find(basicDBObject1);
        return dbCursor.length();
    }

    public void createRank(RankBean rankBean) {
        try {
            long rankID = rankBean.getRankId();
            String rankName = rankBean.getRankName();
            int rankPower = rankBean.getPower();
            String rankTag = rankBean.getTag();
            String rankPrefix = rankBean.getPrefix();
            String rankSuffix = rankBean.getSuffix();
            JsonArray rankPermissions = rankBean.getPermissions();

            DBObject obj = new BasicDBObject("rankId", rankID);
            obj.put("rankName", rankName);
            obj.put("rankPower", rankPower);
            obj.put("rankTag", rankTag);
            obj.put("rankPreffix", rankPrefix);
            obj.put("rankSuffix", rankSuffix);
            obj.put("rankPermissions", rankPermissions);

            ranks.insert(obj);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateRank(RankBean rankBean) {
        try {
            long rankID = rankBean.getRankId();
            String rankName = rankBean.getRankName();
            int rankPower = rankBean.getPower();
            String rankTag = rankBean.getTag();
            String rankPrefix = rankBean.getPrefix();
            String rankSuffix = rankBean.getSuffix();
            JsonArray rankPermissions = rankBean.getPermissions();

            DBObject obj = new BasicDBObject("rankId", rankID);
            DBObject found = ranks.findOne(obj);
            if (found == null) {
                this.createRank(rankBean);
                updateRank(rankBean);
            }
            obj.put("rankName", rankName);
            obj.put("rankPower", rankPower);
            obj.put("rankTag", rankTag);
            obj.put("rankPreffix", rankPrefix);
            obj.put("rankSuffix", rankSuffix);
            obj.put("rankPermissions", rankPermissions);

            ranks.update(Objects.requireNonNull(found), obj);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
