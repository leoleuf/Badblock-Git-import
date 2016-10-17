package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutHeldItemSlot implements Packet<PacketListenerPlayOut> {

    private int a;

    public PacketPlayOutHeldItemSlot() {}

    public PacketPlayOutHeldItemSlot(int i) {
        this.a = i;
    }

    @Override
	public void a(PacketDataSerializer packetdataserializer) throws IOException {
        this.a = packetdataserializer.readByte();
    }

    @Override
	public void b(PacketDataSerializer packetdataserializer) throws IOException {
        packetdataserializer.writeByte(this.a);
    }

    @Override
	public void a(PacketListenerPlayOut packetlistenerplayout) {
        packetlistenerplayout.a(this);
    }

}