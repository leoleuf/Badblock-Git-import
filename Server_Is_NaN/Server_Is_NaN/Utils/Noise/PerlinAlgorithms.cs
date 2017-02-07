using System;

namespace Server_Is_NaN.Utils.Noise
{
    public class PerlinNoiseGenerator : NoiseGenerator {
        protected static readonly int[][] grad3 = new int[][]{new int[]{1, 1, 0}, new int[]{-1, 1, 0}, new int[]{1, -1, 0}, new int[]{-1, -1, 0},
    	new int[]{1, 0, 1}, new int[]{-1, 0, 1}, new int[]{1, 0, -1}, new int[]{-1, 0, -1},
    	new int[]{0, 1, 1}, new int[]{0, -1, 1}, new int[]{0, 1, -1}, new int[]{0, -1, -1}};

        protected PerlinNoiseGenerator() {
            int[] p = {151, 160, 137, 91, 90, 15, 131, 13, 201,
                95, 96, 53, 194, 233, 7, 225, 140, 36, 103, 30, 69, 142, 8, 99, 37,
                240, 21, 10, 23, 190, 6, 148, 247, 120, 234, 75, 0, 26, 197, 62,
                94, 252, 219, 203, 117, 35, 11, 32, 57, 177, 33, 88, 237, 149, 56,
                87, 174, 20, 125, 136, 171, 168, 68, 175, 74, 165, 71, 134, 139,
                48, 27, 166, 77, 146, 158, 231, 83, 111, 229, 122, 60, 211, 133,
                230, 220, 105, 92, 41, 55, 46, 245, 40, 244, 102, 143, 54, 65, 25,
                63, 161, 1, 216, 80, 73, 209, 76, 132, 187, 208, 89, 18, 169, 200,
                196, 135, 130, 116, 188, 159, 86, 164, 100, 109, 198, 173, 186, 3,
                64, 52, 217, 226, 250, 124, 123, 5, 202, 38, 147, 118, 126, 255,
                82, 85, 212, 207, 206, 59, 227, 47, 16, 58, 17, 182, 189, 28, 42,
                223, 183, 170, 213, 119, 248, 152, 2, 44, 154, 163, 70, 221, 153,
                101, 155, 167, 43, 172, 9, 129, 22, 39, 253, 19, 98, 108, 110, 79,
                113, 224, 232, 178, 185, 112, 104, 218, 246, 97, 228, 251, 34, 242,
                193, 238, 210, 144, 12, 191, 179, 162, 241, 81, 51, 145, 235, 249,
                14, 239, 107, 49, 192, 214, 31, 181, 199, 106, 157, 184, 84, 204,
                176, 115, 121, 50, 45, 127, 4, 150, 254, 138, 236, 205, 93, 222,
                114, 67, 29, 24, 72, 243, 141, 128, 195, 78, 66, 215, 61, 156, 180};

            for (int i = 0; i < 512; i++) {
                perm[i] = p[i & 255];
            }
        }

        public PerlinNoiseGenerator(int seed) : this(new Random(seed)) { }

        public PerlinNoiseGenerator(Random rand) {
            
            offsetX = rand.NextDouble() * 256;
            offsetY = rand.NextDouble() * 256;
            offsetZ = rand.NextDouble() * 256;

            for (int i = 0; i < 256; i++) {
                perm[i] = rand.Next(256);
            }

            for (int i = 0; i < 256; i++) {
                int pos = rand.Next(256 - i) + i;
                int old = perm[i];

                perm[i] = perm[pos];
                perm[pos] = old;
                perm[i + 256] = perm[i];
            }
        }

        public override double noise(double x, double y, double z) {
            x += offsetX;
            y += offsetY;
            z += offsetZ;

            int floorX = floor(x);
            int floorY = floor(y);
            int floorZ = floor(z);

            // Find unit cube containing the point
            int X = floorX & 255;
            int Y = floorY & 255;
            int Z = floorZ & 255;

            // Get relative xyz coordinates of the point within the cube
            x -= floorX;
            y -= floorY;
            z -= floorZ;

            // Compute fade curves for xyz
            double fX = fade(x);
            double fY = fade(y);
            double fZ = fade(z);

            // Hash coordinates of the cube corners
            int A = perm[X] + Y;
            int AA = perm[A] + Z;
            int AB = perm[A + 1] + Z;
            int B = perm[X + 1] + Y;
            int BA = perm[B] + Z;
            int BB = perm[B + 1] + Z;

            return lerp(fZ, lerp(fY, lerp(fX, grad(perm[AA], x, y, z),
                            grad(perm[BA], x - 1, y, z)),
                        lerp(fX, grad(perm[AB], x, y - 1, z),
                            grad(perm[BB], x - 1, y - 1, z))),
                    lerp(fY, lerp(fX, grad(perm[AA + 1], x, y, z - 1),
                            grad(perm[BA + 1], x - 1, y, z - 1)),
                        lerp(fX, grad(perm[AB + 1], x, y - 1, z - 1),
                            grad(perm[BB + 1], x - 1, y - 1, z - 1))));
        }
    }

    public class PerlinOctaveGenerator : OctaveGenerator
    {
        public PerlinOctaveGenerator(int seed, int octaves) : this(new Random(seed), octaves) { }

        public PerlinOctaveGenerator(Random rand, int octaves) : base(createOctaves(rand, octaves)) { }

        private static NoiseGenerator[] createOctaves(Random rand, int octaves)
        {
            NoiseGenerator[] result = new NoiseGenerator[octaves];

            for (int i = 0; i < octaves; i++)
            {
                result[i] = new PerlinNoiseGenerator(rand);
            }

            return result;
        }
    }

}