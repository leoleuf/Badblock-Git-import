package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class BiomePlains extends BiomeBase {

    protected boolean aD;

    protected BiomePlains(int i) {
        super(i);
        this.a(0.8F, 0.4F);
        this.a(BiomeBase.e);
        this.au.add(new BiomeBase.BiomeMeta(EntityHorse.class, 5, 2, 6));
        this.as.A = -999;
        this.as.B = 4;
        this.as.C = 10;
    }

    @Override
	public BlockFlowers.EnumFlowerVarient a(Random random, BlockPosition blockposition) {
        double d0 = BiomeBase.af.a(blockposition.getX() / 200.0D, blockposition.getZ() / 200.0D);
        int i;

        if (d0 < -0.8D) {
            i = random.nextInt(4);
            switch (i) {
            case 0:
                return BlockFlowers.EnumFlowerVarient.ORANGE_TULIP;

            case 1:
                return BlockFlowers.EnumFlowerVarient.RED_TULIP;

            case 2:
                return BlockFlowers.EnumFlowerVarient.PINK_TULIP;

            case 3:
            default:
                return BlockFlowers.EnumFlowerVarient.WHITE_TULIP;
            }
        } else if (random.nextInt(3) > 0) {
            i = random.nextInt(3);
            return i == 0 ? BlockFlowers.EnumFlowerVarient.POPPY : (i == 1 ? BlockFlowers.EnumFlowerVarient.HOUSTONIA : BlockFlowers.EnumFlowerVarient.OXEYE_DAISY);
        } else {
            return BlockFlowers.EnumFlowerVarient.DANDELION;
        }
    }

    @Override
	public void a(World world, Random random, BlockPosition blockposition) {
        double d0 = BiomeBase.af.a((blockposition.getX() + 8) / 200.0D, (blockposition.getZ() + 8) / 200.0D);
        int i;
        int j;
        int k;
        int l;

        if (d0 < -0.8D) {
            this.as.B = 15;
            this.as.C = 5;
        } else {
            this.as.B = 4;
            this.as.C = 10;
            BiomeBase.ag.a(BlockTallPlant.EnumTallFlowerVariants.GRASS);

            for (i = 0; i < 7; ++i) {
                j = random.nextInt(16) + 8;
                k = random.nextInt(16) + 8;
                l = random.nextInt(world.getHighestBlockYAt(blockposition.a(j, 0, k)).getY() + 32);
                BiomeBase.ag.generate(world, random, blockposition.a(j, l, k));
            }
        }

        if (this.aD) {
            BiomeBase.ag.a(BlockTallPlant.EnumTallFlowerVariants.SUNFLOWER);

            for (i = 0; i < 10; ++i) {
                j = random.nextInt(16) + 8;
                k = random.nextInt(16) + 8;
                l = random.nextInt(world.getHighestBlockYAt(blockposition.a(j, 0, k)).getY() + 32);
                BiomeBase.ag.generate(world, random, blockposition.a(j, l, k));
            }
        }

        super.a(world, random, blockposition);
    }

    @Override
	protected BiomeBase d(int i) {
        BiomePlains biomeplains = new BiomePlains(i);

        biomeplains.a("Sunflower Plains");
        biomeplains.aD = true;
        biomeplains.b(9286496);
        biomeplains.aj = 14273354;
        return biomeplains;
    }
}
