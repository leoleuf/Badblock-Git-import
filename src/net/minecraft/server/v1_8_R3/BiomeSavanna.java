package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class BiomeSavanna extends BiomeBase {

    private static final WorldGenAcaciaTree aD = new WorldGenAcaciaTree(false);

    protected BiomeSavanna(int i) {
        super(i);
        this.au.add(new BiomeBase.BiomeMeta(EntityHorse.class, 1, 2, 6));
        this.decorator.A = 1;
        this.decorator.B = 4;
        this.decorator.C = 20;
        this.decorator.mushroomCount = 8;
    }

    @Override
	public WorldGenTreeAbstract a(Random random) {
        return random.nextInt(5) > 0 ? BiomeSavanna.aD : this.aA;
    }

    @Override
	protected BiomeBase d(int i) {
        BiomeSavanna.BiomeSavannaSub biomesavanna_biomesavannasub = new BiomeSavanna.BiomeSavannaSub(i, this);

        biomesavanna_biomesavannasub.temperature = (this.temperature + 1.0F) * 0.5F;
        biomesavanna_biomesavannasub.an = this.an * 0.5F + 0.3F;
        biomesavanna_biomesavannasub.ao = this.ao * 0.5F + 1.2F;
        return biomesavanna_biomesavannasub;
    }

    @Override
	public void a(World world, Random random, BlockPosition blockposition) {
        BiomeBase.ag.a(BlockTallPlant.EnumTallFlowerVariants.GRASS);

        for (int i = 0; i < 7; ++i) {
            int j = random.nextInt(16) + 8;
            int k = random.nextInt(16) + 8;
            int l = random.nextInt(world.getHighestBlockYAt(blockposition.a(j, 0, k)).getY() + 32);

            BiomeBase.ag.generate(world, random, blockposition.a(j, l, k));
        }

        super.a(world, random, blockposition);
    }

    public static class BiomeSavannaSub extends BiomeBaseSub {

        public BiomeSavannaSub(int i, BiomeBase biomebase) {
            super(i, biomebase);
            this.decorator.A = 2;
            this.decorator.B = 2;
            this.decorator.C = 5;
        }

        @Override
		public void a(World world, Random random, ChunkSnapshot chunksnapshot, int i, int j, double d0) {
            this.ak = Blocks.GRASS.getBlockData();
            this.al = Blocks.DIRT.getBlockData();
            if (d0 > 1.75D) {
                this.ak = Blocks.STONE.getBlockData();
                this.al = Blocks.STONE.getBlockData();
            } else if (d0 > -0.5D) {
                this.ak = Blocks.DIRT.getBlockData().set(BlockDirt.VARIANT, BlockDirt.EnumDirtVariant.COARSE_DIRT);
            }

            this.b(world, random, chunksnapshot, i, j, d0);
        }

        @Override
		public void a(World world, Random random, BlockPosition blockposition) {
            this.decorator.a(world, random, this, blockposition);
        }
    }
}
