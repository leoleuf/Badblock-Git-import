package fr.badblock.tech.protocol.packets;

import fr.badblock.tech.protocol.PacketHandler;
import fr.badblock.tech.protocol.buffers.ByteInputStream;
import fr.badblock.tech.protocol.buffers.ByteOutputStream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PacketReconnectionInvitation implements Packet {
    private String playerName;
    private boolean invited;

    @Override
    public void read(ByteInputStream input) throws IOException {
        playerName = input.readUTF();
        invited = input.readBoolean();
    }

    @Override
    public void write(ByteOutputStream output) throws IOException {
        output.writeUTF(playerName);
        output.writeBoolean(invited);
    }

    @Override
    public void handle(PacketHandler handler) {
        handler.handle(this);
    }
}