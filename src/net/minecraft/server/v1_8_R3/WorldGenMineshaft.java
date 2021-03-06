package net.minecraft.server.v1_8_R3;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class WorldGenMineshaft extends StructureGenerator {

    private double d = 0.004D;

    public WorldGenMineshaft() {}

    @Override
	public String a() {
        return "Mineshaft";
    }

    public WorldGenMineshaft(Map<String, String> map) {
        Iterator iterator = map.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            if (((String) entry.getKey()).equals("chance")) {
                this.d = MathHelper.a((String) entry.getValue(), this.d);
            }
        }

    }

    @Override
	protected boolean a(int i, int j) {
        return this.b.nextDouble() < this.d && this.b.nextInt(80) < Math.max(Math.abs(i), Math.abs(j));
    }

    @Override
	protected StructureStart b(int i, int j) {
        return new WorldGenMineshaftStart(this.c, this.b, i, j);
    }
}
