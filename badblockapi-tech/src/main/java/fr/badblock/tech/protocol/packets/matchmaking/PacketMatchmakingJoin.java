package fr.badblock.tech.protocol.packets.matchmaking;

import fr.badblock.tech.protocol.PacketHandler;
import fr.badblock.tech.protocol.buffers.ByteInputStream;
import fr.badblock.tech.protocol.buffers.ByteOutputStream;
import fr.badblock.tech.protocol.packets.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;

/**
 * Packet permettant � un serveur d'indiquer qu'un joueur veux jouer
 *
 * @author LeLanN
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PacketMatchmakingJoin implements Packet {
    private String serverName;
    private String playerName;

    @Override
    public void read(ByteInputStream input) throws IOException {
        serverName = input.readUTF();
        playerName = input.readUTF();
    }

    @Override
    public void write(ByteOutputStream output) throws IOException {
        output.writeUTF(serverName);
        output.writeUTF(playerName);
    }

    @Override
    public void handle(PacketHandler handler) {
        handler.handle(this);
    }
}
