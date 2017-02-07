using System.Drawing;
using Server_Is_NaN.Server.Worlds.Generator.GenLayers;
using System.Diagnostics;
using System;
using Server_Is_NaN.Server.Worlds.Biomes;

namespace Server_Is_NaN
{
    class Program
    {
        static void Main(string[] args)
        {
            int w = 200;

            Bitmap bitmap = new Bitmap(w * 16, w * 16);
            GenLayer lay = GenLayer.CreateLayer(127431842179823417L);

            int c = 0;

            for (int cy = 0; cy < w; cy++)
            {
                for (int cx = 0; cx < w; cx++)
                {
                    c++;
                    int[] nums = lay.GetInts(cx * 16, cy * 16, 16, 16);

                    for (int y = 0; y < 16; y++)
                    {
                        for (int x = 0; x < 16; x++)
                        {
                            bitmap.SetPixel(x + cx * 16, y + cy * 16, color(nums[x + y * 16]));
                        }
                    }

                    if (c % 1000 == 0)
                        Console.WriteLine(c + "/" + (200 * 200));
                }
            }

            Console.WriteLine(cc + " moutains");

            bitmap.Save("test2.png");
            Process.Start("test2.png");

            //new Server.Server();
        }

        public static int cc = 0;

        private static Color color(int v)
        {
            switch ((BiomeList)v)
            {
                case BiomeList.OCEAN:
                    return Color.Blue;
                case BiomeList.DEEP_OCEAN:
                    return Color.DarkBlue;
                case BiomeList.ICE_PLAIN:
                    return Color.WhiteSmoke;
                case BiomeList.ICE_PLAINS_M:
                    return Color.LightGray;
                case BiomeList.ICE_MOUTAINS:
                    return Color.DimGray;
                case BiomeList.ICE_MOUTAINS_M:
                    return Color.Gray;
                case BiomeList.ICE_TAIGA:
                    return Color.LightGreen;
                case BiomeList.ICE_TAIGA_M:
                    return Color.LightSeaGreen;
                case BiomeList.ICE_FOREST:
                    return Color.GreenYellow;
                case BiomeList.ICE_FOREST_M:
                    return Color.LimeGreen;
                case BiomeList.ICE_BEACH:
                    return Color.LightYellow;
                case BiomeList.MOUTAIN:
                    return Color.SlateGray;
                case BiomeList.MOUTAIN_M:
                    cc++;
                    return Color.DarkGray;
                case BiomeList.TAIGA:
                    return Color.Green;
                case BiomeList.TAIGA_M:
                    return Color.DarkGreen;
                case BiomeList.PLAINS:
                    return Color.Olive;
                case BiomeList.FOREST:
                    return Color.Gray;
                case BiomeList.FOREST_M:
                    return Color.DarkOliveGreen;
                case BiomeList.JUNGLE:
                    return Color.Khaki;
                case BiomeList.JUNGLE_M:
                    return Color.DarkKhaki;
                case BiomeList.JUNGLE_EDGE:
                    return Color.LawnGreen;
                case BiomeList.JUNGLE_EDGE_M:
                    return Color.ForestGreen;
                case BiomeList.SWAMPLAND:
                    return Color.SandyBrown;
                case BiomeList.BEACH:
                    return Color.Salmon;
                case BiomeList.DESERT:
                    return Color.LightYellow;
                case BiomeList.DESERT_M:
                    return Color.Yellow;
                case BiomeList.SAVANNA:
                    return Color.LightGoldenrodYellow;
                case BiomeList.SAVANNA_M:
                    return Color.DarkGoldenrod;
            }

            return Color.White;
        }
    }
}
