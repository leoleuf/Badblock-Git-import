package fr.badblock.ladder.api.entities;

import java.net.InetSocketAddress;

import fr.badblock.protocol.packets.Packet;

public interface Connection {
	public void sendPacket(Packet packet);
	public InetSocketAddress getAddress();
}
