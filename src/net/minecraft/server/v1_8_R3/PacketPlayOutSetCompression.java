package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutSetCompression implements Packet<PacketListenerPlayOut> {

    private int a;

    public PacketPlayOutSetCompression() {}

    public void a(PacketDataSerializer packetdataserializer) throws IOException {
        this.a = packetdataserializer.e();
    }

    public void b(PacketDataSerializer packetdataserializer) throws IOException {
        packetdataserializer.b(this.a);
    }

    public void a(PacketListenerPlayOut packetlistenerplayout) {
        packetlistenerplayout.a(this);
    }

}