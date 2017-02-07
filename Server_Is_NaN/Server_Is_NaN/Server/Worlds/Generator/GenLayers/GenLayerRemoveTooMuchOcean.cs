namespace Server_Is_NaN.Server.Worlds.Generator.GenLayers
{
    public class GenLayerRemoveTooMuchOcean : GenLayer
    {
        public GenLayerRemoveTooMuchOcean(GenLayer parent, long seed) : base(parent, seed) { }

        public override int[] GetInts(int x, int y, int width, int height)
        {
            int parent_x = x - 1;
            int parent_y = y - 1;
            int parent_w = width + 2;
            int l = height + 2;
            int[] parent_res = parent.GetInts(parent_x, parent_y, parent_w, l);
            int[] result = new int[width * height];

            for (int dy = 0; dy < height; ++dy)
            {
                for (int dx = 0; dx < width; ++dx)
                {
                    int k1 = parent_res[dx + 1 + (dy + 1 - 1) * (width + 2)];
                    int l1 = parent_res[dx + 1 + 1 + (dy + 1) * (width + 2)];
                    int i2 = parent_res[dx + 1 - 1 + (dy + 1) * (width + 2)];
                    int j2 = parent_res[dx + 1 + (dy + 1 + 1) * (width + 2)];
                    int k2 = parent_res[dx + 1 + (dy + 1) * parent_w];
                    result[dx + dy * width] = k2;
                    InitLocalSeed((long)(dx + x), (long)(dy + y));

                    if (k2 == 0 && k1 == 0 && l1 == 0 && i2 == 0 && j2 == 0 && this.NextInt(2) == 0)
                    {
                        result[dx + dy * width] = 1;
                    }
                }
            }

            return result;
        }
    }
}
