package fr.badblock.protocol.packets;

import java.io.IOException;

import fr.badblock.protocol.PacketHandler;
import fr.badblock.protocol.buffers.ByteInputStream;
import fr.badblock.protocol.buffers.ByteOutputStream;


/**
 * Repr�sente un Packet Ladder
 * @author LeLanN
 */
public interface Packet {
	public void read(ByteInputStream input) throws IOException;
	
	public void write(ByteOutputStream output) throws IOException;
	
	public void handle(PacketHandler handler) throws Exception;
}
