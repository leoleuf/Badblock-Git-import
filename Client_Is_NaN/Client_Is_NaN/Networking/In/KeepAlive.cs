using static Server_Is_NaN.Networking.PacketIn;

namespace Server_Is_NaN.Networking.In
{
    public class KeepAlive : PacketInJson
    {
        public long id;

        public override void Handle(InPacketHandler handler)
        {
            handler.HandleKeepAlive(this);
        }
    }
}
