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

        public Handshake()
        {

        }

        public Handshake(int Request, string ProtocolVersion, string Username)
        {
            this.Request = Request;
            this.ProtocolVersion = ProtocolVersion;
            this.Username = Username;
        }

        public override void Handle(InPacketHandler handler)
        {
            handler.HandleHandshake(this);
        }
    }
}
