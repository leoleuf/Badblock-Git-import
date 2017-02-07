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

            
        }

        public bool IsChunkLoaded(int x, int z)
        {
            return false;
        }

        public void UnloadChunk(Chunk chunk) { }

        public void UnloadChunks() { }
    }
}
