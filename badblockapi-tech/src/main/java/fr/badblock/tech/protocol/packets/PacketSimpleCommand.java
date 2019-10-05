package fr.badblock.tech.protocol.packets;

import fr.badblock.tech.protocol.PacketHandler;
import fr.badblock.tech.protocol.buffers.ByteInputStream;
import fr.badblock.tech.protocol.buffers.ByteOutputStream;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.IOException;

/**
 * Packet permettant l'utilisation de commandes console :
 *
 * @author LeLanN
 */
@Data
@AllArgsConstructor
public class PacketSimpleCommand implements Packet {
    private String player;
    private String command;

    public PacketSimpleCommand(String command) {
        this(null, command);
    }

    @Override
    public void read(ByteInputStream input) throws IOException {
        player = input.readUTF();
        command = input.readUTF();
    }

    @Override
    public void write(ByteOutputStream output) throws IOException {
        output.writeUTF(player);
        output.writeUTF(command);
    }

    @Override
    public void handle(PacketHandler handler) {
        handler.handle(this);
    }
}