package fr.badblock.tech.protocol.packets;

import fr.badblock.tech.protocol.PacketHandler;
import fr.badblock.tech.protocol.buffers.ByteInputStream;
import fr.badblock.tech.protocol.buffers.ByteOutputStream;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Packet d'extinction de Ladder
 *
 * @author LeLanN
 */
@Data
@NoArgsConstructor
public class PacketLadderStop implements Packet {
    @Override
    public void read(ByteInputStream input) {
    }

    @Override
    public void write(ByteOutputStream output) {
    }

    @Override
    public void handle(PacketHandler handler) {
        handler.handle(this);
    }

}
