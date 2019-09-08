package fr.badblock.api.data.rank;


public class RankBean {

    private int rankId;
    private String rankName;
    private int power;
    private String tag;
    private String prefix;
    private String suffix;
    private String permissionsJson;
    private boolean isStaff;


    public RankBean(int rankId, String rankName, int power, String tag, String prefix, String suffix, String permissionsJson, boolean isStaff) {
        this.rankId = rankId;
        this.rankName = rankName;
        this.power = power;
        this.tag = tag;
        this.prefix = prefix;
        this.suffix = suffix;
        this.permissionsJson = permissionsJson;
        this.isStaff = isStaff;
    }

    public int getRankId() {
        return rankId;
    }

    public void setRankId(int rankId) {
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

    public boolean isStaff() {
        return isStaff;
    }

    public void setStaff(boolean staff) {
        isStaff = staff;
    }

}
