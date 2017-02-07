using Server_Is_NaN.Server.Worlds.Biomes;

namespace Server_Is_NaN.Server.Worlds.Generator.GenLayers
{
    public class GenLayerBiomeHills : GenLayer
    {
        private GenLayer secondEntry = null;

        public GenLayerBiomeHills(GenLayer parent, GenLayer secondEntry, long seed) : base(parent, seed) {
            this.secondEntry = secondEntry;
        }

        public override int[] GetInts(int x, int y, int width, int height)
        {
            int[] parent_res = this.parent.GetInts(x - 1, y - 1, width + 2, height + 2);
            int[] sentry_res = this.secondEntry.GetInts(x - 1, y - 1, width + 2, height + 2);

            int[] res = new int[width * height];

            for (int dy = 0; dy < height; ++dy)
            {
                for (int dx = 0; dx < width; ++dx)
                {
                    InitLocalSeed(dx + x, dy + y);

                    int v1 = parent_res[dx + 1 + (dy + 1) * (width + 2)];
                    int v2 = sentry_res[dx + 1 + (dy + 1) * (width + 2)];

                    if ((v2 - 2) % 7 == 0 && !IsBiomeOcean(v1))
                    {
                        res[dx + dy * width] = (int) Biome.GetMType((BiomeList) v1);
                    }
                    else res[dx + dy * width] = v1;
                }
            }

            return res;
        }
    }
}
