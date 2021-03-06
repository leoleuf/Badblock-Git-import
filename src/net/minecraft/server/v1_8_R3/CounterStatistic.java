package net.minecraft.server.v1_8_R3;

public class CounterStatistic extends Statistic {

    public CounterStatistic(String s, IChatBaseComponent ichatbasecomponent, Counter counter) {
        super(s, ichatbasecomponent, counter);
    }

    public CounterStatistic(String s, IChatBaseComponent ichatbasecomponent) {
        super(s, ichatbasecomponent);
    }

    @Override
	public Statistic h() {
        super.h();
        StatisticList.c.add(this);
        return this;
    }
}
