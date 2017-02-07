using Server_Is_NaN.Networking.Sockets;
using static Server_Is_NaN.Networking.PacketOut;

namespace Server_Is_NaN.Networking.Out
{
    public class SendChunks : PacketOutByte
    {
        public SimpleChunk[] chunks { get; private set; }

        public SendChunks(SimpleChunk[] chunk)
        {
            this.chunks = chunk;
        }

        public SendChunks() { }

        public override void Deserialize(ByteNetworkStream stream)
        {
            this.chunks = stream.ReadArray(() => new SimpleChunk(stream));
        }

        public override void Serialize(ByteNetworkStream stream)
        {
            stream.WriteArray(c => c.Write(stream), chunks);
        }

        public override void Handle(OutPacketHandler handler)
        {
            handler.HandleSendChunks(this);
        }
    }

    public class SimpleChunk
    {
        public int x, z;

        public byte[] blocks;
        public byte[] biomes;

        public SimpleChunk(int x, int z, byte[] blocks, byte[] biomes)
        {
            this.blocks = blocks;
            this.biomes = biomes;
        }

        public SimpleChunk(ByteNetworkStream stream)
        {
            this.x = stream.ReadInt();
            this.z = stream.ReadInt();
            this.blocks = stream.ReadByteArray();
            this.biomes = stream.ReadByteArray();
        }

        public void Write(ByteNetworkStream stream)
        {
            stream.WriteInt(x);
            stream.WriteInt(z);
            stream.WriteArray(stream.WriteByte, blocks);
            stream.WriteArray(stream.WriteByte, biomes);
        }

        public byte GetBlock(int x, int y, int z)
        {
            return blocks[y + x * 256 + z * 16 * 256];
        }

        public byte GetBiome(int x, int z)
        {
            return biomes[x + z * 16];
        }
    }
}
