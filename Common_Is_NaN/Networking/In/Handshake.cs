using static Server_Is_NaN.Networking.PacketIn;

namespace Server_Is_NaN.Networking.In
{
    public class Handshake : PacketInJson
    {
        /* Request: ping (0) or login (1). If 1, an username is requested. */
        public int Request;
        public string ProtocolVersion;
        /* Optional (login only) */
        public string Username;

        public override void Handle(InPacketHandler handler)
        {
            handler.handleHandshake(this);
        }
    }
}
