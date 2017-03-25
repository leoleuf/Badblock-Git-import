package fr.badblock.common.protocol.packets;

import java.io.IOException;
import java.net.InetSocketAddress;

import fr.badblock.common.protocol.PacketHandler;
import fr.badblock.common.protocol.buffers.ByteInputStream;
import fr.badblock.common.protocol.buffers.ByteOutputStream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data@NoArgsConstructor@AllArgsConstructor
public class PacketPlayerLogin implements Packet {
	private String 			  playerName;
	private InetSocketAddress address;

	@Override
	public void read(ByteInputStream input) throws IOException {
		playerName = input.readUTF();
		address    = new InetSocketAddress(input.readUTF(), input.readInt());
	}

	@Override
	public void write(ByteOutputStream output) throws IOException {
		output.writeUTF(playerName);
		output.writeUTF(address.getAddress().getHostAddress());
		output.writeInt(address.getPort());
	}

	@Override
	public void handle(PacketHandler handler) throws Exception {
		handler.handle(this);
	}
}