package net.minecraft.server.v1_8_R3;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;

public class PacketPlayOutStatistic implements Packet<PacketListenerPlayOut> {

    private Map<Statistic, Integer> a;

    public PacketPlayOutStatistic() {}

    public PacketPlayOutStatistic(Map<Statistic, Integer> map) {
        this.a = map;
    }

    @Override
	public void a(PacketListenerPlayOut packetlistenerplayout) {
        packetlistenerplayout.a(this);
    }

    @Override
	public void a(PacketDataSerializer packetdataserializer) throws IOException {
        int i = packetdataserializer.e();

        this.a = Maps.newHashMap();

        for (int j = 0; j < i; ++j) {
            Statistic statistic = StatisticList.getStatistic(packetdataserializer.c(32767));
            int k = packetdataserializer.e();

            if (statistic != null) {
                this.a.put(statistic, Integer.valueOf(k));
            }
        }

    }

    @Override
	public void b(PacketDataSerializer packetdataserializer) throws IOException {
        packetdataserializer.b(this.a.size());
        Iterator iterator = this.a.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            packetdataserializer.a(((Statistic) entry.getKey()).name);
            packetdataserializer.b(((Integer) entry.getValue()).intValue());
        }

    }

}