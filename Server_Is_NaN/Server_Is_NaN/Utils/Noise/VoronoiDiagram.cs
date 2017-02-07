using System;

namespace Server_Is_NaN.Utils.Noise
{
    public class VoronoiDiagram
    {
        private readonly double size;

        private readonly OctaveGenerator distorsion_x, distorsion_y;
        private readonly bool useDistorsion;

        private readonly int seed;

        public VoronoiDiagram(double size, int seed, bool useDistorsion)
        {
            this.size = size;
            this.seed = seed;

            this.useDistorsion = useDistorsion;

            if (useDistorsion)
            {
                this.distorsion_x = new PerlinOctaveGenerator(seed + 3981, 4);
                this.distorsion_y = new PerlinOctaveGenerator(seed + 9817, 4);
            }
        }

        public VoronoiDiagram(int size, int seed) : this(size, seed, true) { }

        public SimpleCoord GetCell(double x, double y)
        {
            double noiseX = 0, noiseY = 0;

            if (useDistorsion)
            {
                noiseX = distorsion_x.noise(x / 256.0d, 2d, 0.5d) * 50d;
                noiseY = distorsion_x.noise(y / 256.0d, 2d, 0.5d) * 50d;
            }

            return GetCell0(x + noiseX, y + noiseY);
        }

        private SimpleCoord GetCell0(double x, double y)
        {
            int x0 = (int) (x / size), y0 = (int) (y / size);

            SimpleCoord nearest = null;
            double min = -1;
            int max = 2;

            for (int i = -max; i <= max; i++)
            {
                for (int i2 = -max; i2 <= max; i2++)
                {
                    double d1 = DistanceSquared(x, y, x0 + i, y0 + i2);

                    if (min == -1 || d1 < min)
                    {
                        min = d1;
                        nearest = new SimpleCoord(x0 + i, y0 + i2);
                    }
                }
            }

            return nearest;
        }

        private double DistanceSquared(double x0, double z0, int xn, int zn)
        {
            Random random = new Random(seed * xn * zn);

            xn = (int) (xn * size + random.NextDouble() * size - (size / 2));
            zn = (int) (zn * size + random.NextDouble() * size - (size / 2));

            double dx = x0 - xn;
            double dz = z0 - zn;

            return dx * dx + dz * dz;
        }
    }
}
