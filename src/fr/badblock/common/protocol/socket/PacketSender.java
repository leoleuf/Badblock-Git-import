package fr.badblock.common.protocol.socket;

import java.io.IOException;

import fr.badblock.common.protocol.packets.Packet;

public interface PacketSender {
	public void sendPacket(Packet packet);
	public boolean isRunning();
	public void close() throws IOException;
}