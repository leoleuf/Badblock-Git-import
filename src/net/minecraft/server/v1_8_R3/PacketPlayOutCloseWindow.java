package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutCloseWindow implements Packet<PacketListenerPlayOut> {

    private int a;

    public PacketPlayOutCloseWindow() {}

    public PacketPlayOutCloseWindow(int i) {
        this.a = i;
    }

    @Override
	public void a(PacketListenerPlayOut packetlistenerplayout) {
        packetlistenerplayout.a(this);
    }

    @Override
	public void a(PacketDataSerializer packetdataserializer) throws IOException {
        this.a = packetdataserializer.readUnsignedByte();
    }

    @Override
	public void b(PacketDataSerializer packetdataserializer) throws IOException {
        packetdataserializer.writeByte(this.a);
    }

}