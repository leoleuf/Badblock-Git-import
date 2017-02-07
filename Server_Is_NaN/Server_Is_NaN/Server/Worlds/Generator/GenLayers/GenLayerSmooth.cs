namespace Server_Is_NaN.Server.Worlds.Generator.GenLayers
{
    public class GenLayerSmooth : GenLayer
    {
        public GenLayerSmooth(GenLayer parent, long seed) : base(parent, seed) { }

        public override int[] GetInts(int x, int y, int width, int height)
        {
            int parent_x = x - 1;
            int parent_y = y - 1;
            int parent_width = width + 2;
            int parent_height = height + 2;
            int[] parent_res = this.parent.GetInts(parent_x, parent_y, parent_width, parent_height);
            int[] res = new int[width * height];

            for (int dy = 0; dy < height; ++dy)
            {
                for (int dx = 0; dx < width; ++dx)
                {
                    int k2 = parent_res[dx + 0 + (dy + 1) * parent_width];
                    int l2 = parent_res[dx + 2 + (dy + 1) * parent_width];
                    int i3 = parent_res[dx + 1 + (dy + 0) * parent_width];
                    int j3 = parent_res[dx + 1 + (dy + 2) * parent_width];
                    int k3 = parent_res[dx + 1 + (dy + 1) * parent_width];

                    if (k2 == l2 && i3 == j3)
                    {
                        this.InitLocalSeed((long)(dx + x), (long)(dy + y));
                        if (this.NextInt(2) == 0)
                            k3 = k2;
                        else
                            k3 = i3;
                    }
                    else
                    {
                        if (k2 == l2)
                            k3 = k2;

                        if (i3 == j3)
                            k3 = i3;
                    }

                    res[dx + dy * width] = k3;
                }
            }

            return res;
        }
    }
}
