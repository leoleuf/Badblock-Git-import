namespace Server_Is_NaN.Server.Worlds.Generator.GenLayers
{
    public class GenLayerIsland : GenLayer
    {
        public GenLayerIsland(GenLayer parent, long seed) : base(parent, seed) { }

        public override int[] GetInts(int x, int y, int width, int height)
        {
            int[] vals = new int[width * height];

            for (int dy = 0; dy < height; dy++)
            {
                for (int dx = 0; dx < width; dx++)
                {
                    InitLocalSeed(x + dx, y + dy);
                    vals[dy * width + dx] = NextInt(10) == 0 ? 1 : 0;
                }
            }

            return vals;
        }
    }
}
