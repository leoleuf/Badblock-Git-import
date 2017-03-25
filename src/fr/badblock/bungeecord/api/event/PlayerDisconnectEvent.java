package fr.badblock.bungeecord.api.event;

import fr.badblock.bungeecord.api.connection.ProxiedPlayer;
import fr.badblock.bungeecord.api.plugin.Event;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Called when a player has left the proxy, it is not safe to call any methods
 * that perform an action on the passed player instance.
 */
@Data
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class PlayerDisconnectEvent extends Event
{

    /**
     * Player disconnecting.
     */
    private final ProxiedPlayer player;
}
