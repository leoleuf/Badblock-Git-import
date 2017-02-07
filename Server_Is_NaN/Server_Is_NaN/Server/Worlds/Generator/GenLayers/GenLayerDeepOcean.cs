namespace Server_Is_NaN.Server.Worlds.Generator.GenLayers
{
    public class GenLayerDeepOcean : GenLayer
    {
        public GenLayerDeepOcean(GenLayer parent, long seed) : base(parent, seed) { }

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
                    int k2 = parent_res[dx + 1 + (dy + 1 - 1) * (width + 2)];
                    int l2 = parent_res[dx + 1 + 1 + (dy + 1) * (width + 2)];
                    int i3 = parent_res[dx + 1 - 1 + (dy + 1) * (width + 2)];
                    int j3 = parent_res[dx + 1 + (dy + 1 + 1) * (width + 2)];
                    int k3 = parent_res[dx + 1 + (dy + 1) * parent_w];
                    int l3 = 0;

                    if (k2 == genInfo.ocean)
                        ++l3;

                    if (l2 == genInfo.ocean)
                        ++l3;

                    if (i3 == genInfo.ocean)
                        ++l3;

                    if (j3 == genInfo.ocean)
                        ++l3;

                    if (k3 == genInfo.ocean && l3 > 3)
                    {
                        res[dx + dy * width] = genInfo.deeepOcean;
                    }
                    else
                    {
                        res[dx + dy * width] = k3;
                    }
                }
            }

            return res;
        }
    }
}
