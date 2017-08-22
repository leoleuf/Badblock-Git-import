package fr.badblock.protocol.packets;

import java.io.IOException;

import fr.badblock.protocol.PacketHandler;
import fr.badblock.protocol.buffers.ByteInputStream;
import fr.badblock.protocol.buffers.ByteOutputStream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Packet permettant d'indiquer la d�connexion d'un joueur (ou permettant de l'�ject�)
 * @author LeLanN
 */
@Data@NoArgsConstructor@AllArgsConstructor
public class PacketPlayerQuit implements Packet {
	private String   userName;
	private String[] reason;
	
	@Override
	public void read(ByteInputStream input) throws IOException {
		userName   = input.readUTF();
	
		if(input.readBoolean())
			reason = input.readArrayUTF();
	}

	@Override
	public void write(ByteOutputStream output) throws IOException {
		output.writeUTF(userName);
		
		output.writeBoolean(reason != null);
		if(reason != null)
			output.writeArrayUTF(reason);
	}

	@Override
	public void handle(PacketHandler handler) throws Exception {
		handler.handle(this);
	}
}
