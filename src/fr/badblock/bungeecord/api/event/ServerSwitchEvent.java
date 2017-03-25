package fr.badblock.bungeecord.api.event;

import fr.badblock.bungeecord.api.connection.ProxiedPlayer;
import fr.badblock.bungeecord.api.plugin.Event;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Called when a player has changed servers.
 */
@Data
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class ServerSwitchEvent extends Event
{

    /**
     * Player whom the server is for.
     */
    private final ProxiedPlayer player;
}
