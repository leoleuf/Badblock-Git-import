package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class BiomeDesert extends BiomeBase {

    public BiomeDesert(int i) {
        super(i);
        this.au.clear();
        this.ak = Blocks.SAND.getBlockData();
        this.al = Blocks.SAND.getBlockData();
        this.decorator.A = -999;
        this.decorator.D = 2;
        this.decorator.F = 50;
        this.decorator.G = 10;
        this.au.clear();
        this.decorator.mushroomCount = 8;
    }

    @Override
	public void a(World world, Random random, BlockPosition blockposition) {
        super.a(world, random, blockposition);
        if (random.nextInt(1000) == 0) {
            int i = random.nextInt(16) + 8;
            int j = random.nextInt(16) + 8;
            BlockPosition blockposition1 = world.getHighestBlockYAt(blockposition.a(i, 0, j)).up();

            (new WorldGenDesertWell()).generate(world, random, blockposition1);
        }

    }
}
