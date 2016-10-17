package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutBlockChange implements Packet<PacketListenerPlayOut> {

    private BlockPosition a;
    public IBlockData block;

    public PacketPlayOutBlockChange() {}

    public PacketPlayOutBlockChange(World world, BlockPosition blockposition) {
        this.a = blockposition;
        this.block = world.getType(blockposition);
    }

    @Override
	public void a(PacketDataSerializer packetdataserializer) throws IOException {
        this.a = packetdataserializer.c();
        this.block = Block.d.a(packetdataserializer.e());
    }

    @Override
	public void b(PacketDataSerializer packetdataserializer) throws IOException {
        packetdataserializer.a(this.a);
        packetdataserializer.b(Block.d.b(this.block));
    }

    @Override
	public void a(PacketListenerPlayOut packetlistenerplayout) {
        packetlistenerplayout.a(this);
    }

}