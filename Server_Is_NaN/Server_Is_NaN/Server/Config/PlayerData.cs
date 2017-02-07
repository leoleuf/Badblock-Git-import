using Server_Is_NaN.Server.Worlds;
using System.IO;

using Server_Is_NaN.Utils;

namespace Server_Is_NaN.Server.Config
{
    public class PlayerData
    {
        public static PlayerData LoadData(string user)
        {
            if (!Directory.Exists("users"))
            {
                Directory.CreateDirectory("users");
            }
            
            return JsonUtils.LoadFile<PlayerData>(Path.Combine("users", user.ToLower()));
        }

        public Location location;
    }
}
