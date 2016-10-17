package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketLoginOutSetCompression implements Packet<PacketLoginOutListener> {

    private int a;

    public PacketLoginOutSetCompression() {}

    public PacketLoginOutSetCompression(int i) {
        this.a = i;
    }

    @Override
	public void a(PacketDataSerializer packetdataserializer) throws IOException {
        this.a = packetdataserializer.e();
    }

    @Override
	public void b(PacketDataSerializer packetdataserializer) throws IOException {
        packetdataserializer.b(this.a);
    }

    @Override
	public void a(PacketLoginOutListener packetloginoutlistener) {
        packetloginoutlistener.a(this);
    }

}