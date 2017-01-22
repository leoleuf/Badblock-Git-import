using Newtonsoft.Json;
using System;
using System.IO;
using Server_Is_NaN.Utils;
using Server_Is_NaN.Networking.Sockets;

namespace Server_Is_NaN.Server
{
    /**
     * Represent server main class 
     */
    public class Server
    {
        public static Server Instance { private set; get; }

        public string ProtocolVersion { get; } = "alpha_1";
        public int Slots {get; set; }
        public string Ip { get; }
        public int Port { get; }


        public Server()
        {
            if (Instance != null)
                throw new Exception("Already running!");
            Instance = this;

            //ServerConfiguration config = JsonConvert.DeserializeObject<ServerConfiguration>(File.ReadAllText("config.json"));
            ServerConfiguration config = JsonUtils.LoadFile<ServerConfiguration>("config.json");
            JsonUtils.SaveFile("config.json", config, true);

            this.Slots = config.maxPlayers;
            this.Ip = config.ip;
            this.Port = config.port;
        }

        private void StartListening()
        {
            new SocketListener().StartListening(Ip, Port);
        }
    }
}
