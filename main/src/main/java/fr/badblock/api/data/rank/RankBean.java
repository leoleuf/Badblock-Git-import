package fr.badblock.api.data.rank;


public class RankBean {

    private int rankId;
    private String rankName;
    private int power;
    private String tagJson;
    private String prefixJson;
    private String suffixJson;
    private String permissionsJson;
    private boolean isStaff;


    public RankBean(int rankId, String rankName, int power, String tagJson, String prefixJson, String suffixJson, String permissionsJson, boolean isStaff) {
        this.rankId = rankId;
        this.rankName = rankName;
        this.power = power;
        this.tagJson = tagJson;
        this.prefixJson = prefixJson;
        this.suffixJson = suffixJson;
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

    public String getTagJson() {
        return tagJson;
    }

    public void setTagJson(String tagJson) {
        this.tagJson = tagJson;
    }

    public String getPrefixJson() {
        return prefixJson;
    }

    public void setPrefixJson(String prefixJson) {
        this.prefixJson = prefixJson;
    }

    public String getSuffixJson() {
        return suffixJson;
    }

    public void setSuffixJson(String suffixJson) {
        this.suffixJson = suffixJson;
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
