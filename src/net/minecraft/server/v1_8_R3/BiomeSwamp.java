package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class BiomeSwamp extends BiomeBase {

    protected BiomeSwamp(int i) {
        super(i);
        this.decorator.A = 2;
        this.decorator.B = 1;
        this.decorator.D = 1;
        this.decorator.mushroomCount = 8;
        this.decorator.F = 10;
        this.decorator.J = 1;
        this.decorator.z = 4;
        this.decorator.I = 0;
        this.decorator.H = 0;
        this.decorator.C = 5;
        this.ar = 14745518;
        this.at.add(new BiomeBase.BiomeMeta(EntitySlime.class, 1, 1, 1));
    }

    @Override
	public WorldGenTreeAbstract a(Random random) {
        return this.aC;
    }

    @Override
	public BlockFlowers.EnumFlowerVarient a(Random random, BlockPosition blockposition) {
        return BlockFlowers.EnumFlowerVarient.BLUE_ORCHID;
    }

    @Override
	public void a(World world, Random random, ChunkSnapshot chunksnapshot, int i, int j, double d0) {
        double d1 = BiomeBase.af.a(i * 0.25D, j * 0.25D);

        if (d1 > 0.0D) {
            int k = i & 15;
            int l = j & 15;

            for (int i1 = 255; i1 >= 0; --i1) {
                if (chunksnapshot.a(l, i1, k).getBlock().getMaterial() != Material.AIR) {
                    if (i1 == 62 && chunksnapshot.a(l, i1, k).getBlock() != Blocks.WATER) {
                        chunksnapshot.a(l, i1, k, Blocks.WATER.getBlockData());
                        if (d1 < 0.12D) {
                            chunksnapshot.a(l, i1 + 1, k, Blocks.WATERLILY.getBlockData());
                        }
                    }
                    break;
                }
            }
        }

        this.b(world, random, chunksnapshot, i, j, d0);
    }
}
