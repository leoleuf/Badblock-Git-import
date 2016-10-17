package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutPlayerListHeaderFooter implements Packet<PacketListenerPlayOut> {

    public net.md_5.bungee.api.chat.BaseComponent[] header, footer; // Paper

    private IChatBaseComponent a;
    private IChatBaseComponent b;

    public PacketPlayOutPlayerListHeaderFooter() {}

    public PacketPlayOutPlayerListHeaderFooter(IChatBaseComponent ichatbasecomponent) {
        this.a = ichatbasecomponent;
    }

    @Override
	public void a(PacketDataSerializer packetdataserializer) throws IOException {
        this.a = packetdataserializer.d();
        this.b = packetdataserializer.d();
    }

    @Override
	public void b(PacketDataSerializer packetdataserializer) throws IOException {
        // Paper start
        if (this.header != null) {
            packetdataserializer.a(net.md_5.bungee.chat.ComponentSerializer.toString(this.header));
        } else {
            packetdataserializer.a(this.a);
        }

        if (this.footer != null) {
            packetdataserializer.a(net.md_5.bungee.chat.ComponentSerializer.toString(this.footer));
        } else {
            packetdataserializer.a(this.b);
        }
        // Paper end
    }

    @Override
	public void a(PacketListenerPlayOut packetlistenerplayout) {
        packetlistenerplayout.a(this);
    }

    // PaperSpigot start - fix compile error
    /*
    */
    // PaperSpigot end
}