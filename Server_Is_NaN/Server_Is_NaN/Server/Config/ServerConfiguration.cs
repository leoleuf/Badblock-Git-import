using Server_Is_NaN.Networking.Sockets;

namespace Server_Is_NaN.Server.Config
{
    public class ServerConfiguration
    {
        public string ip = "";
        public int port = SocketListener.default_port;
        public int maxPlayers = 16;

        public int timeOut = 20 * 20;
        public WorldConf[] worlds = new WorldConf[] { };

        public int maxChunkBulk = 10;
    }

    public class WorldConf
    {
        public string name;
        public Dimension dimension;
    }
}
