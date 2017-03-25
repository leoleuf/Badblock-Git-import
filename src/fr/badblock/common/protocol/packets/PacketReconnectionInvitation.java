package fr.badblock.common.protocol.packets;

import java.io.IOException;

import fr.badblock.common.protocol.PacketHandler;
import fr.badblock.common.protocol.buffers.ByteInputStream;
import fr.badblock.common.protocol.buffers.ByteOutputStream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data@NoArgsConstructor@AllArgsConstructor
public class PacketReconnectionInvitation implements Packet {
	private String  playerName;
	private boolean invited;

	@Override
	public void read(ByteInputStream input) throws IOException {
		playerName = input.readUTF();
		invited    = input.readBoolean();
	}

	@Override
	public void write(ByteOutputStream output) throws IOException {
		output.writeUTF(playerName);
		output.writeBoolean(invited);
	}

	@Override
	public void handle(PacketHandler handler) throws Exception {
		handler.handle(this);
	}
}