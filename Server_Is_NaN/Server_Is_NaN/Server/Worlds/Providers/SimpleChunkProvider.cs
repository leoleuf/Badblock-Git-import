using System.Collections.Generic;
using Server_Is_NaN.Server.Worlds.Entities;

namespace Server_Is_NaN.Server.Worlds.Providers
{
    public class SimpleChunkProvider : IChunkProvider
    {
        public Chunk GetChunk(int x, int z)
        {
            return new Chunk(new SimpleChunkData());
        }

        public bool IsChunkLoaded(int x, int z)
        {
            return true;
        }

        public void UnloadChunk(Chunk chunk)
        {
            
        }

        public void UnloadChunks()
        {
            
        }
    }

    public class SimpleChunkData : IChunkData
    {
        public byte[] LoadBiomes()
        {
            return new byte[1 << 8];
        }

        public byte[] LoadBlocks()
        {
            return new byte[1 << 16];
        }

        public List<Entity> LoadEntities()
        {
            return new List<Entity>();
        }

        public void SetBiomes(byte[] biomes)
        {
        }

        public void SetBlocks(byte[] blocks)
        {
        }

        public void SetEntities(List<Entity> entities)
        {
        }
    }
}
