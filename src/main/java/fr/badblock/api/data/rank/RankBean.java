package fr.badblock.api.data.rank;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

public class RankBean {

    private long rankId;
    private String rankName;
    private int power;
    private String tag;
    private String prefix;
    private String suffix;
    private String permissionsJson;


    public RankBean(long rankId, String rankName, int power, String tag, String prefix, String suffix, String permissionsJson) {
        this.rankId = rankId;
        this.rankName = rankName;
        this.power = power;
        this.tag = tag;
        this.prefix = prefix;
        this.suffix = suffix;
        this.permissionsJson = permissionsJson;
    }

    public long getRankId() {
        return rankId;
    }

    public void setRankId(long rankId) {
        this.rankId = rankId;
    }

    public String getRankName() {
        return rankName;
    }

    public void setRankName(String rankName) {
        this.rankName = rankName;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }


    public String getPermissionsJson() {
        return permissionsJson;
    }

    public void setPermissionsJson(String permissionsJson) {
        this.permissionsJson = permissionsJson;
    }
}
