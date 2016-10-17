package net.minecraft.server.v1_8_R3;

import java.io.IOException;

import io.netty.buffer.ByteBuf;

public class PacketPlayOutCustomPayload implements Packet<PacketListenerPlayOut> {

    private String a;
    private PacketDataSerializer b;

    public PacketPlayOutCustomPayload() {}

    public PacketPlayOutCustomPayload(String s, PacketDataSerializer packetdataserializer) {
        this.a = s;
        this.b = packetdataserializer;
        if (packetdataserializer.writerIndex() > 1048576) {
            throw new IllegalArgumentException("Payload may not be larger than 1048576 bytes");
        }
    }

    @Override
	public void a(PacketDataSerializer packetdataserializer) throws IOException {
        this.a = packetdataserializer.c(20);
        int i = packetdataserializer.readableBytes();

        if (i >= 0 && i <= 1048576) {
            this.b = new PacketDataSerializer(packetdataserializer.readBytes(i));
        } else {
            throw new IOException("Payload may not be larger than 1048576 bytes");
        }
    }

    @Override
	public void b(PacketDataSerializer packetdataserializer) throws IOException {
        packetdataserializer.a(this.a);
        packetdataserializer.writeBytes(this.b);
    }

    @Override
	public void a(PacketListenerPlayOut packetlistenerplayout) {
        packetlistenerplayout.a(this);
    }

}