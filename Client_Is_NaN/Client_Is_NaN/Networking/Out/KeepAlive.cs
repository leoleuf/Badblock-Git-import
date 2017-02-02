using static Server_Is_NaN.Networking.PacketOut;

namespace Server_Is_NaN.Networking.Out
{
    public class KeepAlive : PacketOutJson
    {
        public long id;

        public override void Handle(OutPacketHandler handler)
        {
            handler.HandleKeepAlive(this);
        }
    }
}
