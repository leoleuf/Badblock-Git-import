package net.minecraft.server.v1_8_R3;

import java.io.IOException;
import java.util.List;

public class PacketPlayOutWindowItems implements Packet<PacketListenerPlayOut> {

    private int a;
    private ItemStack[] b;

    public PacketPlayOutWindowItems() {}

    public PacketPlayOutWindowItems(int i, List<ItemStack> list) {
        this.a = i;
        this.b = new ItemStack[list.size()];

        for (int j = 0; j < this.b.length; ++j) {
            ItemStack itemstack = list.get(j);

            this.b[j] = itemstack == null ? null : itemstack.cloneItemStack();
        }

    }

    @Override
	public void a(PacketDataSerializer packetdataserializer) throws IOException {
        this.a = packetdataserializer.readUnsignedByte();
        short short0 = packetdataserializer.readShort();

        this.b = new ItemStack[short0];

        for (int i = 0; i < short0; ++i) {
            this.b[i] = packetdataserializer.i();
        }

    }

    @Override
	public void b(PacketDataSerializer packetdataserializer) throws IOException {
        packetdataserializer.writeByte(this.a);
        packetdataserializer.writeShort(this.b.length);
        ItemStack[] aitemstack = this.b;
        int i = aitemstack.length;

        for (int j = 0; j < i; ++j) {
            ItemStack itemstack = aitemstack[j];

            packetdataserializer.a(itemstack);
        }

    }

    @Override
	public void a(PacketListenerPlayOut packetlistenerplayout) {
        packetlistenerplayout.a(this);
    }

}