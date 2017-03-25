package fr.badblock.bungeecord.api.event;

import fr.badblock.bungeecord.api.connection.Connection;
import fr.badblock.bungeecord.api.plugin.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * An event which occurs in the communication between two nodes.
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public abstract class TargetedEvent extends Event
{

    /**
     * Creator of the action.
     */
    private final Connection sender;
    /**
     * Receiver of the action.
     */
    private final Connection receiver;
}
