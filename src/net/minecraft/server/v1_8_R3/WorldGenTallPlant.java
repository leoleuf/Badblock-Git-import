package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class WorldGenTallPlant extends WorldGenerator {

    private BlockTallPlant.EnumTallFlowerVariants a;

    public WorldGenTallPlant() {}

    public void a(BlockTallPlant.EnumTallFlowerVariants blocktallplant_enumtallflowervariants) {
        this.a = blocktallplant_enumtallflowervariants;
    }

    @Override
	public boolean generate(World world, Random random, BlockPosition blockposition) {
        boolean flag = false;

        for (int i = 0; i < 64; ++i) {
            BlockPosition blockposition1 = blockposition.a(random.nextInt(8) - random.nextInt(8), random.nextInt(4) - random.nextInt(4), random.nextInt(8) - random.nextInt(8));

            if (world.isEmpty(blockposition1) && (!world.worldProvider.o() || blockposition1.getY() < 254) && Blocks.DOUBLE_PLANT.canPlace(world, blockposition1)) {
                Blocks.DOUBLE_PLANT.a(world, blockposition1, this.a, 2);
                flag = true;
            }
        }

        return flag;
    }
}
