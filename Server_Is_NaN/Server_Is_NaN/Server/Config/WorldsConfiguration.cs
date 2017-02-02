using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Server_Is_NaN.Server.Config
{
    public class WorldsConfiguration
    {
        public WorldConfiguration[] worlds;
        public string mainWorld;   
    }

    public class WorldConfiguration
    {
        public string name;
    }
}
