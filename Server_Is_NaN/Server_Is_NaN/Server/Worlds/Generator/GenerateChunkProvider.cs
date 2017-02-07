using Server_Is_NaN.Server.Worlds.Generator.GenLayers;
using System;

namespace Server_Is_NaN.Server.Worlds.Generator
{
    public class GenerateChunkProvider : IChunkProvider
    {
        private GenLayer genLayer;

        public GenerateChunkProvider(GenLayer genLayer)
        {
            this.genLayer = genLayer;
        }

        public Chunk GetChunk(int x, int z)
        {
            int[] biomes = genLayer.GetInts(x * 16 - 9, z * 16 - 9, 34, 34);
            
            return null;
        }

        public bool IsChunkLoaded(int x, int z)
        {
            return false;
        }

        public void UnloadChunk(Chunk chunk) { }

        public void UnloadChunks() { }
    }

    public class ChunkGenerator
    {
        private byte[] values = new byte[1 << 16];

        public byte GetBlockAt(int x, int y, int z)
        {
            return values[(x << 12) | (y << 8) | z];
        }

        public void SetBlockAt(int x, int y, int z, byte block)
        {
            values[((x << 12) | (z << 8) | y)] = block;
        }
    }
}
