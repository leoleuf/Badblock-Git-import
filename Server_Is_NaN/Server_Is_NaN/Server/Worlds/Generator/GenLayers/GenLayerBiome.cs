namespace Server_Is_NaN.Server.Worlds.Generator.GenLayers
{
    public class GenLayerBiome : GenLayer
    {
        public GenLayerBiome(GenLayer parent, long seed) : base(parent, seed) { }

        public override int[] GetInts(int x, int y, int width, int height)
        {
            int[] par = parent.GetInts(x, y, width, height);

            for (int dy = 0; dy < height; dy++)
            {
                for (int dx = 0; dx < width; dx++)
                {
                    InitLocalSeed(dx + x, dy + y);
                    int pos = dx + dy * width;

                    if (par[pos] != genInfo.ocean)
                        par[pos] = SelectRandom(genInfo.biomeGroups[par[pos] - genInfo.ocean - 1]);
                }
            }

            return par;
        }
    }
}
