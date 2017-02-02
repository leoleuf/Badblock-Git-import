using System;
using Server_Is_NaN.Networking;
using Server_Is_NaN.Utils;
using Server_Is_NaN.Server.World.Entities;
using Server_Is_NaN.Networking.Sockets;

namespace Server_Is_NaN.Server
{
    public class PlayerConnection : InPacketHandler
    {
        private ConnectionState state = ConnectionState.HANDSHAKING;
        private Server server { get {
                return Server.Instance;
        }}

        private string ProtocolVersion;
        private string Username;
        private Player Handler;

        private SocketHandler sock;

        public SocketHandler Socket {
            private get { return sock; }
            set {
                if (sock != null)
                    throw new Exception("SocketHandler already defininded!");

                sock = value;
            }
        }

        public bool IsDisconnected()
        {
            return Socket.IsDisconnected();
        }

        public override void HandleDisconnect(Networking.In.Disconnect packet)
        {
            state = ConnectionState.CLOSED;
            Socket.Close();
        }

        public override void HandleHandshake(Networking.In.Handshake packet)
        {
            BadState(ConnectionState.HANDSHAKING);
            
            this.ProtocolVersion = packet.ProtocolVersion;
            this.Username = packet.Username;

            if (packet.Request == 0)
            {
                state = ConnectionState.PING;
                SendPacket(new Networking.Out.PingAnswer(0 /*FIXME*/, server.Slots, server.ProtocolVersion));
                Disconnect();
            }
            else if (packet.Request == 1)
            {
                if (packet.Username == null || !GeneralUtils.username_regex.IsMatch(packet.Username))
                    throw new Exception("Bad username!");
                if (!server.ProtocolVersion.Equals(packet.ProtocolVersion))
                    throw new Exception("Server use protocol version '" + server.ProtocolVersion + "'!");

                Handler = new Player(this);
                //FIXME create player

                state = ConnectionState.PLAYING;
                SendPacket(new Networking.Out.LoginSuccess(/*FIXME*/));
            }
            else
                throw new Exception("Bad request: " + packet.Request);
        }

        public override void HandleKeepAlive(Networking.In.KeepAlive packet)
        {
            BadState(ConnectionState.PLAYING);
            Handler.ReceiveKeepAlive();
        }

        public void SendPacket(PacketOut packet)
        {
            Socket.SendPacket(packet);
        }

        public void Disconnect()
        {
            Disconnect("");
        }

        public void Disconnect(string message)
        {
            state = ConnectionState.CLOSED;
            SendPacket(new Networking.Out.Disconnect(message));
            Socket.Close();
        }

        private void BadState(ConnectionState needed)
        {
            if(state != needed)
                throw new Exception("Bad state!");
        }
    }    
}
