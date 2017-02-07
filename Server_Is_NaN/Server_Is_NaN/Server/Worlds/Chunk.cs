using Server_Is_NaN.Networking.Out;
using Server_Is_NaN.Server.Worlds.Biomes;
using Server_Is_NaN.Server.Worlds.Blocks;
using Server_Is_NaN.Server.Worlds.Entities;
using System.Collections.Generic;

namespace Server_Is_NaN.Server.Worlds
{
    public class Chunk
    {
        public static SimpleChunk[] ToSimple(List<Chunk> chunk)
        {
            SimpleChunk[] res = new SimpleChunk[chunk.Count];

            for (int i = 0; i < chunk.Count; i++)
            {
                res[i] = new SimpleChunk(chunk[i].x, chunk[i].z, chunk[i].blocks, chunk[i].biomes);
            }

            return res;
        }

        /* Represent biome per column. For the column (x, z) biome is (x + z * 16) */
        private byte[] biomes;
        /* Represent blocks. For the block (x, y, z) the value is (y + x * HEIGHT + z * 16 * HEIGHT) */
        private byte[] blocks;

        private List<Entity> entities = new List<Entity>(); //FIXME

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
