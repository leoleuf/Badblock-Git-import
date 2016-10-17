package net.minecraft.server.v1_8_R3;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

public class ChunkProviderDebug implements IChunkProvider {

    private static final List<IBlockData> a = Lists.newArrayList();
    private static final int b;
    private static final int c;
    private final World d;

    public ChunkProviderDebug(World world) {
        this.d = world;
    }

    @Override
	public Chunk getOrCreateChunk(int i, int j) {
        ChunkSnapshot chunksnapshot = new ChunkSnapshot();

        int k;

        for (int l = 0; l < 16; ++l) {
            for (int i1 = 0; i1 < 16; ++i1) {
                int j1 = i * 16 + l;

                k = j * 16 + i1;
                chunksnapshot.a(l, 60, i1, Blocks.BARRIER.getBlockData());
                IBlockData iblockdata = b(j1, k);

                if (iblockdata != null) {
                    chunksnapshot.a(l, 70, i1, iblockdata);
                }
            }
        }

        Chunk chunk = new Chunk(this.d, chunksnapshot, i, j);

        chunk.initLighting();
        BiomeBase[] abiomebase = this.d.getWorldChunkManager().getBiomeBlock((BiomeBase[]) null, i * 16, j * 16, 16, 16);
        byte[] abyte = chunk.getBiomeIndex();

        for (k = 0; k < abyte.length; ++k) {
            abyte[k] = (byte) abiomebase[k].id;
        }

        chunk.initLighting();
        return chunk;
    }

    public static IBlockData b(int i, int j) {
        IBlockData iblockdata = null;

        if (i > 0 && j > 0 && i % 2 != 0 && j % 2 != 0) {
            i /= 2;
            j /= 2;
            if (i <= ChunkProviderDebug.b && j <= ChunkProviderDebug.c) {
                int k = MathHelper.a(i * ChunkProviderDebug.b + j);

                if (k < ChunkProviderDebug.a.size()) {
                    iblockdata = ChunkProviderDebug.a.get(k);
                }
            }
        }

        return iblockdata;
    }

    @Override
	public boolean isChunkLoaded(int i, int j) {
        return true;
    }

    @Override
	public void getChunkAt(IChunkProvider ichunkprovider, int i, int j) {}

    @Override
	public boolean a(IChunkProvider ichunkprovider, Chunk chunk, int i, int j) {
        return false;
    }

    @Override
	public boolean saveChunks(boolean flag, IProgressUpdate iprogressupdate) {
        return true;
    }

    @Override
	public void c() {}

    @Override
	public boolean unloadChunks() {
        return false;
    }

    @Override
	public boolean canSave() {
        return true;
    }

    @Override
	public String getName() {
        return "DebugLevelSource";
    }

    @Override
	public List<BiomeBase.BiomeMeta> getMobsFor(EnumCreatureType enumcreaturetype, BlockPosition blockposition) {
        BiomeBase biomebase = this.d.getBiome(blockposition);

        return biomebase.getMobs(enumcreaturetype);
    }

    @Override
	public BlockPosition findNearestMapFeature(World world, String s, BlockPosition blockposition) {
        return null;
    }

    @Override
	public int getLoadedChunks() {
        return 0;
    }

    @Override
	public void recreateStructures(Chunk chunk, int i, int j) {}

    @Override
	public Chunk getChunkAt(BlockPosition blockposition) {
        return this.getOrCreateChunk(blockposition.getX() >> 4, blockposition.getZ() >> 4);
    }

    static {
        Iterator iterator = Block.REGISTRY.iterator();

        while (iterator.hasNext()) {
            Block block = (Block) iterator.next();

            ChunkProviderDebug.a.addAll(block.P().a());
        }

        b = MathHelper.f(MathHelper.c((float) ChunkProviderDebug.a.size()));
        c = MathHelper.f((float) ChunkProviderDebug.a.size() / (float) ChunkProviderDebug.b);
    }
}
