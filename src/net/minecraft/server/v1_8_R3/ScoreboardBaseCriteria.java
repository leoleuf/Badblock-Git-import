package net.minecraft.server.v1_8_R3;

import java.util.List;

public class ScoreboardBaseCriteria implements IScoreboardCriteria {

    private final String j;

    public ScoreboardBaseCriteria(String s) {
        this.j = s;
        IScoreboardCriteria.criteria.put(s, this);
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
