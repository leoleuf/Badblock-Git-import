package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutSetSlot implements Packet<PacketListenerPlayOut> {

    private int a;
    private int b;
    private ItemStack c;

    public PacketPlayOutSetSlot() {}

    public PacketPlayOutSetSlot(int i, int j, ItemStack itemstack) {
        this.a = i;
        this.b = j;
        this.c = itemstack == null ? null : itemstack.cloneItemStack();
    }

    @Override
	public void a(PacketListenerPlayOut packetlistenerplayout) {
        packetlistenerplayout.a(this);
    }

    @Override
	public void a(PacketDataSerializer packetdataserializer) throws IOException {
        this.a = packetdataserializer.readByte();
        this.b = packetdataserializer.readShort();
        this.c = packetdataserializer.i();
    }

    @Override
	public void b(PacketDataSerializer packetdataserializer) throws IOException {
        packetdataserializer.writeByte(this.a);
        packetdataserializer.writeShort(this.b);
        packetdataserializer.a(this.c);
    }

}