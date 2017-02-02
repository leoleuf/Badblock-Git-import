using Server_Is_NaN.Server.World.Entities;
using System.Collections.Generic;

namespace Server_Is_NaN.Server.World
{
    public interface IChunkData
    {
        byte[] LoadBlocks();
        
        void SetBlocks(byte[] blocks);

        byte[] LoadBiomes();

        void SetBiomes(byte[] biomes);

        List<Entity> LoadEntities();

        void SetEntities(List<Entity> entities);
    }
}
