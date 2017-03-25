package fr.badblock.bungeecord.api.event;

import fr.badblock.bungeecord.api.config.ServerInfo;
import fr.badblock.bungeecord.api.connection.ProxiedPlayer;
import fr.badblock.bungeecord.api.plugin.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class ServerDisconnectEvent extends Event
{

    /**
     * Player disconnecting from a server.
     */
    @NonNull
    private final ProxiedPlayer player;
    /**
     * Server the player is disconnecting from.
     */
    @NonNull
    private final ServerInfo target;
}
