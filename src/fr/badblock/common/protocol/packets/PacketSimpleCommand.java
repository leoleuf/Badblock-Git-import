package fr.badblock.common.protocol.packets;

import java.io.IOException;

import fr.badblock.common.protocol.PacketHandler;
import fr.badblock.common.protocol.buffers.ByteInputStream;
import fr.badblock.common.protocol.buffers.ByteOutputStream;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Packet permettant l'utilisation de commandes console :
 * @author LeLanN
 */
@Data@AllArgsConstructor
public class PacketSimpleCommand implements Packet {
	private String player;
	private String command;

	public PacketSimpleCommand(String command) {
		this(null, command);
	}

	@Override
	public void read(ByteInputStream input) throws IOException {
		player  = input.readUTF();
		command = input.readUTF();
	}

	@Override
	public void write(ByteOutputStream output) throws IOException {
		output.writeUTF(player);
		output.writeUTF(command);
	}

	@Override
	public void handle(PacketHandler handler) throws Exception {
		handler.handle(this);
	}
}