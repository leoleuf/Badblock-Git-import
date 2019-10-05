package fr.badblock.tech.protocol.packets;

import fr.badblock.tech.protocol.PacketHandler;
import fr.badblock.tech.protocol.buffers.ByteInputStream;
import fr.badblock.tech.protocol.buffers.ByteOutputStream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;

/**
 * Packet permettant de changer le serveur Spigot du joueur
 *
 * @author LeLanN
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PacketPlayerPlace implements Packet {
    private String playerName;
    private String serverName;

    @Override
    public void read(ByteInputStream input) throws IOException {
        playerName = input.readUTF();
        serverName = input.readUTF();
    }

    @Override
    public void write(ByteOutputStream output) throws IOException {
        output.writeUTF(playerName);
        output.writeUTF(serverName);
    }

    @Override
    public void handle(PacketHandler handler) {
        handler.handle(this);
    }
}
