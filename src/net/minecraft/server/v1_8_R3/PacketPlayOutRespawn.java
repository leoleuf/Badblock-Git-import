package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutRespawn implements Packet<PacketListenerPlayOut> {

    private int a;
    private EnumDifficulty b;
    private WorldSettings.EnumGamemode c;
    private WorldType d;

    public PacketPlayOutRespawn() {}

    public PacketPlayOutRespawn(int i, EnumDifficulty enumdifficulty, WorldType worldtype, WorldSettings.EnumGamemode worldsettings_enumgamemode) {
        this.a = i;
        this.b = enumdifficulty;
        this.c = worldsettings_enumgamemode;
        this.d = worldtype;
    }

    @Override
	public void a(PacketListenerPlayOut packetlistenerplayout) {
        packetlistenerplayout.a(this);
    }

    @Override
	public void a(PacketDataSerializer packetdataserializer) throws IOException {
        this.a = packetdataserializer.readInt();
        this.b = EnumDifficulty.getById(packetdataserializer.readUnsignedByte());
        this.c = WorldSettings.EnumGamemode.getById(packetdataserializer.readUnsignedByte());
        this.d = WorldType.getType(packetdataserializer.c(16));
        if (this.d == null) {
            this.d = WorldType.NORMAL;
        }

    }

    @Override
	public void b(PacketDataSerializer packetdataserializer) throws IOException {
        packetdataserializer.writeInt(this.a);
        packetdataserializer.writeByte(this.b.a());
        packetdataserializer.writeByte(this.c.getId());
        packetdataserializer.a(this.d.name());
    }

}