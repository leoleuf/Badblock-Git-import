package net.minecraft.server.v1_8_R3;

public class WorldProviderHell extends WorldProvider {

    public WorldProviderHell() {}

    @Override
	public void b() {
        this.c = new WorldChunkManagerHell(BiomeBase.HELL, 0.0F);
        this.d = true;
        this.e = true;
        this.dimension = -1;
    }

    @Override
	protected void a() {
        float f = 0.1F;

        for (int i = 0; i <= 15; ++i) {
            float f1 = 1.0F - i / 15.0F;

            this.f[i] = (1.0F - f1) / (f1 * 3.0F + 1.0F) * (1.0F - f) + f;
        }

    }

    @Override
	public IChunkProvider getChunkProvider() {
        return new ChunkProviderHell(this.b, this.b.getWorldData().shouldGenerateMapFeatures(), this.b.getSeed());
    }

    @Override
	public boolean d() {
        return false;
    }

    @Override
	public boolean canSpawn(int i, int j) {
        return false;
    }

    @Override
	public float a(long i, float f) {
        return 0.5F;
    }

    @Override
	public boolean e() {
        return false;
    }

    @Override
	public String getName() {
        return "Nether";
    }

    @Override
	public String getSuffix() {
        return "_nether";
    }

    @Override
	public WorldBorder getWorldBorder() {
        return new WorldBorder() {
            @Override
			public double getCenterX() {
                return super.getCenterX() / 8.0D;
            }

            @Override
			public double getCenterZ() {
                return super.getCenterZ() / 8.0D;
            }
        };
    }
}
