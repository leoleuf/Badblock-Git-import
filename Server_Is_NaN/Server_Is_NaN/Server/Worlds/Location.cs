using Newtonsoft.Json;
using Server_Is_NaN.Server.World;
using System;

namespace Server_Is_NaN.Server.Worlds
{
    public class Location
    {
        public SWorld world { get; private set; }
        public double x { get; set; }
        public double y { get; set; }
        public double z { get; set; }

        public Location(SWorld world, double x, double y, double z)
        {
            this.world = world;
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
}
