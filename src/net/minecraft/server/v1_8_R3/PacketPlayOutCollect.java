package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutCollect implements Packet<PacketListenerPlayOut> {

    private int a;
    private int b;

    public PacketPlayOutCollect() {}

    public PacketPlayOutCollect(int i, int j) {
        this.a = i;
        this.b = j;
    }

    @Override
	public void a(PacketDataSerializer packetdataserializer) throws IOException {
        this.a = packetdataserializer.e();
        this.b = packetdataserializer.e();
    }

    @Override
	public void b(PacketDataSerializer packetdataserializer) throws IOException {
        packetdataserializer.b(this.a);
        packetdataserializer.b(this.b);
    }

    @Override
	public void a(PacketListenerPlayOut packetlistenerplayout) {
        packetlistenerplayout.a(this);
    }

}