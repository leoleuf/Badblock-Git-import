using System.Collections.Generic;

using Newtonsoft.Json.Linq;

using Server_Is_NaN.Server.Config;
using Server_Is_NaN.Utils;
using System;

namespace Server_Is_NaN.Server.Worlds.Entities
{
    public class Player : Entity
    {
        const int timeBetweenKeepAlive = 20;

        private Server server { get {
                return Server.Instance;
         } }

        private int keepAliveTo = -1, keepAliveFrom = 0;

        public List<SimpleCoord> chunks = new List<SimpleCoord>(); 
        private Queue<SimpleCoord> chunksToShow { get; } = new Queue<SimpleCoord>(); 

        public PlayerConnection Connection { get; }
        public string Name { get; }

        public double lastUpdatedX = 0, lastUpdatedZ = 0;
        
        public Player(string name, PlayerConnection connection)
        {
            this.Name = name;
            this.Connection = connection;

            PlayerData data = PlayerData.LoadData(name);

            if (data.location == null || data.location.world == null /* World doesn't exist lel */)
            {
                data.location = server.GetMainWorld().GetSpawnLocation();
            }

            Position = data.location;
            data.location.world.AddEntity(this);
            data.location.world.ShowChunks(this);
        }

        public override void tick()
        {
            if (Connection.IsDisconnected())
            {
                Position.world.RemoveFromWorld(this);
                Removed = true;
                return;
            }

            //TODO world & position change <3

            keepAliveTo++;
            keepAliveFrom++;

            if (chunksToShow.Count > 0)
            {
                List<Chunk> chunks = new List<Chunk>();

                while (chunksToShow.Count > 0 && chunks.Count < server.MaxChunkBulk)
                {
                    SimpleCoord coords = chunksToShow.Dequeue();

                    if (this.chunks.Contains(coords)) // verify if the chunk is still in list
                        chunks.Add( Position.world.GetChunk(coords.x, coords.z) );
                }
                Console.WriteLine(" === " + chunks.Count);
                SendChunk(chunks);
            }

            if (keepAliveTo >= timeBetweenKeepAlive)
            {
                keepAliveTo = 0;
                Connection.SendPacket(new Networking.Out.KeepAlive());
            }

            if (keepAliveFrom >= server.TimeOut)
            {
                Disconnect("Connection timed out");
                return;
            }
        }

        public void ShowChunk(SimpleCoord c)
        {
            this.chunksToShow.Enqueue(c);
            this.chunks.Add(c);
        }

        private void SendChunk(List<Chunk> chunks)
        {
            if (chunks.Count == 0)
                return;

            Connection.SendPacket(new Networking.Out.SendChunks( Chunk.ToSimple(chunks) ));
        }

        public void ReceiveKeepAlive()
        {
            this.keepAliveFrom = 0;
        }

        public void Disconnect(string message)
        {
            Connection.Disconnect(message);
        }

        public override JObject ToJson()
        {
            return null; //FIXME
        }
    }
}
