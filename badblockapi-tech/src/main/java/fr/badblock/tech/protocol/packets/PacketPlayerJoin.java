package fr.badblock.tech.protocol.packets;

import fr.badblock.tech.protocol.PacketHandler;
import fr.badblock.tech.protocol.buffers.ByteInputStream;
import fr.badblock.tech.protocol.buffers.ByteOutputStream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PacketPlayerJoin implements Packet {
    private String playerName;
    private String nickName;
    private UUID uniqueId;
    private InetSocketAddress address;

    @Override
    public void read(ByteInputStream input) throws IOException {
        playerName = input.readUTF();
        nickName = input.readUTF();
        uniqueId = input.readUUID();
        address = new InetSocketAddress(input.readUTF(), input.readInt());
    }

    @Override
    public void write(ByteOutputStream output) throws IOException {
        output.writeUTF(playerName);
        output.writeUTF(nickName);
        output.writeUUID(uniqueId);
        output.writeUTF(address.getAddress().getHostAddress());
        output.writeInt(address.getPort());
    }

    @Override
    public void handle(PacketHandler handler) {
        handler.handle(this);
    }
}
