using Server_Is_NaN.Networking.Sockets;
using Server_Is_NaN.Networking.Out;

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

        public PacketsOut()
        {
            AddPacket<Disconnect>(0x00);
            AddPacket<KeepAlive>(0x01);
            AddPacket<PingAnswer>(0x02);
            AddPacket<LoginSuccess>(0x03);

            AddPacket<SendChunks>(0x10);
        }

        protected override void HandlePacket(object packet, object handler)
        {
            PacketOut p = (PacketOut)packet;

            p.Handle((OutPacketHandler)handler);
        }
    }
}
