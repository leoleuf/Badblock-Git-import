package fr.badblock.bungeecord.protocol.packet;

import fr.badblock.bungeecord.protocol.AbstractPacketHandler;
import fr.badblock.bungeecord.protocol.DefinedPacket;
import fr.badblock.bungeecord.protocol.ProtocolConstants;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class EncryptionResponse extends DefinedPacket
{

	private byte[] sharedSecret;
	private byte[] verifyToken;

	@Override
	public void read(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion)
	{
		sharedSecret = readArray( buf, 128 );
		verifyToken = readArray( buf, 128 );
	}

	@Override
	public void write(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion)
	{
		writeArray( sharedSecret, buf );
		writeArray( verifyToken, buf );
	}

	@Override
	public void handle(AbstractPacketHandler handler)
	{
		try {
			handler.handle( this );
		}catch(Exception error) {
			error.printStackTrace();
		}
	}
}
