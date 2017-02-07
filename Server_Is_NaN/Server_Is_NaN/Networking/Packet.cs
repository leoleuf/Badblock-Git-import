using Server_Is_NaN.Networking.Sockets;
using System;
using Newtonsoft.Json;
using System.Collections.Generic;

namespace Server_Is_NaN.Networking
{
    public class Packet { }

    public interface JsonPacket { }

    public interface BytePacket
    {
        void Serialize(ByteNetworkStream stream);

        void Deserialize(ByteNetworkStream stream);
    }

    public abstract class PacketGroup
    {
        public Type[] packets = new Type[0xFF];
        public bool[] json = new bool[0xFF];
        public Dictionary<Type, int> ids = new Dictionary<Type, int>();

        protected void AddPacket<T>(int id)
        {
            AddPacket(id, typeof(T));
        }

        protected void AddPacket(int id, Type packet)
        {
            if (id < 0 || id >= 0xFF)
                throw new Exception("Invalid packet id (or in use): " + id);

            bool bytePacket = typeof(BytePacket).IsAssignableFrom(packet);
            bool jsonPacket = typeof(JsonPacket).IsAssignableFrom(packet);

            if (bytePacket == jsonPacket)
                throw new Exception("Invalid packet type " + packet);

            packets[id] = packet;
            json[id] = jsonPacket;
            ids[packet] = id;
        }

        public void ReadPacket(int id, object handler, ByteNetworkStream stream)
        {
            if (id > 0xFF || id < 0 || packets[id] == null)
                throw new Exception("Invalid packet id: " + id);

            object obj = null;

            if (json[id])
            {
                obj = JsonConvert.DeserializeObject(stream.ReadUTF(), packets[id]);
            }
            else
            {
                obj = Activator.CreateInstance(packets[id]);
                ((BytePacket)obj).Deserialize(stream);
            }

            HandlePacket(obj, handler);
        }

        public void WritePacket(ByteNetworkStream stream, ByteNetworkStream comp, bool compressed, Packet packet)
        {
            if (!ids.ContainsKey(packet.GetType()))
                throw new Exception("Can't send unregistered packet " + packet.GetType().Name);

            stream.WriteInt(ids[packet.GetType()]);
            stream.WriteBoolean(compressed);

            stream.Flush();
            stream = (compressed ? comp : stream);

            if (packet is JsonPacket)
            {
                stream.WriteUTF(JsonConvert.SerializeObject(packet));
            }
            else
            {
                BytePacket p = (BytePacket)packet;
                p.Serialize(stream);
            }

            stream.Flush();
        }

        protected abstract void HandlePacket(object packet, object handler);
    }
}
