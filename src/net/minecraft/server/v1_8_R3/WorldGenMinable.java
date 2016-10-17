package net.minecraft.server.v1_8_R3;

import java.util.Random;

import com.google.common.base.Predicate;

public class WorldGenMinable extends WorldGenerator {

    private final IBlockData a;
    private final int b;
    private final Predicate<IBlockData> c;

    public WorldGenMinable(IBlockData iblockdata, int i) {
        this(iblockdata, i, BlockPredicate.a(Blocks.STONE));
    }

    public WorldGenMinable(IBlockData iblockdata, int i, Predicate<IBlockData> predicate) {
        this.a = iblockdata;
        this.b = i;
        this.c = predicate;
    }

    @Override
	public boolean generate(World world, Random random, BlockPosition blockposition) {
        float f = random.nextFloat() * 3.1415927F;
        double d0 = blockposition.getX() + 8 + MathHelper.sin(f) * this.b / 8.0F;
        double d1 = blockposition.getX() + 8 - MathHelper.sin(f) * this.b / 8.0F;
        double d2 = blockposition.getZ() + 8 + MathHelper.cos(f) * this.b / 8.0F;
        double d3 = blockposition.getZ() + 8 - MathHelper.cos(f) * this.b / 8.0F;
        double d4 = blockposition.getY() + random.nextInt(3) - 2;
        double d5 = blockposition.getY() + random.nextInt(3) - 2;

        for (int i = 0; i < this.b; ++i) {
            float f1 = (float) i / (float) this.b;
            double d6 = d0 + (d1 - d0) * f1;
            double d7 = d4 + (d5 - d4) * f1;
            double d8 = d2 + (d3 - d2) * f1;
            double d9 = random.nextDouble() * this.b / 16.0D;
            double d10 = (MathHelper.sin(3.1415927F * f1) + 1.0F) * d9 + 1.0D;
            double d11 = (MathHelper.sin(3.1415927F * f1) + 1.0F) * d9 + 1.0D;
            int j = MathHelper.floor(d6 - d10 / 2.0D);
            int k = MathHelper.floor(d7 - d11 / 2.0D);
            int l = MathHelper.floor(d8 - d10 / 2.0D);
            int i1 = MathHelper.floor(d6 + d10 / 2.0D);
            int j1 = MathHelper.floor(d7 + d11 / 2.0D);
            int k1 = MathHelper.floor(d8 + d10 / 2.0D);

            for (int l1 = j; l1 <= i1; ++l1) {
                double d12 = (l1 + 0.5D - d6) / (d10 / 2.0D);

                if (d12 * d12 < 1.0D) {
                    for (int i2 = k; i2 <= j1; ++i2) {
                        double d13 = (i2 + 0.5D - d7) / (d11 / 2.0D);

                        if (d12 * d12 + d13 * d13 < 1.0D) {
                            for (int j2 = l; j2 <= k1; ++j2) {
                                double d14 = (j2 + 0.5D - d8) / (d10 / 2.0D);

                                if (d12 * d12 + d13 * d13 + d14 * d14 < 1.0D) {
                                    BlockPosition blockposition1 = new BlockPosition(l1, i2, j2);

                                    if (this.c.apply(world.getType(blockposition1))) {
                                        world.setTypeAndData(blockposition1, this.a, 2);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return true;
    }
}
