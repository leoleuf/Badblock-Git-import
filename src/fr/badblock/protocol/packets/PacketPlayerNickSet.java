package fr.badblock.protocol.packets;

import java.io.IOException;
import java.util.UUID;

import fr.badblock.protocol.PacketHandler;
import fr.badblock.protocol.buffers.ByteInputStream;
import fr.badblock.protocol.buffers.ByteOutputStream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data@NoArgsConstructor@AllArgsConstructor
public class PacketPlayerNickSet implements Packet {
	private String 			  playerName;
	private UUID			  uuid;
	private String 			  nickName;

	@Override
	public void read(ByteInputStream input) throws IOException {
		playerName = input.readUTF();
		uuid = input.readUUID();
		nickName = input.readUTF();
	}

	@Override
	public void write(ByteOutputStream output) throws IOException {
		output.writeUTF(playerName);
		output.writeUUID(uuid);
		output.writeUTF(nickName);
	}

	@Override
	public void handle(PacketHandler handler) throws Exception {
		handler.handle(this);
	}
}