using Server_Is_NaN.Server.Worlds.Biomes;

namespace Server_Is_NaN.Server.Worlds.Generator
{
    public class WorldGenInfo
    {
        public int[][] biomeGroups;
        public int ocean, deeepOcean;

        public static WorldGenInfo Earth()
        {
            WorldGenInfo info = new WorldGenInfo();

            info.biomeGroups = new int[][] {
                new int[]{ (int)BiomeList.DESERT, (int)BiomeList.DESERT, (int)BiomeList.DESERT, (int)BiomeList.SAVANNA, (int)BiomeList.PLAINS },
                new int[]{ (int) BiomeList.JUNGLE, (int)BiomeList.FOREST, (int)BiomeList.FOREST/*Roofed?*/, (int)BiomeList.MOUTAIN, (int)BiomeList.PLAINS, (int)BiomeList.FOREST/*Birch ?*/, (int)BiomeList.SWAMPLAND},
                new int[]{ (int)BiomeList.FOREST, (int)BiomeList.MOUTAIN, (int)BiomeList.TAIGA, (int)BiomeList.PLAINS },
                new int[]{ (int)BiomeList.ICE_PLAIN, (int)BiomeList.ICE_PLAIN, (int)BiomeList.ICE_FOREST, (int)BiomeList.ICE_TAIGA, (int)BiomeList.ICE_MOUTAINS}
            };

            info.ocean = (int) BiomeList.OCEAN;
            info.deeepOcean = (int) BiomeList.DEEP_OCEAN;

            return info;
        }
    }
}
