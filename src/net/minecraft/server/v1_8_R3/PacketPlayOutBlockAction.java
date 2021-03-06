package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutBlockAction implements Packet<PacketListenerPlayOut> {

    private BlockPosition a;
    private int b;
    private int c;
    private Block d;

    public PacketPlayOutBlockAction() {}

    public PacketPlayOutBlockAction(BlockPosition blockposition, Block block, int i, int j) {
        this.a = blockposition;
        this.b = i;
        this.c = j;
        this.d = block;
    }

    @Override
	public void a(PacketDataSerializer packetdataserializer) throws IOException {
        this.a = packetdataserializer.c();
        this.b = packetdataserializer.readUnsignedByte();
        this.c = packetdataserializer.readUnsignedByte();
        this.d = Block.getById(packetdataserializer.e() & 4095);
    }

    @Override
	public void b(PacketDataSerializer packetdataserializer) throws IOException {
        packetdataserializer.a(this.a);
        packetdataserializer.writeByte(this.b);
        packetdataserializer.writeByte(this.c);
        packetdataserializer.b(Block.getId(this.d) & 4095);
    }

    @Override
	public void a(PacketListenerPlayOut packetlistenerplayout) {
        packetlistenerplayout.a(this);
    }

}