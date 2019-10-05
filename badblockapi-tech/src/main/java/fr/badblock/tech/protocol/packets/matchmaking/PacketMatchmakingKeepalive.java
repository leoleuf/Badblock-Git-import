package fr.badblock.tech.protocol.packets.matchmaking;

import fr.badblock.tech.protocol.PacketHandler;
import fr.badblock.tech.protocol.buffers.ByteInputStream;
import fr.badblock.tech.protocol.buffers.ByteOutputStream;
import fr.badblock.tech.protocol.packets.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Packet permettant � un serveur Bukkit d'indiquer qu'il si il est disponible ou non
 *
 * @author LeLanN
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PacketMatchmakingKeepalive implements Packet {
    private ServerStatus status = ServerStatus.WAITING;
    private int players = 0;
    private int slots = 4;

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

    @Getter
    public enum ServerStatus {
        WAITING(1),
        RUNNING(2),
        FINISHED(3),
        STOPING(4);

        private final int id;

        ServerStatus(int id) {
            this.id = id;
        }

        public static ServerStatus getStatus(int id) {
            for (final ServerStatus status : values()) {
                if (status.getId() == id)
                    return status;
            }

            return null;
        }
    }
}
