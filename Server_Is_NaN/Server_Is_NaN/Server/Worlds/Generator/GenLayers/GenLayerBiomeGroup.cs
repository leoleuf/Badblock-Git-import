namespace Server_Is_NaN.Server.Worlds.Generator.GenLayers
{
    public class GenLayerBiomeGroup : GenLayer
    {
        public GenLayerBiomeGroup(GenLayer parent, long seed) : base(parent, seed) { }

        public override int[] GetInts(int x, int y, int width, int height)
        {
            int[] par = parent.GetInts(x, y, width, height);

            for (int dy = 0; dy < height; dy++)
            {
                for (int dx = 0; dx < width; dx++)
                {
                    InitLocalSeed(dx + x, dy + y);

                    if (par[dx + dy * width] != 0 || genInfo.ocean < 0)
                    {
                        par[dx + dy * width] = NextInt(genInfo.biomeGroups.Length) + 1 + genInfo.ocean;
                    }
                    else
                    {
                        par[dx + dy * width] = genInfo.ocean;
                    }
                }
            }

            return par;
        }
    }
}
