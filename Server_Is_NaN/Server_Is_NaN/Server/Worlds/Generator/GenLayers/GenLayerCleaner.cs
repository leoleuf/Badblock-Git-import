namespace Server_Is_NaN.Server.Worlds.Generator.GenLayers
{
    public class GenLayerCleaner : GenLayer
    {
        public GenLayerCleaner(GenLayer parent, long seed) : base(parent, seed) { }

        public override int[] GetInts(int x, int y, int width, int height)
        {
            int[] parent_res = parent.GetInts(x, y, width, height);

            for (int dy = 0; dy < height; dy++)
            {
                for (int dx = 0; dx < width; dx++)
                {
                    int pos = dx + dy * width;

                    InitLocalSeed(dx + x, dy + y);
                    parent_res[pos] = (parent_res[pos] > 0) ? NextInt(299999) + 2 : 0;
                }
            }

            return parent_res;
        }
    }
}
