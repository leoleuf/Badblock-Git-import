package net.minecraft.server.v1_8_R3;

import java.io.IOException;
import java.util.UUID;

import com.mojang.authlib.GameProfile;

public class PacketLoginInStart implements Packet<PacketLoginInListener> {

    private GameProfile a;

    public PacketLoginInStart() {}

    public PacketLoginInStart(GameProfile gameprofile) {
        this.a = gameprofile;
    }

    @Override
	public void a(PacketDataSerializer packetdataserializer) throws IOException {
        this.a = new GameProfile((UUID) null, packetdataserializer.c(16));
    }

    @Override
	public void b(PacketDataSerializer packetdataserializer) throws IOException {
        packetdataserializer.a(this.a.getName());
    }

    @Override
	public void a(PacketLoginInListener packetlogininlistener) {
        packetlogininlistener.a(this);
    }

    public GameProfile a() {
        return this.a;
    }

}