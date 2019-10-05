package fr.badblock.tech.protocol.socket;

import java.io.IOException;

import fr.badblock.tech.protocol.packets.Packet;

public interface PacketSender {
	void sendPacket(Packet packet);
	boolean isRunning();
	void close() throws IOException;
}