package fr.badblock.api.database;

import com.mongodb.DBCollection;
import fr.badblock.api.BadBlockAPI;
import fr.badblock.api.data.rank.RankBean;

public class RankDataManager {

    private DBCollection ranks;

    public RankDataManager() {
        this.ranks = BadBlockAPI.getPluginInstance().getMongoService().db().getCollection("rank");
    }

    public RankBean getRank(long rankId, RankBean rankBean) {
        try{

        }
    }

    public void createRank(RankBean rankBean) {
    }

    public void updateRank(RankBean rankBean){

    }
}
