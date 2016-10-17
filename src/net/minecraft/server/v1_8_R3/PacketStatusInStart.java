package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketStatusInStart implements Packet<PacketStatusInListener> {

    public PacketStatusInStart() {}

    @Override
	public void a(PacketDataSerializer packetdataserializer) throws IOException {}

    @Override
	public void b(PacketDataSerializer packetdataserializer) throws IOException {}

    @Override
	public void a(PacketStatusInListener packetstatusinlistener) {
        packetstatusinlistener.a(this);
    }

}