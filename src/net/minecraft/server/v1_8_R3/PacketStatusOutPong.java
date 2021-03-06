package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketStatusOutPong implements Packet<PacketStatusOutListener> {

    private long a;

    public PacketStatusOutPong() {}

    public PacketStatusOutPong(long i) {
        this.a = i;
    }

    @Override
	public void a(PacketDataSerializer packetdataserializer) throws IOException {
        this.a = packetdataserializer.readLong();
    }

    @Override
	public void b(PacketDataSerializer packetdataserializer) throws IOException {
        packetdataserializer.writeLong(this.a);
    }

    @Override
	public void a(PacketStatusOutListener packetstatusoutlistener) {
        packetstatusoutlistener.a(this);
    }

}