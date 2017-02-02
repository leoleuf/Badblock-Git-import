using Server_Is_NaN.Server.World.Biomes;
using Server_Is_NaN.Server.World.Blocks;
using Server_Is_NaN.Server.World.Entities;
using System.Collections.Generic;

namespace Server_Is_NaN.Server.World
{
    public class Chunk
    {
        /* Represent biome per column. For the column (x, z) biome is (x + z * 16) */
        private byte[] biomes;
        /* Represent blocks. For the block (x, y, z) the value is (x + y * HEIGHT + z * 16) */
        private byte[] blocks;

        private List<Entity> entities;

        public int x { get; }
        public int z { get; }

        public Chunk(IChunkData data)
        {
            biomes = data.LoadBiomes();
            blocks = data.LoadBlocks();
        }

        public Biome GetBiomeAt(int x, int z) {
            return Biome.GetById(biomes[x + z * 16]);
        }

        public Block GetBlockAt(int x, int y, int z)
        {
            /* TODO */
            return null;
        }

        public void LoadChunk(/* World */)
        {
            // Add entities
        }

        public void UnloadChunk(/* World */)
        {
            // Remove entities
        }

        public void StoreChunk(IChunkData data)
        {
            data.SetBiomes(biomes);
            data.SetBlocks(blocks);
            data.SetEntities(entities);
        }
    }
}
