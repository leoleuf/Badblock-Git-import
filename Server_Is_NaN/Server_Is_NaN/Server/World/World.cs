using Server_Is_NaN.Server.World.Blocks;

namespace Server_Is_NaN.Server.World
{
    public class World
    {
        public static readonly int WORLD_HEIGHT = 256;
        private readonly IChunkProvider provider;

        public Chunk GetChunk(int x, int z)
        {
            return provider.GetChunk(x, z);
        }

        public Chunk GetChunkAt(int x, int z)
        {
            return provider.GetChunk(x >> 4, z >> 4);
        }

        public Block GetBlockAt(int x, int y, int z)
        {
            if (y < 0 || y >= WORLD_HEIGHT)
            {
                return Block.AIR;
            }

            return GetChunkAt(x, z).GetBlockAt(x % 16, y, z % 16);
        }
    }
}
