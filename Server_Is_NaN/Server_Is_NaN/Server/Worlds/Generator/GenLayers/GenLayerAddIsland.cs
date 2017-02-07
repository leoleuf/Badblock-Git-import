namespace Server_Is_NaN.Server.Worlds.Generator.GenLayers
{
    public class GenLayerAddIsland : GenLayer
    {
        public GenLayerAddIsland(GenLayer parent, long seed) : base(parent, seed) { }

        public override int[] GetInts(int x, int y, int width, int height)
        {
            int i1 = x - 1;
            int j1 = y - 1;
            int parent_w = width + 2;
            int parent_h = height + 2;
            int[] parent_res = this.parent.GetInts(i1, j1, parent_w, parent_h);
            int[] result = new int[width * height];

            for (int dy = 0; dy < height; ++dy)
            {
                for (int dx = 0; dx < width; ++dx)
                {
                    int k2 = parent_res[dx + 0 + (dy + 0) * parent_w];
                    int l2 = parent_res[dx + 2 + (dy + 0) * parent_w];
                    int i3 = parent_res[dx + 0 + (dy + 2) * parent_w];
                    int j3 = parent_res[dx + 2 + (dy + 2) * parent_w];
                    int k3 = parent_res[dx + 1 + (dy + 1) * parent_w];

                    this.InitLocalSeed((long)(dx + x), (long)(dy + y));
                    if (k3 == 0 && (k2 != 0 || l2 != 0 || i3 != 0 || j3 != 0))
                    {
                        int l3 = 1;
                        int i4 = 1;

                        if (k2 != 0 && NextInt(l3++) == 0)
                        {
                            i4 = k2;
                        }

                        if (l2 != 0 && NextInt(l3++) == 0)
                        {
                            i4 = l2;
                        }

                        if (i3 != 0 && NextInt(l3++) == 0)
                        {
                            i4 = i3;
                        }

                        if (j3 != 0 && NextInt(l3++) == 0)
                        {
                            i4 = j3;
                        }

                        if (NextInt(3) == 0)
                        {
                            result[dx + dy * width] = i4;
                        }
                        else if (i4 == 4)
                        {
                            result[dx + dy * width] = 4;
                        }
                        else
                        {
                            result[dx + dy * width] = 0;
                        }
                    }
                    else if (k3 > 0 && (k2 == 0 || l2 == 0 || i3 == 0 || j3 == 0))
                    {
                        if (NextInt(5) == 0)
                        {
                            if (k3 == 4)
                            {
                                result[dx + dy * width] = 4;
                            }
                            else
                            {
                                result[dx + dy * width] = 0;
                            }
                        }
                        else
                        {
                            result[dx + dy * width] = k3;
                        }
                    }
                    else
                    {
                        result[dx + dy * width] = k3;
                    }
                }
            }

            return result;
        }
    }
}
