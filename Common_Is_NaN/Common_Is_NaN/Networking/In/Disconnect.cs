using static Server_Is_NaN.Networking.PacketIn;

namespace Server_Is_NaN.Networking.In
{
    public class Disconnect : PacketInJson
    {
        public override void Handle(InPacketHandler handler)
        {
            handler.HandleDisconnect(this);
        }
    }
}
