package fr.badblock.tech.protocol.packets;

import fr.badblock.tech.protocol.PacketHandler;
import fr.badblock.tech.protocol.buffers.ByteInputStream;
import fr.badblock.tech.protocol.buffers.ByteOutputStream;

import java.io.IOException;



/**
 * Repr�sente un Packet Ladder
 * @author LeLanN
 */
public interface Packet {
	void read(ByteInputStream input) throws IOException;
	
	void write(ByteOutputStream output) throws IOException;
	
	void handle(PacketHandler handler) throws Exception;
}
