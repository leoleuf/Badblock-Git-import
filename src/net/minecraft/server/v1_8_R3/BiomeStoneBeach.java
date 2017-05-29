package net.minecraft.server.v1_8_R3;

public class BiomeStoneBeach extends BiomeBase {

    public BiomeStoneBeach(int i) {
        super(i);
        this.au.clear();
        this.ak = Blocks.STONE.getBlockData();
        this.al = Blocks.STONE.getBlockData();
        this.decorator.A = -999;
        this.decorator.D = 0;
        this.decorator.F = 0;
        this.decorator.G = 0;
    }
}
