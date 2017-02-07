using static Server_Is_NaN.Networking.PacketOut;

namespace Server_Is_NaN.Networking.Out
{
    public class LoginSuccess : PacketOutJson
    {
        private Dimension dimension;

        public LoginSuccess(Dimension dimension)
        {
            this.dimension = dimension;
        }

        public LoginSuccess() { }

        public override void Handle(OutPacketHandler handler)
        {
            handler.HandleLoginSuccess(this);
        }
    }
}
