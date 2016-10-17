package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutUpdateEntityNBT implements Packet<PacketListenerPlayOut> {

    private int a;
    private NBTTagCompound b;

    public PacketPlayOutUpdateEntityNBT() {}

    public PacketPlayOutUpdateEntityNBT(int i, NBTTagCompound nbttagcompound) {
        this.a = i;
        this.b = nbttagcompound;
    }

    @Override
	public void a(PacketDataSerializer packetdataserializer) throws IOException {
        this.a = packetdataserializer.e();
        this.b = packetdataserializer.h();
    }

    @Override
	public void b(PacketDataSerializer packetdataserializer) throws IOException {
        packetdataserializer.b(this.a);
        packetdataserializer.a(this.b);
    }

    @Override
	public void a(PacketListenerPlayOut packetlistenerplayout) {
        packetlistenerplayout.a(this);
    }

}