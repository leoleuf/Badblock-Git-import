namespace Server_Is_NaN.Utils
{
    public class SimpleCoord
    {
        public int x { get; private set; }
        public int z { get; private set; }

        public SimpleCoord(int x, int z)
        {
            this.x = x;
            this.z = z;
        }

        public int DistanceSquared(SimpleCoord coords)
        {
            int dx = x - coords.x;
            int dz = z - coords.z;

            return dx * dx + dz * dz;
        }
    }
}
