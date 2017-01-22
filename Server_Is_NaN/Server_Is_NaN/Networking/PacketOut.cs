using Server_Is_NaN.Networking.Sockets;

namespace Server_Is_NaN.Networking
{
    public abstract class PacketOut : Packet
    {
        private PacketOut() { }

        public abstract void Handle(OutPacketHandler handler);

        public abstract class PacketOutJson : PacketOut, JsonPacket
        {
            public PacketOutJson() : base() { }
        }

        public abstract class PacketOutByte : PacketOut, BytePacket
        {
            public PacketOutByte() : base() { }

            public abstract void Deserialize(ByteNetworkStream stream);

            public abstract void Serialize(ByteNetworkStream stream);
        }
    }

    public class PacketsOut : PacketGroup
    {
        public static readonly PacketsOut OUT = new PacketsOut();

        protected override void HandlePacket(object packet, object handler)
        {
            PacketIn p = (PacketIn)packet;

            p.Handle((InPacketHandler)handler);
        }
    }
}
