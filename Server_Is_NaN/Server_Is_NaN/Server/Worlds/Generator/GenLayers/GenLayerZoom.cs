using System;

namespace Server_Is_NaN.Server.Worlds.Generator.GenLayers
{
    public class GenLayerZoom : GenLayer
    {
        public GenLayerZoom(GenLayer parent, long worldSeed) : base(parent, worldSeed) { }

        public override int[] GetInts(int x, int y, int width, int height)
        {
            int parent_x = x >> 1;
            int parent_y = y >> 1;
            int parent_w = (width >> 1) + 2;
            int parent_height = (height >> 1) + 2;

            int[] parent = this.parent.GetInts(parent_x, parent_y, parent_w, parent_height);

            int w = parent_w - 1 << 1;
            int h = parent_height - 1 << 1;

            int[] result = new int[w * h];
            int k2;

            for (int line = 0; line < parent_height - 1; line++)
            {
                k2 = (line << 1) * w;

                int j3 = parent[(line + 0) * parent_w];
                int k3 = parent[(line + 1) * parent_w];

                for (int column = 0; column < parent_w - 1; ++column)
                {
                    this.InitLocalSeed((long)(column + parent_x << 1), (long)(line + parent_y << 1));

                    int l3 = parent[column + 1 + (line + 0) * parent_w];
                    int i4 = parent[column + 1 + (line + 1) * parent_w];

                    result[k2] = j3;
                    result[k2++ + w] = SelectRandom(j3, k3);
                    result[k2]       = SelectRandom(j3, l3);
                    result[k2++ + w] = SelectModeOrRandom(j3, l3, k3, i4);

                    j3 = l3;
                    k3 = i4;
                }
            }

            int[] res = new int[width * height];

            for (k2 = 0; k2 < height; ++k2)
                Array.Copy(result, (k2 + (y & 1)) * w + (x & 1), res, k2 * width, width);

            return res;
        }

        protected virtual int SelectModeOrRandom(int i1, int i2, int i3, int i4)
        {
            if (i1 == i2)
                if ((i3 != i4) || (i1 == i3))
                    return i1;
                else
                    return new int[] { i1, i2, i3, i4 }[NextInt(4)];

            if (i1 == i3 && i2 != i4)
                return i1;

            if (i1 == i4 && i2 != i3)
                return i1;

            if ((i2 == i3 && i1 != i4) || (i2 == i4))
                return i2;

            return new int[] { i1, i2, i3, i4 }[NextInt(4)];
        }
    }
}
