package fr.badblock.bungeecord.api.event;

import java.net.InetSocketAddress;

import fr.badblock.bungeecord.api.plugin.Event;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Called when the proxy can't bind the hostname.
 */
@Data
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class ProxyUnableToBindEvent extends Event
{

    /**
     * The socket address who must be bind.
     */
    private InetSocketAddress address;
    /**
     * The throw error.
     */
    private Throwable throwable;
    

    public ProxyUnableToBindEvent(InetSocketAddress address, Throwable throwable)
    {
        this.address = address;
        this.throwable = throwable;
    }
}
