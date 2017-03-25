package fr.badblock.bungeecord.api.event;

import fr.badblock.bungeecord.api.connection.PendingConnection;
import fr.badblock.bungeecord.api.plugin.Event;
import fr.badblock.bungeecord.protocol.packet.Handshake;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Event called to represent a player first making their presence and username
 * known.
 */
@Data
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class PlayerHandshakeEvent extends Event
{

    /**
     * Connection attempting to login.
     */
    private final PendingConnection connection;
    /**
     * The handshake.
     */
    private final Handshake handshake;

    public PlayerHandshakeEvent(PendingConnection connection, Handshake handshake)
    {
        this.connection = connection;
        this.handshake = handshake;
    }
}
