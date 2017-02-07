using System;
using System.IO;
using Server_Is_NaN.Server.Config;
using Server_Is_NaN.Utils;
using Server_Is_NaN.Server.World;
using System.Threading;
using System.Collections.Generic;

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
        public int TimeOut { get; set; }
        public string Ip { get; }
        public int Port { get; }
        public int MaxChunkBulk { get; }

        //public SWorld temp = new SWorld();
        private List<SWorld> worlds = new List<SWorld>();

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
            this.TimeOut = config.timeOut;
            this.MaxChunkBulk = config.maxChunkBulk;

            LoadWorlds(config.worlds);
            StartListening();

            new Thread(tick).Start();
        }

        private void tick()
        {
            while (true)
            {
                Thread.Sleep(50);
                foreach (SWorld world in worlds)
                    world.tick();
            }
        }

        private void LoadWorlds(WorldConf[] worlds)
        {
            foreach (WorldConf world in worlds)
            {
                if (!Directory.Exists(world.name))
                {
                    Directory.CreateDirectory(world.name);
                }

                this.worlds.Add(new SWorld(world.name, world.dimension));
            }
        }

        private void StartListening()
        {
            Console.WriteLine(Port);
            new SocketListener().StartListening(Ip, Port);
        }

        public SWorld GetWorld(string name)
        {
            return worlds.Find(w => w.Name.Equals(name));
        }

        public ICollection<SWorld> GetWorlds()
        {
            return worlds.AsReadOnly();
        }

        public SWorld GetMainWorld()
        {
            return worlds[0]; //FIXME
        }
    }
}
