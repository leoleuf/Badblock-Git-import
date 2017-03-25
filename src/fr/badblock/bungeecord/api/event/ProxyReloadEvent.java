package fr.badblock.bungeecord.api.event;

import fr.badblock.bungeecord.api.CommandSender;
import fr.badblock.bungeecord.api.plugin.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.EqualsAndHashCode;

/**
 * Called when somebody reloads BungeeCord
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProxyReloadEvent extends Event
{

    /**
     * Creator of the action.
     */
    private final CommandSender sender;
}
