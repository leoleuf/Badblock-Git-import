package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class WorldGenFire extends WorldGenerator {

    public WorldGenFire() {}

    @Override
	public boolean generate(World world, Random random, BlockPosition blockposition) {
        for (int i = 0; i < 64; ++i) {
            BlockPosition blockposition1 = blockposition.a(random.nextInt(8) - random.nextInt(8), random.nextInt(4) - random.nextInt(4), random.nextInt(8) - random.nextInt(8));

            if (world.isEmpty(blockposition1) && world.getType(blockposition1.down()).getBlock() == Blocks.NETHERRACK) {
                world.setTypeAndData(blockposition1, Blocks.FIRE.getBlockData(), 2);
            }
        }

        return true;
    }
}
