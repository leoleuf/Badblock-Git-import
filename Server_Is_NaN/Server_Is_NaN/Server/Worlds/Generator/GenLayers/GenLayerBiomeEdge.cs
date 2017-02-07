using Server_Is_NaN.Server.Worlds.Biomes;
using System;

namespace Server_Is_NaN.Server.Worlds.Generator.GenLayers
{
    public class GenLayerBiomeEdge : GenLayer
    {
        private BiomeList[] forests = new BiomeList[]
            {
                BiomeList.JUNGLE,
                BiomeList.JUNGLE_M,
                BiomeList.FOREST,
                BiomeList.FOREST_M,
                BiomeList.ICE_FOREST,
                BiomeList.ICE_FOREST_M,
                BiomeList.TAIGA,
                BiomeList.TAIGA_M,
                BiomeList.ICE_TAIGA,
                BiomeList.ICE_TAIGA_M
            };

        public GenLayerBiomeEdge(GenLayer parent, long seed) : base(parent, seed) { }

        public override int[] GetInts(int x, int y, int width, int height)
        {
            int parent_x = x - 1;
            int parent_y = y - 1;
            int parent_w = width + 2;
            int parent_h = height + 2;
            int[] parent_res = parent.GetInts(parent_x, parent_y, parent_w, parent_h);
            int[] res = new int[width * height];

            for (int dy = 0; dy < height; ++dy)
            {
                for (int dx = 0; dx < width; ++dx)
                {
                    int l1 = parent_res[dx + 1 + (dy + 0) * (parent_w)];
                    int i2 = parent_res[dx + 2 + (dy + 1) * (parent_w)];
                    int j2 = parent_res[dx + 0 + (dy + 1) * (parent_w)];
                    int k2 = parent_res[dx + 1 + (dy + 2) * (parent_w)];

                    int current = parent_res[dx + 1 + (dy + 1) * parent_w];

                    if (IsBiomeOcean(l1) || IsBiomeOcean(i2) || IsBiomeOcean(j2) || IsBiomeOcean(k2))
                    {
                        if(!IsBiomeOcean(current))
                            current = (int)BiomeList.BEACH;
                    }
                    else if (current == (int)BiomeList.JUNGLE)
                    {
                        if (VerifyAll(IsJungleForest, l1, i2, j2, k2))
                        {
                            current = (int)BiomeList.JUNGLE_EDGE;
                        }
                    }


                    res[dx + dy * width] = current;
                }
            }

            return res;
        }

        private bool VerifyAll(Predicate<int> pred, params int[] vals)
        {
            int i = 0;
            while (i < vals.Length && pred.Invoke(vals[i]))
                i++;

            return i >= vals.Length;
        }

        private bool IsJungleForest(int id)
        {
            if (IsBiomeOcean(id))
                return true;

            BiomeList biome = (BiomeList)id;
            int i = 0;

            while (i < forests.Length && forests[i] != biome)
                i++;

            return i < forests.Length;
        }
    }
}
