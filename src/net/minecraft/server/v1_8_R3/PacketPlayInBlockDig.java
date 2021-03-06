package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayInBlockDig implements Packet<PacketListenerPlayIn> {

    private BlockPosition a;
    private EnumDirection b;
    private PacketPlayInBlockDig.EnumPlayerDigType c;

    public PacketPlayInBlockDig() {}

    @Override
	public void a(PacketDataSerializer packetdataserializer) throws IOException {
        this.c = packetdataserializer.a(PacketPlayInBlockDig.EnumPlayerDigType.class);
        this.a = packetdataserializer.c();
        this.b = EnumDirection.fromType1(packetdataserializer.readUnsignedByte());
    }

    @Override
	public void b(PacketDataSerializer packetdataserializer) throws IOException {
        packetdataserializer.a(this.c);
        packetdataserializer.a(this.a);
        packetdataserializer.writeByte(this.b.a());
    }

    @Override
	public void a(PacketListenerPlayIn packetlistenerplayin) {
        packetlistenerplayin.a(this);
    }

    public BlockPosition a() {
        return this.a;
    }

    public EnumDirection b() {
        return this.b;
    }

    public PacketPlayInBlockDig.EnumPlayerDigType c() {
        return this.c;
    }


    public static enum EnumPlayerDigType {

        START_DESTROY_BLOCK, ABORT_DESTROY_BLOCK, STOP_DESTROY_BLOCK, DROP_ALL_ITEMS, DROP_ITEM, RELEASE_USE_ITEM;

        private EnumPlayerDigType() {}
    }
}