package fr.badblock.api.database;

import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import fr.badblock.api.BadBlockAPI;
import fr.badblock.api.data.rank.RankBean;

import java.util.List;

public class RankDataManager {

    private DBCollection ranks;

    public RankDataManager() {
        this.ranks = BadBlockAPI.getPluginInstance().getMongoService().db().getCollection("rank");
    }

    public RankBean getRank(long rankId, RankBean rankBean) {
        try{
            DBObject dbObject = new BasicDBObject("rankId", rankId);
            DBObject found = ranks.findOne(dbObject);
            if(found != null){
                String rankName = (String) found.get("rankName");
                int rankPower = (int) found.get("rankPower");
                String rankTag = (String) found.get("rankTag");
                String rankPrefix = (String) found.get("rankPrefix");
                String rankSuffix = (String) found.get("rankSuffix");
                JsonObject jsonObject = new JsonObject();
                jsonObject.add("s", "s");
                List<JsonObject> rankPermissions;
            }else{
                this.createRank(rankBean);
                return this.getRank(rankId, rankBean);
            }
        }
    }

    public void createRank(RankBean rankBean) {
    }

    public void updateRank(RankBean rankBean){

    }
}
