package net.minecraft.server.v1_8_R3;

import java.io.IOException;
import java.util.UUID;

public class PacketPlayInSpectate implements Packet<PacketListenerPlayIn> {

    private UUID a;

    public PacketPlayInSpectate() {}

    public PacketPlayInSpectate(UUID uuid) {
        this.a = uuid;
    }

    @Override
	public void a(PacketDataSerializer packetdataserializer) throws IOException {
        this.a = packetdataserializer.g();
    }

    @Override
	public void b(PacketDataSerializer packetdataserializer) throws IOException {
        packetdataserializer.a(this.a);
    }

    @Override
	public void a(PacketListenerPlayIn packetlistenerplayin) {
        packetlistenerplayin.a(this);
    }

    public Entity a(WorldServer worldserver) {
        return worldserver.getEntity(this.a);
    }

}