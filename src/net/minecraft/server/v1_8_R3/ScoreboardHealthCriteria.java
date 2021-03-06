package net.minecraft.server.v1_8_R3;

import java.util.Iterator;
import java.util.List;

public class ScoreboardHealthCriteria extends ScoreboardBaseCriteria {

    public ScoreboardHealthCriteria(String s) {
        super(s);
    }

    @Override
	public int getScoreModifier(List<EntityHuman> list) {
        float f = 0.0F;

        EntityHuman entityhuman;

        for (Iterator iterator = list.iterator(); iterator.hasNext(); f += entityhuman.getHealth() + entityhuman.getAbsorptionHearts()) {
            entityhuman = (EntityHuman) iterator.next();
        }

        if (list.size() > 0) {
            f /= list.size();
        }

        return MathHelper.f(f);
    }

    @Override
	public boolean isReadOnly() {
        return true;
    }

    @Override
	public IScoreboardCriteria.EnumScoreboardHealthDisplay c() {
        return IScoreboardCriteria.EnumScoreboardHealthDisplay.HEARTS;
    }
}
