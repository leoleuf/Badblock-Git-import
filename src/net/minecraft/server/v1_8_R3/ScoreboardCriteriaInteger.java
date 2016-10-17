package net.minecraft.server.v1_8_R3;

import java.util.List;

public class ScoreboardCriteriaInteger implements IScoreboardCriteria {

    private final String j;

    public ScoreboardCriteriaInteger(String s, EnumChatFormat enumchatformat) {
        this.j = s + enumchatformat.e();
        IScoreboardCriteria.criteria.put(this.j, this);
    }

    @Override
	public String getName() {
        return this.j;
    }

    @Override
	public int getScoreModifier(List<EntityHuman> list) {
        return 0;
    }

    @Override
	public boolean isReadOnly() {
        return false;
    }

    @Override
	public IScoreboardCriteria.EnumScoreboardHealthDisplay c() {
        return IScoreboardCriteria.EnumScoreboardHealthDisplay.INTEGER;
    }
}
