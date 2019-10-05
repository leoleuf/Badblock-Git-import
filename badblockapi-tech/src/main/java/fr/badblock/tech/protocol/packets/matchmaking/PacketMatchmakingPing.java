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
 * Permet � un serveur (Bukkit) de demander des informations sur le nombre de joueurs / parties
 *
 * @author LeLanN
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PacketMatchmakingPing implements Packet {
    private int id;
    private String[] servers;

    @Override
    public void read(ByteInputStream input) throws IOException {
        id = input.readInt();
        servers = input.readArrayUTF();
    }

    @Override
    public void write(ByteOutputStream output) throws IOException {
        output.writeInt(id);
        output.writeArrayUTF(servers);
    }

    @Override
    public void handle(PacketHandler handler) {
        handler.handle(this);
    }
}
