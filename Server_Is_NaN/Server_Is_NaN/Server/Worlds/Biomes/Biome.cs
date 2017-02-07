namespace Server_Is_NaN.Server.Worlds.Biomes
{
    public abstract class Biome
    {
        public static Biome GetById(byte id)
        {
            return null;
        }

        public static BiomeList GetBeachByType(BiomeList biome)
        {
            return BiomeList.BEACH;
        }

        public static BiomeList GetMType(BiomeList biome)
        {
            switch (biome)
            {
                case BiomeList.ICE_PLAIN:
                    return BiomeList.ICE_PLAINS_M;
                case BiomeList.ICE_MOUTAINS:
                    return BiomeList.ICE_MOUTAINS_M;
                case BiomeList.ICE_TAIGA:
                    return BiomeList.ICE_TAIGA_M;
                case BiomeList.ICE_FOREST:
                    return BiomeList.ICE_FOREST_M;
                case BiomeList.MOUTAIN:
                    return BiomeList.MOUTAIN_M;
                case BiomeList.TAIGA:
                    return BiomeList.TAIGA_M;
                case BiomeList.FOREST:
                    return BiomeList.FOREST_M;
                case BiomeList.JUNGLE:
                    return BiomeList.JUNGLE_M;
                case BiomeList.JUNGLE_EDGE:
                    return BiomeList.JUNGLE_EDGE_M;
                case BiomeList.DESERT:
                    return BiomeList.DESERT_M;
                case BiomeList.SAVANNA:
                    return BiomeList.SAVANNA_M;
            }

            return biome;
        }

        public abstract BiomeList GetBeach();

        public abstract BiomeList GetMVersion();
    }

    public enum BiomeList
    {
        // 'M' Biomes are a variant of a biome with a bigger elevation factor
        OCEAN,
        DEEP_OCEAN,
        /* Iced biomes */
        ICE_PLAIN,
        ICE_PLAINS_M,
        ICE_MOUTAINS,
        ICE_MOUTAINS_M,
        ICE_TAIGA,
        ICE_TAIGA_M,
        ICE_FOREST,
        ICE_FOREST_M,
        ICE_BEACH,
        /* Cold biomes */
        MOUTAIN,
        MOUTAIN_M,
        TAIGA,
        TAIGA_M,
        /* Normal biomes */
        PLAINS,
        PLAINS_M,
        FOREST,
        FOREST_M,
        JUNGLE,
        JUNGLE_M,
        JUNGLE_EDGE,
        JUNGLE_EDGE_M,
        SWAMPLAND,
        BEACH,
        /* Hot biomes */
        DESERT,
        DESERT_M,
        SAVANNA,
        SAVANNA_M
    }
}
