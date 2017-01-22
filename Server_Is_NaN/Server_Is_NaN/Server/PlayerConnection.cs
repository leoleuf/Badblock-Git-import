using System;
using Server_Is_NaN.Networking;
using Server_Is_NaN.Utils;

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

        public override void handleHandshake(Networking.In.Handshake packet)
        {
            if (state != ConnectionState.HANDSHAKING)
                BadState();

            this.ProtocolVersion = packet.ProtocolVersion;
            this.Username = packet.Username;

            if (packet.Request == 0)
            {
                state = ConnectionState.PING; // waiting for ping request
                SendPacket(new Networking.Out.PingAnswer(0 /*FIXME*/, 0 /*FIXME*/, server.ProtocolVersion));
                Disconnect();
            }
            else if (packet.Request == 1)
            {
                if (packet.Username == null || !GeneralUtils.username_regex.IsMatch(packet.Username))
                    throw new Exception("Bad username!");
                if (!server.ProtocolVersion.Equals(packet.ProtocolVersion))
                    throw new Exception("Server use protocol version '" + server.ProtocolVersion + "'!");

                state = ConnectionState.PLAYING;
                SendPacket(new Networking.Out.LoginSuccess());
            }
            else
                throw new Exception("Bad request: " + packet.Request);
        }

        public override void handleKeepAlive(Networking.In.KeepAlive packet)
        {
            if (state != ConnectionState.PLAYING)
                BadState();
            
        }

        public void SendPacket(PacketOut packet)
        {

        }

        public void Disconnect()
        {
            // must send pending packets and close the socket
        }

        private void BadState()
        {
            throw new Exception("Bad state!");
        }
    }

    public enum ConnectionState
    {
        HANDSHAKING,
        PING,
        PLAYING
    }
}
