using static Server_Is_NaN.Networking.PacketOut;

namespace Server_Is_NaN.Networking.Out
{
    public class ChangeDimension : PacketOutJson
    {
        private Dimension dimension;

        public ChangeDimension(Dimension dimension)
        {
            this.dimension = dimension;
        }

        public ChangeDimension() { }

        public override void Handle(OutPacketHandler handler)
        {
            handler.HandleChangeDimension(this);
        }
    }
}
