using System;
using System.Collections.Generic;
using Server_Is_NaN.Networking.Sockets;

namespace Server_Is_NaN.Networking
{
    public abstract class PacketIn : Packet
    {
        private PacketIn() { }

        public abstract void Handle(InPacketHandler handler);

        /* Sub classes */

        public abstract class PacketInJson : PacketIn, JsonPacket { }

        public abstract class PacketInByte : PacketIn, BytePacket
        {
            public abstract void Deserialize(ByteNetworkStream stream);

            public abstract void Serialize(ByteNetworkStream stream);
        }
    }

    public class PacketsIn : PacketGroup
    {
        public static readonly PacketsIn IN = new PacketsIn();

        protected override void HandlePacket(object packet, object handler)
        {
            PacketIn p = (PacketIn) packet;

            p.Handle((InPacketHandler) handler);
        }
    }
}
