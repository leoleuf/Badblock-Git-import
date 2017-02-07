using System;

namespace Server_Is_NaN.Server.Worlds.Biomes
{
    public class BiomePlains : Biome
    {
        public BiomePlains()
        {

        }

        public override BiomeList GetBeach()
        {
            return BiomeList.BEACH;
        }

        public override BiomeList GetMVersion()
        {
            return BiomeList.PLAINS_M;
        }
    }
}
