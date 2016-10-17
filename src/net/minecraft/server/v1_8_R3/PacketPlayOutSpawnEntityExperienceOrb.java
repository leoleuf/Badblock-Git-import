package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutSpawnEntityExperienceOrb implements Packet<PacketListenerPlayOut> {

    private int a;
    private int b;
    private int c;
    private int d;
    private int e;

    public PacketPlayOutSpawnEntityExperienceOrb() {}

    public PacketPlayOutSpawnEntityExperienceOrb(EntityExperienceOrb entityexperienceorb) {
        this.a = entityexperienceorb.getId();
        this.b = MathHelper.floor(entityexperienceorb.locX * 32.0D);
        this.c = MathHelper.floor(entityexperienceorb.locY * 32.0D);
        this.d = MathHelper.floor(entityexperienceorb.locZ * 32.0D);
        this.e = entityexperienceorb.j();
    }

    @Override
	public void a(PacketDataSerializer packetdataserializer) throws IOException {
        this.a = packetdataserializer.e();
        this.b = packetdataserializer.readInt();
        this.c = packetdataserializer.readInt();
        this.d = packetdataserializer.readInt();
        this.e = packetdataserializer.readShort();
    }

    @Override
	public void b(PacketDataSerializer packetdataserializer) throws IOException {
        packetdataserializer.b(this.a);
        packetdataserializer.writeInt(this.b);
        packetdataserializer.writeInt(this.c);
        packetdataserializer.writeInt(this.d);
        packetdataserializer.writeShort(this.e);
    }

    @Override
	public void a(PacketListenerPlayOut packetlistenerplayout) {
        packetlistenerplayout.a(this);
    }

}