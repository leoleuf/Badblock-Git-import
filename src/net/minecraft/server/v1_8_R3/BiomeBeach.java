package net.minecraft.server.v1_8_R3;

public class BiomeBeach extends BiomeBase {

    public BiomeBeach(int i) {
        super(i);
        this.au.clear();
        this.ak = Blocks.SAND.getBlockData();
        this.al = Blocks.SAND.getBlockData();
        this.decorator.A = -999;
        this.decorator.D = 0;
        this.decorator.F = 0;
        this.decorator.G = 0;
        this.decorator.mushroomCount = 8;
    }
}
