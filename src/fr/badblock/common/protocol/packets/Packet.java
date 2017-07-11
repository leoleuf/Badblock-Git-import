package fr.badblock.common.protocol.packets;

import java.io.IOException;

import fr.badblock.common.protocol.PacketHandler;
import fr.badblock.common.protocol.buffers.ByteInputStream;
import fr.badblock.common.protocol.buffers.ByteOutputStream;


/**
 * Repr�sente un Packet Ladder
 * @author LeLanN
 */
public interface Packet {
	public void read(ByteInputStream input) throws IOException;
	
	public void write(ByteOutputStream output) throws IOException;
	
	public void handle(PacketHandler handler) throws Exception;
}
