using Newtonsoft.Json.Linq;
using System;

namespace Server_Is_NaN.Server.World.Entities
{
    public class Player : Entity
    {
        const int timeBetweenKeepAlive = 20;

        private Server server { get {
                return Server.Instance;
        } }

        private int keepAliveTo = -1, keepAliveFrom = 0;
        public PlayerConnection connection { get; }

        public Player(PlayerConnection connection)
        {
            this.connection = connection;
            Server.Instance.temp.AddEntity(this);
        }

        public override void tick()
        {
            if (connection.IsDisconnected())
            {
                Removed = true;
                return;
            }

            keepAliveTo++;
            keepAliveFrom++;
            //Console.WriteLine(keepAliveFrom);

            if (keepAliveTo >= timeBetweenKeepAlive)
            {
                keepAliveTo = 0;
                connection.SendPacket(new Networking.Out.KeepAlive());
            }

            if (keepAliveFrom >= server.TimeOut)
            {
                Disconnect("Connection timed out");
                return;
            }
        }

        public void ReceiveKeepAlive()
        {
            this.keepAliveFrom = 0;
        }

        public void Disconnect(string message)
        {
            connection.Disconnect(message);
        }

        public override JObject ToJson()
        {
            return null; //FIXME
        }
    }
}
