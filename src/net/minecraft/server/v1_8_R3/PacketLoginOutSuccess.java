package net.minecraft.server.v1_8_R3;

import java.io.IOException;
import java.util.UUID;

import com.mojang.authlib.GameProfile;

public class PacketLoginOutSuccess implements Packet<PacketLoginOutListener> {

    private GameProfile a;

    public PacketLoginOutSuccess() {}

    public PacketLoginOutSuccess(GameProfile gameprofile) {
        this.a = gameprofile;
    }

    @Override
	public void a(PacketDataSerializer packetdataserializer) throws IOException {
        String s = packetdataserializer.c(36);
        String s1 = packetdataserializer.c(16);
        UUID uuid = UUID.fromString(s);

        this.a = new GameProfile(uuid, s1);
    }

    @Override
	public void b(PacketDataSerializer packetdataserializer) throws IOException {
        UUID uuid = this.a.getId();

        packetdataserializer.a(uuid == null ? "" : uuid.toString());
        packetdataserializer.a(this.a.getName());
    }

    @Override
	public void a(PacketLoginOutListener packetloginoutlistener) {
        packetloginoutlistener.a(this);
    }

}