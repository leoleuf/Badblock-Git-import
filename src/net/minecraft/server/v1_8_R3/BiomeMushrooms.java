package net.minecraft.server.v1_8_R3;

public class BiomeMushrooms extends BiomeBase {

    public BiomeMushrooms(int i) {
        super(i);
        this.decorator.A = -100;
        this.decorator.B = -100;
        this.decorator.C = -100;
        this.decorator.mushroomCount = 1;
        this.decorator.K = 1;
        this.ak = Blocks.MYCELIUM.getBlockData();
        this.at.clear();
        this.au.clear();
        this.av.clear();
        this.decorator.mushroomCount = 8;
        this.au.add(new BiomeBase.BiomeMeta(EntityMushroomCow.class, 8, 4, 8));
    }
}
