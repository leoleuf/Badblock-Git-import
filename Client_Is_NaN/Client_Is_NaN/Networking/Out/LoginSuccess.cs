using static Server_Is_NaN.Networking.PacketOut;

namespace Server_Is_NaN.Networking.Out
{
    public class LoginSuccess : PacketOutJson
    {
        /* No content at this moment. Will appear with new features */

        public override void Handle(OutPacketHandler handler)
        {
            handler.HandleLoginSuccess(this);
        }
    }
}
