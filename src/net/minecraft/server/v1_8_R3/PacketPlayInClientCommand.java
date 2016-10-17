package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayInClientCommand implements Packet<PacketListenerPlayIn> {

    private PacketPlayInClientCommand.EnumClientCommand a;

    public PacketPlayInClientCommand() {}

    public PacketPlayInClientCommand(PacketPlayInClientCommand.EnumClientCommand packetplayinclientcommand_enumclientcommand) {
        this.a = packetplayinclientcommand_enumclientcommand;
    }

    @Override
	public void a(PacketDataSerializer packetdataserializer) throws IOException {
        this.a = packetdataserializer.a(PacketPlayInClientCommand.EnumClientCommand.class);
    }

    @Override
	public void b(PacketDataSerializer packetdataserializer) throws IOException {
        packetdataserializer.a(this.a);
    }

    @Override
	public void a(PacketListenerPlayIn packetlistenerplayin) {
        packetlistenerplayin.a(this);
    }

    public PacketPlayInClientCommand.EnumClientCommand a() {
        return this.a;
    }


    public static enum EnumClientCommand {

        PERFORM_RESPAWN, REQUEST_STATS, OPEN_INVENTORY_ACHIEVEMENT;

        private EnumClientCommand() {}
    }
}